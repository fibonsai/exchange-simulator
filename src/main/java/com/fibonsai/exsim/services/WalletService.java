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
import static com.fibonsai.exsim.services.WalletService.State.*;
import static com.fibonsai.exsim.services.WalletService.Wallet.DEFAULT_CURRENCY;
import static com.fibonsai.exsim.services.WalletService.Wallet.NULL;
import static reactor.core.publisher.Sinks.EmitResult.FAIL_CANCELLED;
import static reactor.core.publisher.Sinks.EmitResult.FAIL_NON_SERIALIZED;

@Slf4j
@Service
public class WalletService extends AbstractService {

    private record WalletKey(String owner, String address) {}
    private final Map<WalletKey, Wallet> wallets = new ConcurrentHashMap<>();
    private final Sinks.Many<Event> events = Sinks.many().multicast().onBackpressureBuffer();
    private final Set<String> currenciesWithOnlyOneAddress = Collections.synchronizedSet(new HashSet<>());

    @SuppressWarnings("unused")
    public enum State {
        ONLINE,
        OFFLINE,
        SYNC_ERROR,
        AUDIT_BLOCK,
        READ_ONLY,
        WITHDRAW_ONLY;

        public boolean is(State... states) {
            return List.of(states).contains(this);
        }
    }

    public static class Wallet {

        private static final String NULL_OWNER = "NULL";

        public static final Currency DEFAULT_CURRENCY = Currency.getInstance("USD");
        public static final Wallet NULL = new Wallet(NULL_OWNER, DEFAULT_CURRENCY, UUID.randomUUID().toString()) {
            public Wallet transaction(FundsParams params) { return this; }
            public State state() { return READ_ONLY; }
        };

        private final Currency currency;
        private final String walletAddress;
        private final String owner;

        private BigDecimal amount = BigDecimal.ZERO;
        private State state = OFFLINE;
        private Instant timestamp = Instant.now();

        public Wallet(String owner, Currency currency, String walletAddress) {
            this.owner = owner;
            this.currency = currency;
            this.walletAddress = walletAddress;
        }

        public String owner() {
            return owner;
        }

        public String address() {
            return walletAddress;
        }

        public Currency currency() {
            return currency;
        }

        public BigDecimal amount() {
            return amount;
        }

        public Instant timestamp() {
            return timestamp;
        }

        public State state() {
            return state;
        }

        public Wallet setState(@NonNull State state) {
            this.state = state;
            this.timestamp = Instant.now();
            return this;
        }

        public Wallet transaction(FundsParams params) throws Exception {
            if (state().is(OFFLINE, SYNC_ERROR, AUDIT_BLOCK, READ_ONLY)) {
                throw new IllegalStateException("Transaction is not possible. Wallet state is " + state());
            }
            if (!currency().equals(params.getCurrency())) {
                throw new IllegalArgumentException("Transaction not possible using different currency: %s != %s".formatted(currency(), params.getCurrency()));
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
                    { "timestamp": %s, "currency": %s, "state": %s, "walletAddress": "%s", "owner": "%s", "amount": %s }
                    """.formatted(timestamp(), currency(), state(), address(), owner(), amount());
        }
    }

    public WalletService() {
        super();
    }

    public void currenciesWithOnlyOneAddress(Set<String> currenciesWithOnlyOneAddress) {
        this.currenciesWithOnlyOneAddress.clear();
        this.currenciesWithOnlyOneAddress.addAll(Optional.ofNullable(currenciesWithOnlyOneAddress).orElse(Set.of()));
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

    public Mono<Wallet> setState(Wallet wallet, State state) {
        wallet.setState(state);
        send(new Event(INFO, wallet.toString()), null, null);
        return Mono.just(wallet);
    }

    public Mono<Wallet> createDefaultWallet(String owner) throws IllegalArgumentException {
        return createWallet(owner, DEFAULT_CURRENCY);
    }

    public Mono<Wallet> createWallet(String owner, Currency currency) throws IllegalArgumentException {
        return createWallet(owner, currency, UUID.randomUUID().toString());
    }

    public Mono<Wallet> createWallet(String owner, Currency currency, String walletAddress) throws IllegalArgumentException {
        WalletKey key = new WalletKey(owner, walletAddress);
        if (wallets.containsKey(key)) {
            return Mono.error(new IllegalArgumentException("Wallet %s already exists".formatted(key)));
        }
        Optional<Wallet> walletFromRepository = getWallet(owner, currency).blockOptional();
        if (walletFromRepository.isPresent() &&
                currenciesWithOnlyOneAddress.contains(walletFromRepository.get().currency.getCurrencyCode())) {
            return Mono.error(new IllegalArgumentException(
                    "Multiple wallets addresses not allowed using %s currency".formatted(currency)));
        }
        Wallet wallet = new Wallet(owner, currency, walletAddress);
        wallets.putIfAbsent(key, wallet);
        log.info("Created {} wallet to account {} with id {}", currency, owner, walletAddress);
        send(new Event(INFO, wallet.toString()), null, null);
        return Mono.just(wallet);
    }

    public Mono<Wallet> getDefaultWallet(String owner) {
        return getWallet(owner, DEFAULT_CURRENCY);
    }

    public Mono<Wallet> getWallet(String owner, Currency currency) {
        return Mono.fromCallable(() -> {
                    var locatedWallets = wallets.values().stream()
                            .filter(wallet -> wallet.currency().equals(currency))
                            .filter(wallet -> wallet.owner().equals(owner))
                            .collect(Collectors.toSet());
                    if (locatedWallets.isEmpty()) {
                        return NULL;
                    }
                    if (locatedWallets.size() > 1) {
                        throw new IllegalArgumentException(
                                "Cannot return a single %s wallet when multiple wallets have the same %s currency."
                                .formatted(currency, currency));
                    }
                    return locatedWallets.iterator().next();
                })
                .doOnError(error -> log.error(error.getMessage(), error))
                .filter(wallet -> !wallet.equals(NULL));
    }

    public Mono<Wallet> getWallet(String owner, String walletAddress) {
        WalletKey key = new WalletKey(owner, walletAddress);
        return Mono.just(Optional.ofNullable(wallets.get(key)).orElseGet(() -> {
            log.error("Wallet {} NOT FOUND", walletAddress);
            return NULL;
        })).filter(wallet -> wallet.owner().equals(owner));
    }

    private Mono<Wallet> getWallet(String owner, Object walletId, String traceid) {
        return switch (walletId) {
            case String walletAddress -> getWallet(owner, walletAddress);
            case Currency currency -> getWallet(owner, currency);
            default -> {
                log.error("{}: Unexpected value: {}", traceid, walletId);
                yield Mono.just(NULL);
            }
        };
    }

    @SuppressWarnings("unused")
    public Mono<Wallet> transaction(String owner, Object walletId, FundsParams params) {
        final String traceid = UUID.randomUUID().toString();
        var monoWallet = getWallet(owner, walletId, traceid);
        return monoWallet.flatMap(wallet -> {
                    try {
                        wallet.transaction(params);
                        log.info("{}: Transaction successful: wallet ({}) owned by {}", traceid, wallet.address(), owner);
                        send(new Event(INFO, wallet.toString()), traceid, null);
                        return Mono.just(wallet);
                    } catch (Throwable e) {
                        String errorMessage = "%s: Transaction error: wallet (%s) owned by %s".formatted(traceid, wallet.address(), owner);
                        log.error(errorMessage, e);
                        send(new Event(ERROR, wallet.toString()), traceid, e);
                        return Mono.error(e);
                    }
                });
     }

}
