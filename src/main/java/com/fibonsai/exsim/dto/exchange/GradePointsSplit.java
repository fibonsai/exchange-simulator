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

package com.fibonsai.exsim.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


@JsonDeserialize(builder = GradePointsSplit.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GradePointsSplit(
        BigDecimal legal,
        BigDecimal kycAndTransactionRisk,
        BigDecimal team,
        BigDecimal dataProvision,
        BigDecimal assetQualityAndDiversity,
        BigDecimal marketQuality,
        BigDecimal security,
        BigDecimal negativeReportsPenalty
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    @Override
    public String toString() {
        return "GradePointsSplit{" +
                "legal=" + legal +
                ", kycAndTransactionRisk=" + kycAndTransactionRisk +
                ", team=" + team +
                ", dataProvision=" + dataProvision +
                ", assetQualityAndDiversity=" + assetQualityAndDiversity +
                ", marketQuality=" + marketQuality +
                ", security=" + security +
                ", negativeReportsPenalty=" + negativeReportsPenalty +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        @JsonProperty("Legal")
        private BigDecimal legal;

        @JsonProperty("KYCAndTransactionRisk")
        private BigDecimal kycAndTransactionRisk;

        @JsonProperty("Team")
        private BigDecimal team;

        @JsonProperty("DataProvision")
        private BigDecimal dataProvision;

        @JsonProperty("AssetQualityAndDiversity")
        private BigDecimal assetQualityAndDiversity;

        @JsonProperty("MarketQuality")
        private BigDecimal marketQuality;

        @JsonProperty("Security")
        private BigDecimal security;

        @JsonProperty("NegativeReportsPenalty")
        private BigDecimal negativeReportsPenalty;

        Builder() {
        }

        public Builder legal(BigDecimal legal) {
            this.legal = legal;
            return this;
        }

        public Builder kycAndTransactionRisk(BigDecimal kycAndTransactionRisk) {
            this.kycAndTransactionRisk = kycAndTransactionRisk;
            return this;
        }

        public Builder team(BigDecimal team) {
            this.team = team;
            return this;
        }

        public Builder dataProvision(BigDecimal dataProvision) {
            this.dataProvision = dataProvision;
            return this;
        }

        public Builder assetQualityAndDiversity(BigDecimal assetQualityAndDiversity) {
            this.assetQualityAndDiversity = assetQualityAndDiversity;
            return this;
        }

        public Builder marketQuality(BigDecimal marketQuality) {
            this.marketQuality = marketQuality;
            return this;
        }

        public Builder security(BigDecimal security) {
            this.security = security;
            return this;
        }

        public Builder negativeReportsPenalty(BigDecimal negativeReportsPenalty) {
            this.negativeReportsPenalty = negativeReportsPenalty;
            return this;
        }

        public GradePointsSplit build() {
            return new GradePointsSplit(this.legal, this.kycAndTransactionRisk, this.team, this.dataProvision, this.assetQualityAndDiversity, this.marketQuality, this.security, this.negativeReportsPenalty);
        }
    }
}
