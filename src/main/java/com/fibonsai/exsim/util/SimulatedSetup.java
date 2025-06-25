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

package com.fibonsai.exsim.util;

import com.fibonsai.exsim.dto.WalletState;
import com.fibonsai.exsim.services.AccountService;
import com.fibonsai.exsim.services.ExchangeHubService;
import com.fibonsai.exsim.services.WalletService;
import com.fibonsai.exsim.types.DepositFundsParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class SimulatedSetup implements ApplicationListener<ApplicationReadyEvent> {

    private final ExchangeHubService exchangeHubService;
    private final AccountService accountService;
    private final WalletService walletService;

    public SimulatedSetup(ExchangeHubService exchangeHubService, AccountService accountService, WalletService walletService) {
        log.info("Loading {}", this.getClass().getSimpleName());
        this.exchangeHubService = exchangeHubService;
        this.accountService = accountService;
        this.walletService = walletService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent e) {
        log.info("{}", e);

        walletService.events().subscribe(event -> log.info(event.toString()));
        exchangeHubService.start().subscribe(instant -> {
            log.info("setup executed {}", instant);
            for (int i = 0; i < 10; i++) {
                var account = "account" + i;
                accountService.addAccount(account).subscribe(acc -> {
                    walletService.getDefaultWallet(acc).flatMap(wallet ->
                        walletService.setState(wallet, WalletState.ONLINE)
                    ).subscribe(wallet -> {
                        walletService.transaction(acc, wallet.address(), new DepositFundsParams() {
                            public BigDecimal getAmount() { return BigDecimal.TEN; }
                        }).subscribe();
                    });
                });
            }
        });
    }
}
