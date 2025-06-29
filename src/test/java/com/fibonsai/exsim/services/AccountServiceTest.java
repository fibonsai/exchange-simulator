
/*
 *  Copyright (c) 2025 fibonsai.com
 *  All rights reserved.
 *
 *  This source is subject to the Apache License, Version 2.0.
 *  Please see the LICENSE file for more information.
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fibonsai.exsim.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AccountServiceTest {

    @Autowired
    private WalletService walletService;

    @Autowired
    private AccountService accountService;

    @Test
    void testAddAccount() {
        String accountStr = "testAccount";

        StepVerifier.create(accountService.addAccount(accountStr))
                .expectNext(accountStr)
                .verifyComplete();

        assertNotNull(walletService.getDefaultWallet(accountStr));
    }

    @Test
    void testAddAccountWithExistingAccount() {
        String accountStr = "existingAccount";

        StepVerifier.create(accountService.addAccount(accountStr))
            .consumeNextWith(acc -> {
                StepVerifier.create(walletService.getDefaultWallet(acc))
                    .consumeNextWith(wallet -> {
                        assertEquals(accountStr, wallet.owner());
                    })
                    .verifyComplete();
                StepVerifier.create(accountService.addAccount(accountStr))
                    .consumeErrorWith(error -> {
                        assertTrue(error.getMessage().contains("already exists"));
                        assertInstanceOf(IllegalArgumentException.class, error);
                    })
                    .verify();
            });
    }
}
