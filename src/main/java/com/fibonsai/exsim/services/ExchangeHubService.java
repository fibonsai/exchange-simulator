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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fibonsai.exsim.dto.AssetPair;
import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.dto.exchange.Exchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ExchangeHubService extends AbstractService {

    private final AccountService accountService;
    private final MarketDataService marketDataService;
    private final TradeService tradeService;
    private final AssetService assetService;
    private final ObjectMapper mapper;

    private final Map<String, Exchange> exchanges = new ConcurrentHashMap<>();

    public ExchangeHubService(AccountService accountService,
                              MarketDataService marketDataService,
                              TradeService tradeService,
                              AssetService assetService,
                              ObjectMapper mapper
    ) {
        super();
        this.accountService = accountService;
        this.marketDataService = marketDataService;
        this.tradeService = tradeService;
        this.assetService = assetService;
        this.mapper = mapper;
    }

    public Mono<Instant> start() {
        return Mono.fromCallable(() -> {
            marketDataService.prepareAssets();
            loadExchangesFromFile();
            marketDataService.start();
            tradeService.start();
            accountService.start();

            log.info("exchange started");
            return Instant.now();
        });
    }

    public void loadExchangesFromFile() {
        try (InputStream in = Asset.class.getClassLoader().getResourceAsStream("exchanges.json")) {
            if (in == null) {
                throw new RuntimeException("exchanges.json not found");
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            JsonNode jsonNode = mapper.readTree(bufferedInputStream);
            JsonNode jsonNodeExchanges = Optional.ofNullable(jsonNode.get("Data")).orElseThrow();
            jsonNodeExchanges.forEachEntry((k, exchange) -> {
                exchanges.computeIfAbsent(exchange.get("InternalName").asText().toLowerCase(), name ->
                        mapper.convertValue(exchange, Exchange.class));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (InputStream in = Asset.class.getClassLoader().getResourceAsStream("pairs_by_exchange.json")) {
            if (in == null) {
                throw new RuntimeException("pairs_by_exchange.json not found");
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            JsonNode jsonNode = mapper.readTree(bufferedInputStream);
            JsonNode jsonNodeAssets = Optional.ofNullable(jsonNode.get("exchanges")).orElseThrow();
            jsonNodeAssets.forEachEntry((exchangeName, jsonWithAssets) -> {
                Exchange exchange = exchanges.get(exchangeName.toLowerCase());
                if (exchange != null) {
                    Set<AssetPair> exchangePairs = exchange.assetPairs();
                    jsonWithAssets.forEachEntry((base, quotes) -> {
                        if (quotes.isArray()) {
                            quotes.iterator().forEachRemaining(quote -> {
                                Asset assetBase = assetService.assets().get(base);
                                if (assetBase != null) {
                                    Asset assetQuote = assetService.assets().get(quote.asText());
                                    if (assetQuote != null) {
                                        exchangePairs.add(AssetPair.builder().setBaseAsset(assetBase).setQuoteAsset(assetQuote).build());
                                    }
                                }
                            });
                        }
                    });
                    if (exchange.assetPairs().isEmpty()) {
                        log.warn("Exchange {} don't have asset pairs. Removing.", exchangeName.toLowerCase());
                        exchanges.remove(exchangeName.toLowerCase());
                    } else {
                        log.info("Exchange {} have {} asset pairs", exchange.name(), exchange.assetPairs().size());
                    }
                } else {
                    log.warn("Exchange {} don't have asset pairs. Removing", exchangeName.toLowerCase());
                    exchanges.remove(exchangeName.toLowerCase());
                }
            });
            log.info("{} exchanges loaded", exchanges.size());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
