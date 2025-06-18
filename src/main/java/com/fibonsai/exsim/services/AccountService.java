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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class AccountService extends AbstractService {

    private final WalletService walletService;

    public AccountService(WalletService walletService) {
        super();
        this.walletService = walletService;
    }

    public Mono<String> addAccount(String accountStr) {
        log.info("Add account {}", accountStr);
        try {
            walletService.createDefaultWallet(accountStr);
            return Mono.just(accountStr);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Mono.empty();
        }
    }
}
