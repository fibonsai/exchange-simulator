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

import java.time.Instant;

@Slf4j
@Service
public class ExchangeService extends AbstractService {

    private final AccountService accountService;
    private final MarketDataService marketDataService;
    private final TradeService tradeService;

    public ExchangeService(AccountService accountService, MarketDataService marketDataService, TradeService tradeService) {
        super();
        this.accountService = accountService;
        this.marketDataService = marketDataService;
        this.tradeService = tradeService;
    }

    public Mono<Instant> start() {
        return Mono.fromCallable(() -> {
            marketDataService.prepareAssets();

            log.info("exchange started");
            return Instant.now();
        });
    }
}
