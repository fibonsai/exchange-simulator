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

package com.fibonsai.exsim.dto.asset;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonDeserialize(builder = AssetPlatform.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssetPlatform(
        String blockchain,
        BigDecimal blockchainAssetId,
        String tokenStandard,
        String explorerUrl,
        String smartContractAddress,
        Long launchDate,
        String tradingAs,
        BigDecimal decimals,
        Boolean isInherited
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        @JsonProperty("BLOCKCHAIN")
        private String blockchain = "";

        @JsonProperty("BLOCKCHAIN_ASSET_ID")
        private BigDecimal blockchainAssetId = BigDecimal.ZERO;

        @JsonProperty("TOKEN_STANDARD")
        private String tokenStandard = "";

        @JsonProperty("EXPLORER_URL")
        private String explorerUrl = "";

        @JsonProperty("SMART_CONTRACT_ADDRESS")
        private String smartContractAddress = "";

        @JsonProperty("LAUNCH_DATE")
        private Long launchDate = 0L;

        @JsonProperty("TRADING_AS")
        private String tradingAs = "";

        @JsonProperty("DECIMALS")
        private BigDecimal decimals = BigDecimal.ZERO;

        @JsonProperty("IS_INHERITED")
        private Boolean isInherited = false;

        Builder() {
        }

        public Builder blockchain(String blockchain) {
            this.blockchain = blockchain;
            return this;
        }

        public Builder blockchainAssetId(BigDecimal blockchainAssetId) {
            this.blockchainAssetId = blockchainAssetId;
            return this;
        }

        public Builder tokenStandard(String tokenStandard) {
            this.tokenStandard = tokenStandard;
            return this;
        }

        public Builder explorerUrl(String explorerUrl) {
            this.explorerUrl = explorerUrl;
            return this;
        }

        public Builder smartContractAddress(String smartContractAddress) {
            this.smartContractAddress = smartContractAddress;
            return this;
        }

        public Builder launchDate(long launchDate) {
            this.launchDate = launchDate;
            return this;
        }

        public Builder tradingAs(String tradingAs) {
            this.tradingAs = tradingAs;
            return this;
        }

        public Builder decimals(BigDecimal decimals) {
            this.decimals = decimals;
            return this;
        }

        public Builder isInherited(boolean isInherited) {
            this.isInherited = isInherited;
            return this;
        }

        public AssetPlatform build() {
            return new AssetPlatform(this.blockchain, this.blockchainAssetId, this.tokenStandard, this.explorerUrl, this.smartContractAddress, this.launchDate, this.tradingAs, this.decimals, this.isInherited);
        }

        public String toString() {
            return "AssetPlatform.Builder(blockchain=" + this.blockchain + ", blockchainAssetId=" + this.blockchainAssetId + ", tokenStandard=" + this.tokenStandard + ", explorerUrl=" + this.explorerUrl + ", smartContractAddress=" + this.smartContractAddress + ", launchDate=" + this.launchDate + ", tradingAs=" + this.tradingAs + ", decimals=" + this.decimals + ", isInherited=" + this.isInherited + ")";
        }
    }
}
