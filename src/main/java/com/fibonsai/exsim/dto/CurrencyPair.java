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

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

@Slf4j
public record CurrencyPair(Asset baseAsset, Asset quoteAsset, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal priceScale, BigDecimal tradingFee) {
    public static final String DEFAULT_SEPARATOR = "/";
    public static final Asset DEFAULT_BASE = Asset.NULL;
    public static final Asset DEFAULT_QUOTE = Asset.fromCurrency(Currency.getInstance("USD"));

    public CurrencyPair(Asset baseAsset, Asset quoteAsset, BigDecimal minAmount, BigDecimal maxAmount, BigDecimal priceScale, BigDecimal tradingFee) {
        this.baseAsset = Objects.requireNonNull(baseAsset);
        this.quoteAsset = Objects.requireNonNullElse(quoteAsset, DEFAULT_QUOTE);
        this.minAmount = Objects.requireNonNullElse(minAmount, BigDecimal.ZERO);
        this.maxAmount = Objects.requireNonNullElse(maxAmount, BigDecimal.valueOf(Double.MAX_VALUE));
        this.priceScale = Objects.requireNonNullElse(priceScale, BigDecimal.valueOf(8L));
        this.tradingFee = Objects.requireNonNullElse(tradingFee, BigDecimal.ZERO);
    }

    public final static class Builder {
        private Asset baseAsset = DEFAULT_BASE;
        private Asset quoteAsset = DEFAULT_QUOTE;
        private BigDecimal minAmount = BigDecimal.ZERO;
        private BigDecimal maxAmount = BigDecimal.valueOf(Double.MAX_VALUE);
        private BigDecimal priceScale = BigDecimal.valueOf(8L);
        private BigDecimal tradingFee = BigDecimal.ZERO;

        public Builder convertPairFromString(String currencyPair) {
            String[] currencies = currencyPair.split(DEFAULT_SEPARATOR);
            if (currencies.length == 2) {
                try {
                    Currency baseCurrencyInstance = Currency.getInstance(currencies[0]);
                    this.baseAsset = Asset.fromCurrency(baseCurrencyInstance);
                } catch (IllegalArgumentException | NullPointerException e) {
                    this.baseAsset = Asset.builder().name(currencies[0]).build();
                }
                try {
                    Currency quoteCurrencyInstance = Currency.getInstance(currencies[1]);
                    this.quoteAsset = Asset.fromCurrency(quoteCurrencyInstance);
                } catch (IllegalArgumentException | NullPointerException e) {
                    this.quoteAsset = Asset.builder().name(currencies[1]).build();
                }
                return this;
            }
            if (currencyPair.length() == 6) {
                try {
                    Currency baseCurrencyInstance = Currency.getInstance(currencyPair.substring(0, 2));
                    this.baseAsset = Asset.fromCurrency(baseCurrencyInstance);
                } catch (IllegalArgumentException | NullPointerException e) {
                    this.baseAsset = Asset.builder().name(currencyPair.substring(0, 2)).build();
                }
                try {
                    Currency quoteCurrencyInstance = Currency.getInstance(currencyPair.substring(3, 5));
                    this.quoteAsset = Asset.fromCurrency(quoteCurrencyInstance);
                } catch (IllegalArgumentException | NullPointerException e) {
                    this.quoteAsset = Asset.builder().name(currencyPair.substring(3, 5)).build();
                }
                return this;
            }
            if (currencyPair.length() == 3) {
                try {
                    Currency baseCurrencyInstance = Currency.getInstance(currencyPair);
                    this.baseAsset = Asset.fromCurrency(Currency.getInstance(currencyPair));
                } catch (IllegalArgumentException | NullPointerException e) {
                    this.baseAsset = Asset.builder().name(currencyPair).build();
                }
                return this;
            }
            throw new IllegalArgumentException(currencyPair);
        }

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

        public CurrencyPair build() {
            return new CurrencyPair(baseAsset, quoteAsset, minAmount, maxAmount, priceScale, tradingFee);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
