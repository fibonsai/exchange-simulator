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
import com.fibonsai.exsim.util.ResourcesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("SpellCheckingInspection")
@Slf4j
@Service
public class ExchangeHubService extends AbstractService {

    @Value("${exsim.exchanges_data}")
    String exchangesData;

    @Value("${exsim.exchanges_pairs_data}")
    String exchangesPairsData;

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

            log.info("exchange hub started");
            return Instant.now();
        });
    }

    public void loadExchangesFromFile() {
        log.info("Loading exchanges from {}", exchangesData);
        try (InputStream in = ResourcesUtils.getResourceAsStream(Exchange.class, exchangesData)) {
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

        log.info("Loading exchanges pairs from {}", exchangesPairsData);
        try (InputStream in = ResourcesUtils.getResourceAsStream(Exchange.class, exchangesPairsData)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            JsonNode jsonNode = mapper.readTree(bufferedInputStream);
            JsonNode jsonNodeAssets = Optional.ofNullable(jsonNode.get("exchanges")).orElseThrow();
            final List<String> assetsNotFound = new ArrayList<>();
            final List<String> exchangesWithoutPairs = new ArrayList<>();
            jsonNodeAssets.forEachEntry((exchangeName, jsonWithAssets) -> {
                String exchangeSimpleName = exchangeName.toLowerCase();
                Exchange exchange = exchanges.get(exchangeSimpleName);
                if (exchange != null) {
                    final Set<AssetPair> exchangePairs = exchange.assetPairs();
                    jsonWithAssets.forEachEntry((base, quotes) -> {
                        if (quotes.isArray()) {
                            quotes.iterator().forEachRemaining(quote -> {
                                Asset assetBase = assetService.assets().get(base);
                                if (assetBase != null) {
                                    Asset assetQuote = assetService.assets().get(quote.asText());
                                    if (assetQuote != null) {
                                        exchangePairs.add(AssetPair.builder().baseAsset(assetBase).quoteAsset(assetQuote).build());
                                    }
                                } else {
                                    assetsNotFound.add(base);
                                }
                            });
                        }
                    });
                    if (exchange.assetPairs().isEmpty()) {
                        exchangesWithoutPairs.add(exchangeSimpleName);
                    } else {
                        log.info("Exchange {} have {} asset pairs supported", exchange.name(), exchange.assetPairs().size());
                    }
                } else {
                    exchangesWithoutPairs.add(exchangeSimpleName);
                }
            });
            if (!exchangesWithoutPairs.isEmpty()) {
                removeExchangeWithoutPairs(exchangesWithoutPairs);
            }
            log.info("{} exchanges loaded", exchanges.size());
            if (!assetsNotFound.isEmpty()) {
                log.warn("The following assets are not registered in AssetService and will be ignored: {}",
                        assetsNotFound.stream().sorted().distinct().toList());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void removeExchangeWithoutPairs(List<String> exchangesWithoutPairs) {
        log.warn("The following exchanges don't have asset pairs registered in AssetService and will be removed: {}", exchangesWithoutPairs);
        exchangesWithoutPairs.forEach(exchanges::remove);
    }
}
