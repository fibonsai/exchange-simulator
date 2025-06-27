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
import com.fibonsai.exsim.dto.AssetPair;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@JsonDeserialize(builder = Exchange.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Exchange(
        Long id,
        String name,
        String url,
        List<ItemType> itemType,
        CentralizationType centralizationType,
        String internalName,
        BigDecimal gradePoints,
        GradePointsSplit gradePointsSplit,
        String affiliateUrl,
        String country,
        Boolean orderBook,
        Boolean trades,
        String description,
        String fullAddress,
        String fees,
        String depositMethods,
        String withdrawalMethods,
        Integer sortOrder,
        Set<AssetPair> assetPairs
) implements Serializable, Comparable<Exchange> {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    @Override
    public String toString() {
        return "Exchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", internalName='" + internalName + '\'' +
                ", url='" + url + '\'' +
                ", itemType=" + itemType +
                ", centralizationType=" + centralizationType +
                ", gradePoints=" + gradePoints +
                ", gradePointsSplit=" + gradePointsSplit +
                ", affiliateUrl='" + affiliateUrl + '\'' +
                ", country='" + country + '\'' +
                ", orderBook=" + orderBook +
                ", trades=" + trades +
                ", description='" + description + '\'' +
                ", fullAddress='" + fullAddress + '\'' +
                ", fees='" + fees + '\'' +
                ", depositMethods='" + depositMethods + '\'' +
                ", withdrawalMethods='" + withdrawalMethods + '\'' +
                ", sortOrder=" + sortOrder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Exchange exchange)) return false;

        return name().equals(exchange.name()) && internalName().equals(exchange.internalName());
    }

    @Override
    public int hashCode() {
        int result = name().hashCode();
        result = 31 * result + internalName().hashCode();
        return result;
    }

    @Override
    public int compareTo(Exchange exchange) {
        if (equals(exchange)) return 0;
        return sortOrder().compareTo(exchange.sortOrder());
    }

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        @JsonProperty("Id")
        private Long id;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Url")
        private String url;

        @JsonProperty("ItemType")
        private ArrayList<ItemType> itemType;

        @JsonProperty("CentralizationType")
        private CentralizationType centralizationType;

        @JsonProperty("InternalName")
        private String internalName;

        @JsonProperty("GradePoints")
        private BigDecimal gradePoints;

        @JsonProperty("GradePointsSplit")
        private GradePointsSplit gradePointsSplit;

        @JsonProperty("AffiliateURL")
        private String affiliateUrl;

        @JsonProperty("Country")
        private String country;

        @JsonProperty("OrderBook")
        private Boolean orderBook;

        @JsonProperty("Trades")
        private Boolean trades;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("FullAddress")
        private String fullAddress;

        @JsonProperty("Fees")
        private String fees;

        @JsonProperty("DepositMethods")
        private String depositMethods;

        @JsonProperty("WithdrawalMethods")
        private String withdrawalMethods;

        @JsonProperty("SortOrder")
        private Integer sortOrder;

        Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder itemType(ItemType itemType) {
            if (this.itemType == null) this.itemType = new ArrayList<>();
            this.itemType.add(itemType);
            return this;
        }

        public Builder itemType(Collection<? extends ItemType> itemType) {
            if (itemType == null) return this;
            if (this.itemType == null) this.itemType = new ArrayList<>();
            this.itemType.addAll(itemType);
            return this;
        }

        public Builder clearItemType() {
            if (this.itemType != null)
                this.itemType.clear();
            return this;
        }

        public Builder centralizationType(CentralizationType centralizationType) {
            this.centralizationType = centralizationType;
            return this;
        }

        public Builder internalName(String internalName) {
            this.internalName = internalName;
            return this;
        }

        public Builder gradePoints(BigDecimal gradePoints) {
            this.gradePoints = gradePoints;
            return this;
        }

        public Builder gradePointsSplit(GradePointsSplit gradePointsSplit) {
            this.gradePointsSplit = gradePointsSplit;
            return this;
        }

        public Builder affiliateUrl(String affiliateUrl) {
            this.affiliateUrl = affiliateUrl;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder orderBook(Boolean orderBook) {
            this.orderBook = orderBook;
            return this;
        }

        public Builder trades(Boolean trades) {
            this.trades = trades;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder fullAddress(String fullAddress) {
            this.fullAddress = fullAddress;
            return this;
        }

        public Builder fees(String fees) {
            this.fees = fees;
            return this;
        }

        public Builder depositMethods(String depositMethods) {
            this.depositMethods = depositMethods;
            return this;
        }

        public Builder withdrawalMethods(String withdrawalMethods) {
            this.withdrawalMethods = withdrawalMethods;
            return this;
        }

        public Builder sortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Exchange build() {
            List<ItemType> itemType = switch (this.itemType == null ? 0 : this.itemType.size()) {
                case 0 -> java.util.Collections.emptyList();
                case 1 -> java.util.Collections.singletonList(this.itemType.getFirst());
                default -> java.util.Collections.unmodifiableList(new ArrayList<>(this.itemType));
            };

            return new Exchange(this.id, this.name, this.url, itemType, this.centralizationType, this.internalName, this.gradePoints, this.gradePointsSplit, this.affiliateUrl, this.country, this.orderBook, this.trades, this.description, this.fullAddress, this.fees, this.depositMethods, this.withdrawalMethods, this.sortOrder, new HashSet<>());
        }
    }
}
