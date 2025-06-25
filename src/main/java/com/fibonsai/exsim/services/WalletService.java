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

import com.fibonsai.exsim.dto.Asset;
import com.fibonsai.exsim.dto.Event;
import com.fibonsai.exsim.types.DepositFundsParams;
import com.fibonsai.exsim.types.FundsParams;
import com.fibonsai.exsim.types.WithdrawFundsParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.fibonsai.exsim.dto.Event.EventType.ERROR;
import static com.fibonsai.exsim.dto.Event.EventType.INFO;
import static com.fibonsai.exsim.services.WalletService.WalletState.*;
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


    @SuppressWarnings("unused")
    public enum WalletState {
        ONLINE,
        OFFLINE,
        SYNC_ERROR,
        AUDIT_BLOCK,
        READ_ONLY,
        WITHDRAW_ONLY;

        public boolean is(WalletState... states) {
            return List.of(states).contains(this);
        }
    }

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

    private void send(Event event, @Nullable String traceId, @Nullable Throwable error) {
        Sinks.EmitResult result = events.tryEmitNext(event);
        if (result.equals(FAIL_NON_SERIALIZED) || result.equals(FAIL_CANCELLED)) {
            log.error("Problem to send event: {}", result, error);
        }
    }

    public Mono<Wallet> setState(Wallet wallet, WalletState state) {
        wallet.setState(state);
        send(new Event(INFO, wallet.toString()), null, null);
        return Mono.just(wallet);
    }

    public Mono<Wallet> createDefaultWallet(String owner) throws IllegalArgumentException {
        return createWallet(owner, assetService.defaultAsset());
    }

    public Mono<Wallet> createWallet(String owner, Asset asset) throws IllegalArgumentException {
        return createWallet(owner, asset, UUID.randomUUID().toString());
    }

    public Mono<Wallet> createWallet(String owner, Asset asset, String walletAddress) throws IllegalArgumentException {
        WalletKey key = new WalletKey(owner, walletAddress);
        if (wallets.containsKey(key)) {
            return Mono.error(new IllegalArgumentException("Wallet %s already exists".formatted(key)));
        }
        Optional<Wallet> walletFromRepository = getWallet(owner, asset).blockOptional();
        if (walletFromRepository.isPresent() &&
                assetWithOneAddress.contains(walletFromRepository.get().asset.name())) {
            return Mono.error(new IllegalArgumentException(
                    "Multiple wallets addresses not allowed using %s asset".formatted(asset)));
        }
        Wallet wallet = new Wallet(owner, asset, walletAddress);
        wallets.putIfAbsent(key, wallet);
        log.info("Created {} wallet to account {} with id {}", asset, owner, walletAddress);
        send(new Event(INFO, wallet.toString()), null, null);
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
                send(new Event(INFO, wallet.toString()), traceId, null);
                return Mono.just(wallet);
            } catch (Throwable e) {
                String errorMessage = "%s: Transaction error: wallet (%s) owned by %s".formatted(traceId, wallet.address(), owner);
                log.error(errorMessage, e);
                send(new Event(ERROR, wallet.toString()), traceId, e);
                return Mono.error(e);
            }
        });
     }

    public static class Wallet {

        private final Asset asset;
        private final String walletAddress;
        private final String owner;

        private BigDecimal amount = BigDecimal.ZERO;
        private WalletState state = OFFLINE;
        private Instant timestamp = Instant.now();

        public Wallet(String owner, Asset asset, String walletAddress) {
            this.owner = owner;
            this.asset = asset;
            this.walletAddress = walletAddress;
        }

        public String owner() {
            return owner;
        }

        public String address() {
            return walletAddress;
        }

        public Asset asset() {
            return asset;
        }

        public BigDecimal amount() {
            return amount;
        }

        public Instant timestamp() {
            return timestamp;
        }

        public WalletState state() {
            return state;
        }

        public Wallet setState(@NonNull WalletState state) {
            this.state = state;
            this.timestamp = Instant.now();
            return this;
        }

        public Wallet transaction(FundsParams params) throws Exception {
            if (state().is(OFFLINE, SYNC_ERROR, AUDIT_BLOCK, READ_ONLY)) {
                throw new IllegalStateException("Transaction is not possible. Wallet state is " + state());
            }
            if (!asset().equals(params.getAsset())) {
                throw new IllegalArgumentException("Transaction not possible using different iso4217: %s != %s".formatted(asset(), params.getAsset()));
            }
            switch (params) {
                case DepositFundsParams dfParams -> {
                    if (state().equals(WITHDRAW_ONLY)) {
                        throw new IllegalStateException("Deposit is not possible. Wallet allow only withdraw transaction");
                    }
                    this.amount = this.amount.add(dfParams.getAmount());
                }
                case WithdrawFundsParams wfParams -> {
                    var withdrawFundsAmount = wfParams.getAmount();
                    if (withdrawFundsAmount.compareTo(amount) <= 0) {
                        this.amount = this.amount.subtract(wfParams.getAmount());
                    } else {
                        throw new InsufficientResourcesException("Funds insufficient");
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + params);
            }
            this.timestamp = Instant.now();
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            Wallet wallet = (Wallet) o;
            return walletAddress.equals(wallet.walletAddress);
        }

        @Override
        public int hashCode() {
            return walletAddress.hashCode();
        }

        @Override
        public String toString() {
            return """
                    { "timestamp": %s, "iso4217": %s, "state": %s, "walletAddress": "%s", "owner": "%s", "amount": %s }
                    """.formatted(timestamp(), asset(), state(), address(), owner(), amount());
        }
    }

}
