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

package com.fibonsai.exsim.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.types.Mutable;
import com.fibonsai.exsim.util.AssetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public record AssetPair(
        Asset baseAsset,
        Asset quoteAsset,
        @Mutable Map<String, BigDecimal> assetSpecs
) implements Serializable, Comparable<AssetPair> {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    private static final Map<String, BigDecimal> DEFAULT_ASSET_SPECS = new HashMap<>();
    private static final String MIN_AMOUNT_PROP = "minAmount";
    private static final String MAX_AMOUNT_PROP = "maxAmount";
    private static final String PRICE_SCALE_PROP = "priceScale";
    private static final String TRADING_FEE_PROP = "tradingFee";

    static {
        DEFAULT_ASSET_SPECS.put(MIN_AMOUNT_PROP, BigDecimal.ZERO);
        DEFAULT_ASSET_SPECS.put(MAX_AMOUNT_PROP, BigDecimal.valueOf(Double.MAX_VALUE));
        DEFAULT_ASSET_SPECS.put(PRICE_SCALE_PROP, BigDecimal.valueOf(8L));
        DEFAULT_ASSET_SPECS.put(TRADING_FEE_PROP, BigDecimal.ZERO);
    }

    public AssetPair(Asset baseAsset, Asset quoteAsset) {
        this(baseAsset, quoteAsset, DEFAULT_ASSET_SPECS);
    }

    public static final class Builder {
        private Asset baseAsset = AssetUtil.DEFAULT_BASE;
        private Asset quoteAsset = AssetUtil.DEFAULT_QUOTE;
        private final Map<String, BigDecimal> assetSpecs = new HashMap<>();

        public Builder baseAsset(@NonNull Asset baseAsset) {
            this.baseAsset = baseAsset;
            return this;
        }

        public Builder quoteAsset(@NonNull Asset quoteAsset) {
            this.quoteAsset = quoteAsset;
            return this;
        }

        public Builder assetSpecs(@Nullable Map<String, BigDecimal> assetSpecs) {
            if (assetSpecs != null) {
                this.assetSpecs.putAll(assetSpecs);
            }
            return this;
        }

        public Builder minAmount(@Nullable BigDecimal minAmount) {
            if (minAmount != null) {
                this.assetSpecs.put(MIN_AMOUNT_PROP, minAmount);
            }
            return this;
        }

        public Builder maxAmount(@Nullable BigDecimal maxAmount) {
            if (maxAmount != null) {
                this.assetSpecs.put(MAX_AMOUNT_PROP, maxAmount);
            }
            return this;
        }

        public Builder priceScale(@Nullable BigDecimal priceScale) {
            if (priceScale != null) {
                this.assetSpecs.put(PRICE_SCALE_PROP, priceScale);
            }
            return this;
        }

        public Builder tradingFee(@Nullable BigDecimal tradingFee) {
            if (tradingFee != null) {
                this.assetSpecs.put(TRADING_FEE_PROP, tradingFee);
            }
            return this;
        }

        public AssetPair build() {
            return new AssetPair(baseAsset, quoteAsset, assetSpecs);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "AssetPair{" + simpleName() + ", assetSpecs=" + assetSpecs() + '}';
    }

    @JsonIgnore
    public String simpleName() {
        return baseAsset.symbol() + AssetUtil.DEFAULT_SEPARATOR + quoteAsset.symbol();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AssetPair assetPair)) return false;

        return baseAsset().equals(assetPair.baseAsset()) && quoteAsset().equals(assetPair.quoteAsset());
    }

    @Override
    public int hashCode() {
        int result = baseAsset().hashCode();
        result = 31 * result + quoteAsset().hashCode();
        return result;
    }

    @Override
    public int compareTo(AssetPair assetPair) {
        int baseComparator = baseAsset().compareTo(assetPair.baseAsset());
        if (baseComparator == 0) {
            return quoteAsset().compareTo(assetPair.quoteAsset());
        }
        return baseComparator;
    }
}
