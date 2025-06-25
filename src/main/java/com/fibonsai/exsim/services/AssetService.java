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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fibonsai.exsim.dto.Asset;
import com.fibonsai.exsim.util.AssetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AssetService {

    private final ObjectMapper mapper;

    public static final String DEFAULT_ASSET = "USD";

    public AssetService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Asset defaultAsset() {
        return Optional.ofNullable(assets().get(DEFAULT_ASSET))
                .orElseThrow(() -> new RuntimeException("AssetService not initialized"));
    }

    private final Map<String, Asset> assets = new ConcurrentHashMap<>();

    public void add(Currency currency) {
        assets.computeIfAbsent(currency.getCurrencyCode(), name -> AssetUtil.fromCurrency(currency));
    }

    public void add(Asset asset) {
        assets.computeIfAbsent(asset.symbol(), name -> asset);
    }

    public void loadFiatAssets() {
        Currency.getAvailableCurrencies().forEach(this::add);
        log.info("Loaded {} Currencies (FIAT)", assets().size());
    }

    public void loadFromFile() {
        try (InputStream in = Asset.class.getClassLoader().getResourceAsStream("top_assets_with_metadata.json")) {
            if (in == null) {
                throw new RuntimeException("top_assets_with_metadata.json not found");
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            JsonNode jsonNode = mapper.readTree(bufferedInputStream);
            JsonNode jsonNodeAssets = Optional.ofNullable(jsonNode.get("assets")).orElseThrow();
            jsonNodeAssets.forEachEntry((k, v) -> add(mapper.convertValue(v, Asset.class)));
            log.info("Loaded {} CryptoCurrencies", assets().size());

            log.info(assets().get("BTC").assetCustodians().toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void init() {
        loadFiatAssets();
        loadFromFile();
    }

    public Map<String, Asset> assets() {
        return Collections.unmodifiableMap(assets);
    }

}
