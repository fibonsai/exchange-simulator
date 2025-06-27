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
import com.fibonsai.exsim.util.AssetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Slf4j
public record AssetPair(
        Asset baseAsset,
        Asset quoteAsset,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal priceScale,
        BigDecimal tradingFee
) implements Serializable, Comparable<AssetPair> {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    private static final String DEFAULT_SEPARATOR = "/";

    public static final class Builder {
        private Asset baseAsset = AssetUtil.DEFAULT_BASE;
        private Asset quoteAsset = AssetUtil.DEFAULT_QUOTE;
        private BigDecimal minAmount = BigDecimal.ZERO;
        private BigDecimal maxAmount = BigDecimal.valueOf(Double.MAX_VALUE);
        private BigDecimal priceScale = BigDecimal.valueOf(8L);
        private BigDecimal tradingFee = BigDecimal.ZERO;

        public Builder baseAsset(@NonNull Asset baseAsset) {
            this.baseAsset = baseAsset;
            return this;
        }

        public Builder quoteAsset(@NonNull Asset quoteAsset) {
            this.quoteAsset = quoteAsset;
            return this;
        }

        public Builder minAmount(@Nullable BigDecimal minAmount) {
            if (minAmount != null) {
                this.minAmount = minAmount;
            }
            return this;
        }

        public Builder maxAmount(@Nullable BigDecimal maxAmount) {
            if (maxAmount != null) {
                this.maxAmount = maxAmount;
            }
            return this;
        }

        public Builder priceScale(@NonNull BigDecimal priceScale) {
            this.priceScale = priceScale;
            return this;
        }

        public Builder tradingFee(@Nullable BigDecimal tradingFee) {
            if (tradingFee != null) {
                this.tradingFee = tradingFee;
            }
            return this;
        }

        public AssetPair build() {
            return new AssetPair(baseAsset, quoteAsset, minAmount, maxAmount, priceScale, tradingFee);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "AssetPair{" + simpleName() +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", priceScale=" + priceScale +
                ", tradingFee=" + tradingFee +
                '}';
    }

    @JsonIgnore
    public String simpleName() {
        return baseAsset.symbol() + DEFAULT_SEPARATOR + quoteAsset.symbol();
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
