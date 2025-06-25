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
import com.fibonsai.exsim.dto.CurrencyPair;
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

    public AssetService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private final Map<String, Asset> assets = new ConcurrentHashMap<>();

    public void add(Currency currency) {
        assets.computeIfAbsent(currency.getCurrencyCode(), name -> AssetUtil.fromCurrency(currency));
    }

    public void add(Asset asset) {
        assets.computeIfAbsent(asset.name(), name -> asset);
    }

    public void loadFiatAssets() {
        Currency.getAvailableCurrencies().forEach(this::add);
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

    public CurrencyPair convertPairFromString(String currencyPair) {
        String[] currencies = currencyPair.split(AssetUtil.DEFAULT_SEPARATOR);
        if (currencies.length != 2 && currencyPair.length() != 6 && currencyPair.length() != 3) {
            throw new IllegalArgumentException(currencyPair);
        }

        Asset baseAsset = AssetUtil.DEFAULT_BASE;
        Asset quoteAsset = AssetUtil.DEFAULT_QUOTE;
        if (currencies.length == 2) {
            try {
                Currency baseCurrencyInstance = Currency.getInstance(currencies[0]);
                baseAsset = AssetUtil.fromCurrency(baseCurrencyInstance);
            } catch (IllegalArgumentException | NullPointerException e) {
                baseAsset = Asset.builder().name(currencies[0]).build();
            }
            try {
                Currency quoteCurrencyInstance = Currency.getInstance(currencies[1]);
                quoteAsset = AssetUtil.fromCurrency(quoteCurrencyInstance);
            } catch (IllegalArgumentException | NullPointerException e) {
                quoteAsset = Asset.builder().name(currencies[1]).build();
            }
        }
        if (currencyPair.length() == 6) {
            try {
                Currency baseCurrencyInstance = Currency.getInstance(currencyPair.substring(0, 2));
                baseAsset = AssetUtil.fromCurrency(baseCurrencyInstance);
            } catch (IllegalArgumentException | NullPointerException e) {
                baseAsset = Asset.builder().name(currencyPair.substring(0, 2)).build();
            }
            try {
                Currency quoteCurrencyInstance = Currency.getInstance(currencyPair.substring(3, 5));
                quoteAsset = AssetUtil.fromCurrency(quoteCurrencyInstance);
            } catch (IllegalArgumentException | NullPointerException e) {
                quoteAsset = Asset.builder().name(currencyPair.substring(3, 5)).build();
            }
        }
        if (currencyPair.length() == 3) {
            try {
                Currency baseCurrencyInstance = Currency.getInstance(currencyPair);
                baseAsset = AssetUtil.fromCurrency(Currency.getInstance(currencyPair));
            } catch (IllegalArgumentException | NullPointerException e) {
                baseAsset = Asset.builder().name(currencyPair).build();
            }
        }
        return CurrencyPair.builder().setBaseAsset(baseAsset).setQuoteAsset(quoteAsset).build();
    }

}
