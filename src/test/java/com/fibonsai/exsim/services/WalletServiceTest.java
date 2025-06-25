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

import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.types.DepositFundsParams;
import com.fibonsai.exsim.types.WithdrawFundsParams;
import com.fibonsai.exsim.util.AssetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.*;

import static com.fibonsai.exsim.dto.Event.EventType.ERROR;
import static com.fibonsai.exsim.types.WalletState.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class WalletServiceTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private AssetService assetService;

    private final Asset USD = AssetUtil.fromCurrency(Currency.getInstance("USD"));
    private final Asset EUR = AssetUtil.fromCurrency(Currency.getInstance("EUR"));
    private final DepositFundsParams depositTenUsd = new DepositFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN; }
        public Asset getAsset() { return USD; }
    };
    private final DepositFundsParams depositZeroUsd = new DepositFundsParams() {
        public Asset getAsset() { return USD; }
    };
    private final DepositFundsParams deposit100Usd = new DepositFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN.multiply(BigDecimal.TEN); }
        public Asset getAsset() { return USD; }
    };
    private final DepositFundsParams depositTenEur = new DepositFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN; }
        public Asset getAsset() { return EUR; }
    };
    private final WithdrawFundsParams withdrawTenUsd = new WithdrawFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN; }
        public Asset getAsset() { return USD; }
    };
    private final WithdrawFundsParams withdrawZeroUsd = new WithdrawFundsParams() {
        public Asset getAsset() { return USD; }
    };
    private final WithdrawFundsParams withdraw100Usd = new WithdrawFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN.multiply(BigDecimal.TEN); }
        public Asset getAsset() { return USD; }
    };
    private final WithdrawFundsParams withdrawTenEur = new WithdrawFundsParams() {
        public BigDecimal getAmount() { return BigDecimal.TEN; }
        public Asset getAsset() { return EUR; }
    };

    @BeforeEach
    public void setUp() {
        walletService.reset();
    }

    @Test
    void createDefaultWallet_shouldCreateUsdWallet() {
        String owner = "testOwner";

        StepVerifier.create(walletService.events().take(1)).then(() ->
            StepVerifier.create(walletService.createDefaultWallet(owner))
                .consumeNextWith(wallet -> {
                    assertEquals(owner, wallet.owner());
                    assertEquals(assetService.defaultAsset(), wallet.asset());
                    assertNotNull(wallet.address());
                    assertEquals(BigDecimal.ZERO, wallet.amount());
                    assertEquals(OFFLINE, wallet.state());
                })
                .verifyComplete()
            )
            .expectNextMatches(event -> event.event() instanceof String e && e.contains(owner))
            .thenCancel()
            .verify();
    }

    @Test
    void createWallet_withCurrency_shouldCreateWallet() {
        String owner = "testOwner";

        StepVerifier.create(walletService.events().take(1)).then(() ->
            StepVerifier.create(walletService.createWallet(owner, EUR))
                .consumeNextWith(wallet -> {
                    assertEquals(owner, wallet.owner());
                    assertEquals(EUR, wallet.asset());
                    assertNotNull(wallet.address());
                    assertEquals(BigDecimal.ZERO, wallet.amount());
                    assertEquals(OFFLINE, wallet.state());
                })
                .verifyComplete()
            )
            .expectNextMatches(event -> event.event() instanceof String e && e.contains(owner))
            .thenCancel()
            .verify();
    }

    @Test
    void createWallet_withCurrencyAndAddress_shouldCreateWallet() {
        String owner = "testOwnerWithAddress";
        String address = UUID.randomUUID().toString();

        StepVerifier.create(walletService.events().take(1)).then(() ->
            StepVerifier.create(walletService.createWallet(owner, USD, address))
                .consumeNextWith(wallet -> {
                    assertEquals(owner, wallet.owner());
                    assertEquals(USD, wallet.asset());
                    assertEquals(address, wallet.address());
                    assertEquals(BigDecimal.ZERO, wallet.amount());
                    assertEquals(OFFLINE, wallet.state());
                })
                .verifyComplete()
            )
            .expectNextMatches(event -> event.event() instanceof String e && e.contains(owner))
            .thenCancel()
            .verify();
    }

    @Test
    void createWallet_throwIfDuplicated() {
        String owner = "testOwnerWithAddress";
        String address = UUID.randomUUID().toString();

        StepVerifier.create(walletService.events().take(1)).then(() ->
            StepVerifier.create(walletService.createWallet(owner, USD, address)).then(() ->
                StepVerifier.create(walletService.createWallet(owner, USD, address))
                    .verifyError(IllegalArgumentException.class)
                )
                .thenConsumeWhile(Objects::nonNull)
                .verifyComplete()
            )
            .thenCancel()
            .verify();
    }

    @Test
    void createWallet_throwIfNotAllowedMultiAddress() {
        String owner = "testOwner";
        walletService.assetWithOneAddress(Set.of(USD.name()));

        StepVerifier.create(walletService.events().take(1)).then(() ->
            StepVerifier.create(walletService.createWallet(owner, USD)).then(() ->
                StepVerifier.create(walletService.createWallet(owner, USD))
                    .consumeErrorWith(error -> {
                        assertTrue(error.getMessage().contains("already exists"));
                        assertInstanceOf(error.getClass(), IllegalArgumentException.class);
                    })
                    .verify()
                )
        )
        .thenCancel()
        .verify();
    }

    @Test
    void transaction_throwIllegalStateException() {
        String owner = "testOwner";

        StepVerifier.create(walletService.createWallet(owner, USD))
                .consumeNextWith(wallet -> assertEquals(owner, wallet.owner()))
                .verifyComplete();

        for (var state: List.of(OFFLINE, SYNC_ERROR, AUDIT_BLOCK, READ_ONLY)) {
            walletService.getWallet(owner, USD).subscribe(wallet -> wallet.setState(state));

            StepVerifier.create(walletService.transaction(owner, USD, depositTenUsd))
                .consumeErrorWith(error -> {
                    assertEquals("Transaction is not possible. Wallet state is " + state,
                            error.getMessage());
                    assertInstanceOf(IllegalStateException.class, error);
                })
                .verify();
        }

        StepVerifier.create(walletService.events())
            .consumeNextWith(event ->
                StepVerifier.create(walletService.getWallet(owner, USD)).consumeNextWith(wallet -> {
                        assertTrue(event.event().toString().contains(wallet.owner()));
                        assertNotEquals(ERROR, event.type());
                    })
                    .verifyComplete()
            )
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(OFFLINE.toString()));
                assertEquals(ERROR, event.type());
            })
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(SYNC_ERROR.toString()));
                assertEquals(ERROR, event.type());
            })
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(AUDIT_BLOCK.toString()));
                assertEquals(ERROR, event.type());
            })
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(READ_ONLY.toString()));
                assertEquals(ERROR, event.type());
            })
            .thenCancel()
            .verify();
    }

    @Test
    void transaction_throwIfStateWithdrawOnly() {
        String owner = "testOwner";
        BigDecimal initialAmount = BigDecimal.TEN;

        StepVerifier.create(walletService.createWallet(owner, USD))
                .consumeNextWith(wallet -> assertEquals(owner, wallet.owner()))
                .verifyComplete();
        walletService.getWallet(owner, USD).subscribe(wallet -> wallet.setState(ONLINE));
        walletService.transaction(owner, USD, depositTenUsd).subscribe(wallet -> log.info(wallet.toString()));
        walletService.getWallet(owner, USD).subscribe(wallet -> wallet.setState(WITHDRAW_ONLY));

        StepVerifier.create(walletService.transaction(owner, USD, depositTenUsd))
                .consumeErrorWith(error -> {
                    assertEquals("Deposit is not possible. Wallet allow only withdraw transaction",
                            error.getMessage());
                    assertInstanceOf(IllegalStateException.class, error);
                })
                .verify();

        StepVerifier.create(walletService.events())
            .consumeNextWith(event ->
                StepVerifier.create(walletService.getWallet(owner, USD)).consumeNextWith(wallet -> {
                        assertTrue(event.event().toString().contains(wallet.owner()));
                        assertNotEquals(ERROR, event.type());
                    })
                    .verifyComplete()
            )
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(ONLINE.toString()));
                assertNotEquals(ERROR, event.type());
            })
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(WITHDRAW_ONLY.toString()));
                assertEquals(ERROR, event.type());
                StepVerifier.create(walletService.getWallet(owner, USD)).consumeNextWith(wallet ->
                        assertEquals(initialAmount, wallet.amount()))
                        .verifyComplete();
            })
            .thenCancel()
            .verify();
    }

    @Test
    void transaction_throwIfFundsIsInsufficient() {
        String owner = "testOwner";
        BigDecimal initialAmount = BigDecimal.TEN;

        StepVerifier.create(walletService.createWallet(owner, USD))
                .consumeNextWith(wallet -> assertEquals(owner, wallet.owner()))
                .verifyComplete();
        walletService.getWallet(owner, USD).subscribe(wallet -> wallet.setState(ONLINE));
        walletService.transaction(owner, USD, depositTenUsd).subscribe(wallet -> log.info(wallet.toString()));

        StepVerifier.create(walletService.transaction(owner, USD, withdraw100Usd))
                .consumeErrorWith(error -> {
                    assertEquals("Funds insufficient", error.getMessage());
                    assertInstanceOf(InsufficientResourcesException.class, error);
                })
                .verify();

        StepVerifier.create(walletService.events())
            .consumeNextWith(event ->
                StepVerifier.create(walletService.getWallet(owner, USD)).consumeNextWith(wallet -> {
                        assertTrue(event.event().toString().contains(wallet.owner()));
                        assertNotEquals(ERROR, event.type());
                    })
                    .verifyComplete()
            )
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(ONLINE.toString()));
                assertNotEquals(ERROR, event.type());
            })
            .consumeNextWith(event -> {
                assertTrue(event.toString().contains(ONLINE.toString()));
                assertEquals(ERROR, event.type());
                StepVerifier.create(walletService.getWallet(owner, USD)).consumeNextWith(wallet ->
                            assertEquals(initialAmount, wallet.amount())
                        )
                        .verifyComplete();
            })
            .thenCancel()
            .verify();
    }
}
