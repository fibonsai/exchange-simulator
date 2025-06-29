/*
 * Copyright (c) 2025 fibonsai.com
 * All rights reserved.
 *
 * This source is subject to the Apache License, Version 2.0.
 * Please see the LICENSE file for more information.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fibonsai.exsim.services;

import com.fibonsai.exsim.dto.Event;
import com.fibonsai.exsim.dto.Wallet;
import com.fibonsai.exsim.dto.WalletState;
import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.types.FundsParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.fibonsai.exsim.dto.Event.EventType.ERROR;
import static com.fibonsai.exsim.dto.Event.EventType.INFO;
import static com.fibonsai.exsim.dto.Wallet.ADDRESS_DEFAULT;
import static com.fibonsai.exsim.dto.WalletState.READ_ONLY;
import static reactor.core.publisher.Sinks.EmitResult.FAIL_CANCELLED;
import static reactor.core.publisher.Sinks.EmitResult.FAIL_NON_SERIALIZED;

@Slf4j
@Service
public class WalletService extends AbstractService {

    private final AssetService assetService;

    private record WalletKey(String owner, String address) {}
    private final Map<WalletKey, Wallet> wallets = new ConcurrentHashMap<>();
    private final Set<String> assetWithOneAddress = Collections.synchronizedSet(new HashSet<>());
    private Sinks.Many<Event> events = Sinks.many().multicast().onBackpressureBuffer();


    public WalletService(AssetService assetService) {
        super();
        this.assetService = assetService;
    }

    public Wallet nullWallet() {
        return new Wallet("NULL_OWNER", assetService.defaultAsset(), UUID.randomUUID().toString()) {
            public Wallet transaction(FundsParams params) {
                return this;
            }
            public WalletState state() {
                return READ_ONLY;
            }
        };
    }

    public void reset() {
        log.warn("<<< Resetting wallet service >>>");
        wallets.clear();
        assetWithOneAddress.clear();
        events.tryEmitComplete();
        events = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void assetWithOneAddress(Set<String> assetWithOneAddress) {
        this.assetWithOneAddress.clear();
        this.assetWithOneAddress.addAll(Optional.ofNullable(assetWithOneAddress).orElse(Set.of()));
    }

    public Flux<Event> events() {
        return events.asFlux();
    }

    private void send(Event event) {
        Sinks.EmitResult result = events.tryEmitNext(event);
        if (result.equals(FAIL_NON_SERIALIZED) || result.equals(FAIL_CANCELLED)) {
            log.error("Problem to send event: {}", result);
        }
    }

    public Mono<Wallet> setState(Wallet wallet, WalletState state) {
        wallet.setState(state);
        send(new Event(INFO, wallet.toString()));
        return Mono.just(wallet);
    }

    public Mono<Wallet> createDefaultWallet(String owner) {
        return createWallet(owner, assetService.defaultAsset(), ADDRESS_DEFAULT);
    }

    public Mono<Wallet> createDefaultWallet(String owner, Asset asset) {
        return createWallet(owner, asset, ADDRESS_DEFAULT);
    }

    public Mono<Wallet> createWallet(String owner, Asset asset) {
        return createWallet(owner, asset, UUID.randomUUID().toString());
    }

    public Mono<Wallet> createWallet(String owner, Asset asset, String walletAddress) {
        WalletKey key = new WalletKey(owner, walletAddress);
        if (wallets.containsKey(key)) {
            var error = new IllegalArgumentException("Wallet %s already exists".formatted(key));
            send(new Event(ERROR, error.getMessage(), null, error));
            return Mono.error(error);
        }
        Optional<Wallet> walletFromRepository = getWallet(owner, asset).blockOptional();
        if (walletFromRepository.isPresent() &&
                assetWithOneAddress.contains(walletFromRepository.get().asset().symbol())) {
            var error = new IllegalArgumentException(
                    "Multiple wallets addresses not allowed using %s asset".formatted(asset.symbol()));
            send(new Event(ERROR, error.getMessage(), null, error));
            return Mono.error(error);
        }
        Wallet wallet = new Wallet(owner, asset, walletAddress);
        wallets.putIfAbsent(key, wallet);
        log.info("Created {} wallet to account {} with id {}", asset.symbol(), owner, walletAddress);
        send(new Event(INFO, wallet.toString()));
        return Mono.just(wallet);
    }

    public Mono<Wallet> getDefaultWallet(String owner) {
        return getWallet(owner, assetService.defaultAsset());
    }

    public Mono<Wallet> getWallet(String owner, Asset asset) {
        return Mono.fromCallable(() -> {
                var locatedWallets = wallets.values().stream()
                        .filter(wallet -> wallet.asset().equals(asset))
                        .filter(wallet -> wallet.owner().equals(owner))
                        .collect(Collectors.toSet());
                if (locatedWallets.isEmpty()) {
                    return nullWallet();
                }
                if (locatedWallets.size() > 1) {
                    throw new IllegalArgumentException(
                            "Cannot return a single %s wallet when multiple wallets have the same %s asset."
                            .formatted(asset, asset));
                }
                return locatedWallets.iterator().next();
            })
            .doOnError(error -> log.error(error.getMessage(), error))
            .filter(wallet -> !wallet.equals(nullWallet()));
    }

    public Mono<Wallet> getWallet(String owner, String walletAddress) {
        WalletKey key = new WalletKey(owner, walletAddress);
        return Mono.just(Optional.ofNullable(wallets.get(key)).orElseGet(() -> {
            log.error("Wallet {} NOT FOUND", walletAddress);
            return nullWallet();
        })).filter(wallet -> wallet.owner().equals(owner));
    }

    private Mono<Wallet> getWallet(String owner, Object walletId, String traceid) {
        return switch (walletId) {
            case String walletAddress -> getWallet(owner, walletAddress);
            case Asset asset -> getWallet(owner, asset);
            default -> {
                log.error("{}: Unexpected value: {}", traceid, walletId);
                yield Mono.just(nullWallet());
            }
        };
    }

    @SuppressWarnings("unused")
    public Mono<Wallet> transaction(String owner, Object walletId, FundsParams params) {
        final String traceId = UUID.randomUUID().toString();
        var monoWallet = getWallet(owner, walletId, traceId);
        return monoWallet.flatMap(wallet -> {
            try {
                wallet.transaction(params);
                log.info("{}: Transaction successful: wallet ({}) owned by {}", traceId, wallet.address(), owner);
                send(new Event(INFO, wallet.toString()));
                return Mono.just(wallet);
            } catch (Throwable e) {
                String errorMessage = "%s: Transaction error: wallet (%s) owned by %s".formatted(traceId, wallet.address(), owner);
                log.warn(errorMessage);
                send(new Event(ERROR, wallet.toString()));
                return Mono.error(e);
            }
        });
     }

}
