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

import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.util.AssetUtil;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
public record AssetPair(
        Asset baseAsset,
        Asset quoteAsset,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        BigDecimal priceScale,
        BigDecimal tradingFee
) implements Comparable<AssetPair> {

    private static final String DEFAULT_SEPARATOR = "/";

    public AssetPair(Asset baseAsset, Asset quoteAsset, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal priceScale, BigDecimal tradingFee) {
        this.baseAsset = Objects.requireNonNull(baseAsset);
        this.quoteAsset = Objects.requireNonNullElse(quoteAsset, AssetUtil.DEFAULT_QUOTE);
        this.minAmount = Objects.requireNonNullElse(minAmount, BigDecimal.ZERO);
        this.maxAmount = Objects.requireNonNullElse(maxAmount, BigDecimal.valueOf(Double.MAX_VALUE));
        this.priceScale = Objects.requireNonNullElse(priceScale, BigDecimal.valueOf(8L));
        this.tradingFee = Objects.requireNonNullElse(tradingFee, BigDecimal.ZERO);
    }

    public final static class Builder {
        private Asset baseAsset = AssetUtil.DEFAULT_BASE;
        private Asset quoteAsset = AssetUtil.DEFAULT_QUOTE;
        private BigDecimal minAmount = BigDecimal.ZERO;
        private BigDecimal maxAmount = BigDecimal.valueOf(Double.MAX_VALUE);
        private BigDecimal priceScale = BigDecimal.valueOf(8L);
        private BigDecimal tradingFee = BigDecimal.ZERO;

        public Builder setBaseAsset(Asset baseAsset) {
            this.baseAsset = Objects.requireNonNull(baseAsset);
            return this;
        }

        public Builder setQuoteAsset(Asset quoteAsset) {
            this.quoteAsset = Objects.requireNonNull(quoteAsset);
            return this;
        }

        public Builder setMinAmount(BigDecimal minAmount) {
            this.minAmount = Objects.requireNonNull(minAmount);
            return this;
        }

        public Builder setMaxAmount(BigDecimal maxAmount) {
            this.maxAmount = Objects.requireNonNull(maxAmount);
            return this;
        }

        public Builder setPriceScale(BigDecimal priceScale) {
            this.priceScale = Objects.requireNonNull(priceScale);
            return this;
        }

        public Builder setTradingFee(BigDecimal tradingFee) {
            this.tradingFee = Objects.requireNonNull(tradingFee);
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
