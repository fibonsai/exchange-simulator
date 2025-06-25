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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@JsonDeserialize(builder = Asset.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Asset(
        @JsonProperty("ID")
        String id,

        @JsonProperty("SYMBOL")
        String symbol,

        @JsonProperty("URI")
        String uri,

        @JsonProperty("ASSET_TYPE")
        AssetType assetType,

        @JsonProperty("ASSET_ISSUER_NAME")
        String assetIssuerName,

        @JsonProperty("PARENT_ASSET_SYMBOL")
        String parentAssetSymbol,

        @JsonProperty("CREATED_ON")
        Long createdOn,

        @JsonProperty("UPDATED_ON")
        Long updatedOn,

        @JsonProperty("PUBLIC_NOTICE")
        String publicNotice,

        @JsonProperty("NAME")
        String name,

        @JsonProperty("LAUNCH_DATE")
        Long launchDate,

        @JsonProperty("ASSET_ALTERNATIVE_IDS")
        Map<String, String> assetAlternativeIds,

        @JsonProperty("ASSET_DESCRIPTION_SNIPPET")
        String assetDescriptionSnippet,

        @JsonProperty("ASSET_DECIMAL_POINTS")
        BigDecimal assetDecimalPoints,

        @JsonProperty("SUPPORTED_PLATFORMS")
        List<AssetPlatform> supportedPlatforms,

        @JsonProperty("ASSET_CUSTODIANS")
        List<AssetCustodian> assetCustodians,

        @JsonProperty("ASSET_SECURITY_METRICS")
        List<Map<String, String>> assetSecurityMetrics,

        @JsonProperty("SUPPLY_MAX")
        BigDecimal supplyMax,

        @JsonProperty("SUPPLY_ISSUED")
        BigDecimal supplyIssued,

        @JsonProperty("SUPPLY_TOTAL")
        BigDecimal supplyTotal,

        @JsonProperty("SUPPLY_CIRCULATING")
        BigDecimal supplyCirculating,

        @JsonProperty("SUPPLY_FUTURE")
        BigDecimal supplyFuture,

        @JsonProperty("SUPPLY_LOCKED")
        BigDecimal supplyLocked,

        @JsonProperty("SUPPLY_BURNT")
        BigDecimal supplyBurnt,

        @JsonProperty("SUPPLY_STAKED")
        BigDecimal supplyStaked,

        @JsonProperty("BURN_ADDRESSES")
        List<Map<String, String>> burnAddresses,

        @JsonProperty("LOCKED_ADDRESSES")
        List<Map<String, String>> lockedAddresses,

        @JsonProperty("HAS_SMART_CONTRACT_CAPABILITIES")
        Boolean hasSmartContractCapabilities,

        @JsonProperty("SMART_CONTRACT_SUPPORT_TYPE")
        SmartContractSupportType smartContractSupportType,

        @JsonProperty("LAST_BLOCK_HASHES_PER_SECOND")
        BigDecimal lastBlockHashesPerSecond,

        @JsonProperty("LAST_BLOCK_DIFFICULTY")
        BigDecimal lastBlockDifficulty,

        @JsonProperty("SUPPORTED_STANDARDS")
        List<ProtocolStandard> supportedStandards,

        @JsonProperty("LAYER_TWO_SOLUTIONS")
        List<Map<String, Object>> layerTwoSolutions,

        @JsonProperty("PRIVACY_SOLUTIONS")
        List<Map<String, Object>> privacySolutions,

        @JsonProperty("CODE_REPOSITORIES")
        List<Map<String, Object>> codeRepositories,

        @JsonProperty("OTHER_SOCIAL_NETWORKS")
        List<Map<String, String>> otherSocialNetworks,

        @JsonProperty("HELD_TOKEN_SALE")
        Boolean heldTokenSale,

        @JsonProperty("HELD_EQUITY_SALE")
        Boolean heldEquitySale,

        @JsonProperty("WEBSITE_URL")
        String websiteUrl,

        @JsonProperty("BLOG_URL")
        String blogUrl,

        @JsonProperty("WHITE_PAPER_URL")
        String whitePaperUrl,

        @JsonProperty("OTHER_DOCUMENT_URLS")
        List<Map<String,String>> otherDocumentUrls,

        @JsonProperty("EXPLORER_ADDRESSES")
        List<Map<String, String>> explorerAddresses,

        @JsonProperty("RPC_OPERATORS")
        List<Map<String, String>> rpcOperators,

        @JsonProperty("ASSET_SYMBOL_GLYPH")
        String assetSymbolGlyph,

        @JsonProperty("ASSET_INDUSTRIES")
        List<Map<String, String>> assetIndustries,

        @JsonProperty("CONSENSUS_MECHANISMS")
        List<Map<String, String>> consensusMechanisms,

        @JsonProperty("CONSENSUS_ALGORITHM_TYPES")
        List<Map<String, String>> consensusAlgorithmTypes,

        @JsonProperty("HASHING_ALGORITHM_TYPES")
        List<Map<String, String>> hashingAlgorithmTypes,

        @JsonProperty("MKT_CAP_PENALTY")
        BigDecimal mktCapPenalty,

        @JsonProperty("CIRCULATING_MKT_CAP_USD")
        BigDecimal circulatingMktCapUsd,

        @JsonProperty("TOTAL_MKT_CAP_USD")
        BigDecimal totalMktCapUsd,

        @JsonProperty("ASSET_DESCRIPTION")
        String assetDescription,

        @JsonProperty("ASSET_DESCRIPTION_SUMMARY")
        String assetDescriptionSummary,

        @JsonProperty("PROJECT_LEADERS")
        List<Map<String, String>> projectLeaders,

        @JsonProperty("ASSOCIATED_CONTACT_DETAILS")
        List<Map<String, String>> associatedContactDetails,

        @JsonProperty("SEO_TITLE")
        String seoTitle,

        @JsonProperty("SEO_DESCRIPTION")
        String seoDescription

) implements Serializable, Comparable<Asset> {

    private static final long serialVersionUID = -7340731832345284129L;

    private static final Map<String, Asset> assets = new ConcurrentHashMap<>();

    public static final Asset NULL = Asset.builder().id("NULL").name("NULL").build();

    public static Asset fromCurrency(Currency currency) {
        return Asset.builder()
                .id(currency.getCurrencyCode())
                .name(currency.getCurrencyCode())
                .assetDescription(currency.getDisplayName())
                .assetSymbolGlyph(currency.getSymbol())
                .assetDecimalPoints(BigDecimal.valueOf(currency.getDefaultFractionDigits()))
                .symbol(currency.getSymbol())
                .assetType(AssetType.FIAT)
                .build();
    }

    public static void add(Currency currency) {
        assets.computeIfAbsent(currency.getCurrencyCode(), name -> Asset.fromCurrency(currency));
    }

    public static void add(Asset asset) {
        assets.computeIfAbsent(asset.name(), name -> asset);
    }

    public static void loadFiatAssets() {
        Currency.getAvailableCurrencies().forEach(Asset::add);
    }

    public static void loadFromFile() {
        throw new UnsupportedOperationException("WIP");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(Asset asset) {
        return name().compareTo(asset.name());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Asset asset && name().equals(asset.name());
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", uri='" + uri + '\'' +
                ", assetType=" + assetType +
                ", assetIssuerName='" + assetIssuerName + '\'' +
                ", parentAssetSymbol='" + parentAssetSymbol + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", publicNotice='" + publicNotice + '\'' +
                ", name='" + name + '\'' +
                ", launchDate=" + launchDate +
                ", assetAlternativeIds=" + assetAlternativeIds +
                ", assetDescriptionSnippet='" + assetDescriptionSnippet + '\'' +
                ", assetDecimalPoints=" + assetDecimalPoints +
                ", supportedPlatforms=" + supportedPlatforms +
                ", assetCustodians=" + assetCustodians +
                ", assetSecurityMetrics=" + assetSecurityMetrics +
                ", supplyMax=" + supplyMax +
                ", supplyIssued=" + supplyIssued +
                ", supplyTotal=" + supplyTotal +
                ", supplyCirculating=" + supplyCirculating +
                ", supplyFuture=" + supplyFuture +
                ", supplyLocked=" + supplyLocked +
                ", supplyBurnt=" + supplyBurnt +
                ", supplyStaked=" + supplyStaked +
                ", burnAddresses=" + burnAddresses +
                ", lockedAddresses=" + lockedAddresses +
                ", hasSmartContractCapabilities=" + hasSmartContractCapabilities +
                ", smartContractSupportType=" + smartContractSupportType +
                ", lastBlockHashesPerSecond=" + lastBlockHashesPerSecond +
                ", lastBlockDifficulty=" + lastBlockDifficulty +
                ", supportedStandards=" + supportedStandards +
                ", layerTwoSolutions=" + layerTwoSolutions +
                ", privacySolutions=" + privacySolutions +
                ", codeRepositories=" + codeRepositories +
                ", otherSocialNetworks=" + otherSocialNetworks +
                ", heldTokenSale=" + heldTokenSale +
                ", heldEquitySale=" + heldEquitySale +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", blogUrl='" + blogUrl + '\'' +
                ", whitePaperUrl='" + whitePaperUrl + '\'' +
                ", otherDocumentUrls=" + otherDocumentUrls +
                ", explorerAddresses=" + explorerAddresses +
                ", rpcOperators=" + rpcOperators +
                ", assetSymbolGlyph='" + assetSymbolGlyph + '\'' +
                ", assetIndustries=" + assetIndustries +
                ", consensusMechanisms=" + consensusMechanisms +
                ", consensusAlgorithmTypes=" + consensusAlgorithmTypes +
                ", hashingAlgorithmTypes=" + hashingAlgorithmTypes +
                ", mktCapPenalty=" + mktCapPenalty +
                ", circulatingMktCapUsd=" + circulatingMktCapUsd +
                ", totalMktCapUsd=" + totalMktCapUsd +
                ", assetDescription='" + assetDescription + '\'' +
                ", assetDescriptionSummary='" + assetDescriptionSummary + '\'' +
                ", projectLeaders=" + projectLeaders +
                ", associatedContactDetails=" + associatedContactDetails +
                ", seoTitle='" + seoTitle + '\'' +
                ", seoDescription='" + seoDescription + '\'' +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String id = UUID.randomUUID().toString();
        private String symbol = "";
        private String uri = "";
        private AssetType assetType = AssetType.UNDEF;
        private String assetIssuerName = "";
        private String parentAssetSymbol = "";
        private Long createdOn = 0L;
        private Long updatedOn = 0L;
        private String publicNotice = "";
        private String name = "";
        private Long launchDate = 0L;
        private ArrayList<String> assetAlternativeIds$key = new ArrayList<>();
        private ArrayList<String> assetAlternativeIds$value = new ArrayList<>();
        private String assetDescriptionSnippet = "";
        private BigDecimal assetDecimalPoints = BigDecimal.TWO;
        private ArrayList<AssetPlatform> supportedPlatforms = new ArrayList<>();
        private ArrayList<AssetCustodian> assetCustodians = new ArrayList<>();
        private ArrayList<Map<String, String>> assetSecurityMetrics = new ArrayList<>();
        private BigDecimal supplyMax = BigDecimal.ZERO;
        private BigDecimal supplyIssued = BigDecimal.ZERO;
        private BigDecimal supplyTotal = BigDecimal.ZERO;
        private BigDecimal supplyCirculating = BigDecimal.ZERO;
        private BigDecimal supplyFuture = BigDecimal.ZERO;
        private BigDecimal supplyLocked = BigDecimal.ZERO;
        private BigDecimal supplyBurnt = BigDecimal.ZERO;
        private BigDecimal supplyStaked = BigDecimal.ZERO;
        private ArrayList<Map<String, String>> burnAddresses = new ArrayList<>();
        private ArrayList<Map<String, String>> lockedAddresses = new ArrayList<>();
        private Boolean hasSmartContractCapabilities = false;
        private SmartContractSupportType smartContractSupportType = SmartContractSupportType.UNDEF;
        private BigDecimal lastBlockHashesPerSecond = BigDecimal.ZERO;
        private BigDecimal lastBlockDifficulty = BigDecimal.ZERO;
        private ArrayList<ProtocolStandard> supportedStandards = new ArrayList<>();
        private ArrayList<Map<String, Object>> layerTwoSolutions = new ArrayList<>();
        private ArrayList<Map<String, Object>> privacySolutions = new ArrayList<>();
        private ArrayList<Map<String, Object>> codeRepositories = new ArrayList<>();
        private ArrayList<Map<String, String>> otherSocialNetworks = new ArrayList<>();
        private Boolean heldTokenSale = false;
        private Boolean heldEquitySale = false;
        private String websiteUrl = "";
        private String blogUrl = "";
        private String whitePaperUrl = "";
        private List<Map<String, String>> otherDocumentUrls = new ArrayList<>();
        private ArrayList<Map<String, String>> explorerAddresses = new ArrayList<>();
        private ArrayList<Map<String, String>> rpcOperators = new ArrayList<>();
        private String assetSymbolGlyph = "";
        private ArrayList<Map<String, String>> assetIndustries = new ArrayList<>();
        private ArrayList<Map<String, String>> consensusMechanisms = new ArrayList<>();
        private ArrayList<Map<String, String>> consensusAlgorithmTypes = new ArrayList<>();
        private ArrayList<Map<String, String>> hashingAlgorithmTypes = new ArrayList<>();
        private BigDecimal mktCapPenalty = BigDecimal.ZERO;
        private BigDecimal circulatingMktCapUsd = BigDecimal.ZERO;
        private BigDecimal totalMktCapUsd = BigDecimal.ZERO;
        private String assetDescription = "";
        private String assetDescriptionSummary = "";
        private ArrayList<Map<String, String>> projectLeaders = new ArrayList<>();
        private ArrayList<Map<String, String>> associatedContactDetails = new ArrayList<>();
        private String seoTitle = "";
        private String seoDescription = "";

        Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public Builder assetType(AssetType assetType) {
            this.assetType = assetType;
            return this;
        }

        public Builder assetIssuerName(String assetIssuerName) {
            this.assetIssuerName = assetIssuerName;
            return this;
        }

        public Builder parentAssetSymbol(String parentAssetSymbol) {
            this.parentAssetSymbol = parentAssetSymbol;
            return this;
        }

        public Builder createdOn(Long createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder updatedOn(Long updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        public Builder publicNotice(String publicNotice) {
            this.publicNotice = publicNotice;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder launchDate(Long launchDate) {
            this.launchDate = launchDate;
            return this;
        }

        public Builder assetAlternativeId(String assetAlternativeIdKey, String assetAlternativeIdValue) {
            if (this.assetAlternativeIds$key == null) {
                this.assetAlternativeIds$key = new ArrayList<String>();
                this.assetAlternativeIds$value = new ArrayList<String>();
            }
            this.assetAlternativeIds$key.add(assetAlternativeIdKey);
            this.assetAlternativeIds$value.add(assetAlternativeIdValue);
            return this;
        }

        public Builder assetAlternativeIds(Map<? extends String, ? extends String> assetAlternativeIds) {
            if (assetAlternativeIds == null) {
                throw new NullPointerException("assetAlternativeIds cannot be null");
            }
            if (this.assetAlternativeIds$key == null) {
                this.assetAlternativeIds$key = new ArrayList<String>();
                this.assetAlternativeIds$value = new ArrayList<String>();
            }
            for (final Map.Entry<? extends String, ? extends String> $lombokEntry : assetAlternativeIds.entrySet()) {
                this.assetAlternativeIds$key.add($lombokEntry.getKey());
                this.assetAlternativeIds$value.add($lombokEntry.getValue());
            }
            return this;
        }

        public Builder clearAssetAlternativeIds() {
            if (this.assetAlternativeIds$key != null) {
                this.assetAlternativeIds$key.clear();
                this.assetAlternativeIds$value.clear();
            }
            return this;
        }

        public Builder assetDescriptionSnippet(String assetDescriptionSnippet) {
            this.assetDescriptionSnippet = assetDescriptionSnippet;
            return this;
        }

        public Builder assetDecimalPoints(BigDecimal assetDecimalPoints) {
            this.assetDecimalPoints = assetDecimalPoints;
            return this;
        }

        public Builder supportedPlatform(AssetPlatform supportedPlatform) {
            if (this.supportedPlatforms == null) this.supportedPlatforms = new ArrayList<AssetPlatform>();
            this.supportedPlatforms.add(supportedPlatform);
            return this;
        }

        public Builder supportedPlatforms(Collection<? extends AssetPlatform> supportedPlatforms) {
            if (supportedPlatforms == null) {
                throw new NullPointerException("supportedPlatforms cannot be null");
            }
            if (this.supportedPlatforms == null) this.supportedPlatforms = new ArrayList<AssetPlatform>();
            this.supportedPlatforms.addAll(supportedPlatforms);
            return this;
        }

        public Builder clearSupportedPlatforms() {
            if (this.supportedPlatforms != null)
                this.supportedPlatforms.clear();
            return this;
        }

        public Builder assetCustodian(AssetCustodian assetCustodian) {
            if (this.assetCustodians == null) this.assetCustodians = new ArrayList<AssetCustodian>();
            this.assetCustodians.add(assetCustodian);
            return this;
        }

        public Builder assetCustodians(Collection<? extends AssetCustodian> assetCustodians) {
            if (assetCustodians == null) {
                throw new NullPointerException("assetCustodians cannot be null");
            }
            if (this.assetCustodians == null) this.assetCustodians = new ArrayList<AssetCustodian>();
            this.assetCustodians.addAll(assetCustodians);
            return this;
        }

        public Builder clearAssetCustodians() {
            if (this.assetCustodians != null)
                this.assetCustodians.clear();
            return this;
        }

        public Builder assetSecurityMetric(Map<String, String> assetSecurityMetric) {
            if (this.assetSecurityMetrics == null) this.assetSecurityMetrics = new ArrayList<Map<String, String>>();
            this.assetSecurityMetrics.add(assetSecurityMetric);
            return this;
        }

        public Builder assetSecurityMetrics(Collection<? extends Map<String, String>> assetSecurityMetrics) {
            if (assetSecurityMetrics == null) {
                throw new NullPointerException("assetSecurityMetrics cannot be null");
            }
            if (this.assetSecurityMetrics == null) this.assetSecurityMetrics = new ArrayList<Map<String, String>>();
            this.assetSecurityMetrics.addAll(assetSecurityMetrics);
            return this;
        }

        public Builder clearAssetSecurityMetrics() {
            if (this.assetSecurityMetrics != null)
                this.assetSecurityMetrics.clear();
            return this;
        }

        public Builder supplyMax(BigDecimal supplyMax) {
            this.supplyMax = supplyMax;
            return this;
        }

        public Builder supplyIssued(BigDecimal supplyIssued) {
            this.supplyIssued = supplyIssued;
            return this;
        }

        public Builder supplyTotal(BigDecimal supplyTotal) {
            this.supplyTotal = supplyTotal;
            return this;
        }

        public Builder supplyCirculating(BigDecimal supplyCirculating) {
            this.supplyCirculating = supplyCirculating;
            return this;
        }

        public Builder supplyFuture(BigDecimal supplyFuture) {
            this.supplyFuture = supplyFuture;
            return this;
        }

        public Builder supplyLocked(BigDecimal supplyLocked) {
            this.supplyLocked = supplyLocked;
            return this;
        }

        public Builder supplyBurnt(BigDecimal supplyBurnt) {
            this.supplyBurnt = supplyBurnt;
            return this;
        }

        public Builder supplyStaked(BigDecimal supplyStaked) {
            this.supplyStaked = supplyStaked;
            return this;
        }

        public Builder burnAddress(Map<String, String> burnAddress) {
            if (this.burnAddresses == null) this.burnAddresses = new ArrayList<Map<String, String>>();
            this.burnAddresses.add(burnAddress);
            return this;
        }

        public Builder burnAddresses(Collection<? extends Map<String, String>> burnAddresses) {
            if (burnAddresses == null) {
                throw new NullPointerException("burnAddresses cannot be null");
            }
            if (this.burnAddresses == null) this.burnAddresses = new ArrayList<Map<String, String>>();
            this.burnAddresses.addAll(burnAddresses);
            return this;
        }

        public Builder clearBurnAddresses() {
            if (this.burnAddresses != null)
                this.burnAddresses.clear();
            return this;
        }

        public Builder lockedAddress(Map<String, String> lockedAddress) {
            if (this.lockedAddresses == null) this.lockedAddresses = new ArrayList<Map<String, String>>();
            this.lockedAddresses.add(lockedAddress);
            return this;
        }

        public Builder lockedAddresses(Collection<? extends Map<String, String>> lockedAddresses) {
            if (lockedAddresses == null) {
                throw new NullPointerException("lockedAddresses cannot be null");
            }
            if (this.lockedAddresses == null) this.lockedAddresses = new ArrayList<Map<String, String>>();
            this.lockedAddresses.addAll(lockedAddresses);
            return this;
        }

        public Builder clearLockedAddresses() {
            if (this.lockedAddresses != null)
                this.lockedAddresses.clear();
            return this;
        }

        public Builder hasSmartContractCapabilities(Boolean hasSmartContractCapabilities) {
            this.hasSmartContractCapabilities = hasSmartContractCapabilities;
            return this;
        }

        public Builder smartContractSupportType(SmartContractSupportType smartContractSupportType) {
            this.smartContractSupportType = smartContractSupportType;
            return this;
        }

        public Builder lastBlockHashesPerSecond(BigDecimal lastBlockHashesPerSecond) {
            this.lastBlockHashesPerSecond = lastBlockHashesPerSecond;
            return this;
        }

        public Builder lastBlockDifficulty(BigDecimal lastBlockDifficulty) {
            this.lastBlockDifficulty = lastBlockDifficulty;
            return this;
        }

        public Builder supportedStandard(ProtocolStandard supportedStandard) {
            if (this.supportedStandards == null) this.supportedStandards = new ArrayList<ProtocolStandard>();
            this.supportedStandards.add(supportedStandard);
            return this;
        }

        public Builder supportedStandards(Collection<? extends ProtocolStandard> supportedStandards) {
            if (supportedStandards == null) {
                throw new NullPointerException("supportedStandards cannot be null");
            }
            if (this.supportedStandards == null) this.supportedStandards = new ArrayList<ProtocolStandard>();
            this.supportedStandards.addAll(supportedStandards);
            return this;
        }

        public Builder clearSupportedStandards() {
            if (this.supportedStandards != null)
                this.supportedStandards.clear();
            return this;
        }

        public Builder layerTwoSolution(Map<String, Object> layerTwoSolution) {
            if (this.layerTwoSolutions == null) this.layerTwoSolutions = new ArrayList<Map<String, Object>>();
            this.layerTwoSolutions.add(layerTwoSolution);
            return this;
        }

        public Builder layerTwoSolutions(Collection<? extends Map<String, Object>> layerTwoSolutions) {
            if (layerTwoSolutions == null) {
                throw new NullPointerException("layerTwoSolutions cannot be null");
            }
            if (this.layerTwoSolutions == null) this.layerTwoSolutions = new ArrayList<Map<String, Object>>();
            this.layerTwoSolutions.addAll(layerTwoSolutions);
            return this;
        }

        public Builder clearLayerTwoSolutions() {
            if (this.layerTwoSolutions != null)
                this.layerTwoSolutions.clear();
            return this;
        }

        public Builder privacySolution(Map<String, Object> privacySolution) {
            if (this.privacySolutions == null) this.privacySolutions = new ArrayList<Map<String, Object>>();
            this.privacySolutions.add(privacySolution);
            return this;
        }

        public Builder privacySolutions(Collection<? extends Map<String, Object>> privacySolutions) {
            if (privacySolutions == null) {
                throw new NullPointerException("privacySolutions cannot be null");
            }
            if (this.privacySolutions == null) this.privacySolutions = new ArrayList<Map<String, Object>>();
            this.privacySolutions.addAll(privacySolutions);
            return this;
        }

        public Builder clearPrivacySolutions() {
            if (this.privacySolutions != null)
                this.privacySolutions.clear();
            return this;
        }

        public Builder codeRepository(Map<String, Object> codeRepository) {
            if (this.codeRepositories == null) this.codeRepositories = new ArrayList<Map<String, Object>>();
            this.codeRepositories.add(codeRepository);
            return this;
        }

        public Builder codeRepositories(Collection<? extends Map<String, Object>> codeRepositories) {
            if (codeRepositories == null) {
                throw new NullPointerException("codeRepositories cannot be null");
            }
            if (this.codeRepositories == null) this.codeRepositories = new ArrayList<Map<String, Object>>();
            this.codeRepositories.addAll(codeRepositories);
            return this;
        }

        public Builder clearCodeRepositories() {
            if (this.codeRepositories != null)
                this.codeRepositories.clear();
            return this;
        }

        public Builder otherSocialNetwork(Map<String, String> otherSocialNetwork) {
            if (this.otherSocialNetworks == null) this.otherSocialNetworks = new ArrayList<Map<String, String>>();
            this.otherSocialNetworks.add(otherSocialNetwork);
            return this;
        }

        public Builder otherSocialNetworks(Collection<? extends Map<String, String>> otherSocialNetworks) {
            if (otherSocialNetworks == null) {
                throw new NullPointerException("otherSocialNetworks cannot be null");
            }
            if (this.otherSocialNetworks == null) this.otherSocialNetworks = new ArrayList<Map<String, String>>();
            this.otherSocialNetworks.addAll(otherSocialNetworks);
            return this;
        }

        public Builder clearOtherSocialNetworks() {
            if (this.otherSocialNetworks != null)
                this.otherSocialNetworks.clear();
            return this;
        }

        public Builder heldTokenSale(Boolean heldTokenSale) {
            this.heldTokenSale = heldTokenSale;
            return this;
        }

        public Builder heldEquitySale(Boolean heldEquitySale) {
            this.heldEquitySale = heldEquitySale;
            return this;
        }

        public Builder websiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
            return this;
        }

        public Builder blogUrl(String blogUrl) {
            this.blogUrl = blogUrl;
            return this;
        }

        public Builder whitePaperUrl(String whitePaperUrl) {
            this.whitePaperUrl = whitePaperUrl;
            return this;
        }

        public Builder otherDocumentUrls(Map<String, String> explorerAddress) {
            if (this.otherDocumentUrls == null) this.otherDocumentUrls = new ArrayList<Map<String, String>>();
            this.otherDocumentUrls.add(explorerAddress);
            return this;
        }

        public Builder otherDocumentUrls(Collection<? extends Map<String, String>> otherDocumentUrls) {
            if (otherDocumentUrls == null) {
                throw new NullPointerException("otherDocumentUrls cannot be null");
            }
            if (this.otherDocumentUrls == null) this.otherDocumentUrls = new ArrayList<Map<String, String>>();
            this.otherDocumentUrls.addAll(otherDocumentUrls);
            return this;
        }

        public Builder clearOtherDocumentUrls() {
            if (this.otherDocumentUrls != null)
                this.otherDocumentUrls.clear();
            return this;
        }

        public Builder explorerAddress(Map<String, String> explorerAddress) {
            if (this.explorerAddresses == null) this.explorerAddresses = new ArrayList<Map<String, String>>();
            this.explorerAddresses.add(explorerAddress);
            return this;
        }

        public Builder explorerAddresses(Collection<? extends Map<String, String>> explorerAddresses) {
            if (explorerAddresses == null) {
                throw new NullPointerException("explorerAddresses cannot be null");
            }
            if (this.explorerAddresses == null) this.explorerAddresses = new ArrayList<Map<String, String>>();
            this.explorerAddresses.addAll(explorerAddresses);
            return this;
        }

        public Builder clearExplorerAddresses() {
            if (this.explorerAddresses != null)
                this.explorerAddresses.clear();
            return this;
        }

        public Builder rpcOperator(Map<String, String> rpcOperator) {
            if (this.rpcOperators == null) this.rpcOperators = new ArrayList<Map<String, String>>();
            this.rpcOperators.add(rpcOperator);
            return this;
        }

        public Builder rpcOperators(Collection<? extends Map<String, String>> rpcOperators) {
            if (rpcOperators == null) {
                throw new NullPointerException("rpcOperators cannot be null");
            }
            if (this.rpcOperators == null) this.rpcOperators = new ArrayList<Map<String, String>>();
            this.rpcOperators.addAll(rpcOperators);
            return this;
        }

        public Builder clearRpcOperators() {
            if (this.rpcOperators != null)
                this.rpcOperators.clear();
            return this;
        }

        public Builder assetSymbolGlyph(String assetSymbolGlyph) {
            this.assetSymbolGlyph = assetSymbolGlyph;
            return this;
        }

        public Builder assetIndustry(Map<String, String> assetIndustry) {
            if (this.assetIndustries == null) this.assetIndustries = new ArrayList<Map<String, String>>();
            this.assetIndustries.add(assetIndustry);
            return this;
        }

        public Builder assetIndustries(Collection<? extends Map<String, String>> assetIndustries) {
            if (assetIndustries == null) {
                throw new NullPointerException("assetIndustries cannot be null");
            }
            if (this.assetIndustries == null) this.assetIndustries = new ArrayList<Map<String, String>>();
            this.assetIndustries.addAll(assetIndustries);
            return this;
        }

        public Builder clearAssetIndustries() {
            if (this.assetIndustries != null)
                this.assetIndustries.clear();
            return this;
        }

        public Builder consensusMechanism(Map<String, String> consensusMechanism) {
            if (this.consensusMechanisms == null) this.consensusMechanisms = new ArrayList<Map<String, String>>();
            this.consensusMechanisms.add(consensusMechanism);
            return this;
        }

        public Builder consensusMechanisms(Collection<? extends Map<String, String>> consensusMechanisms) {
            if (consensusMechanisms == null) {
                throw new NullPointerException("consensusMechanisms cannot be null");
            }
            if (this.consensusMechanisms == null) this.consensusMechanisms = new ArrayList<Map<String, String>>();
            this.consensusMechanisms.addAll(consensusMechanisms);
            return this;
        }

        public Builder clearConsensusMechanisms() {
            if (this.consensusMechanisms != null)
                this.consensusMechanisms.clear();
            return this;
        }

        public Builder consensusAlgorithmType(Map<String, String> consensusAlgorithmType) {
            if (this.consensusAlgorithmTypes == null)
                this.consensusAlgorithmTypes = new ArrayList<Map<String, String>>();
            this.consensusAlgorithmTypes.add(consensusAlgorithmType);
            return this;
        }

        public Builder consensusAlgorithmTypes(Collection<? extends Map<String, String>> consensusAlgorithmTypes) {
            if (consensusAlgorithmTypes == null) {
                throw new NullPointerException("consensusAlgorithmTypes cannot be null");
            }
            if (this.consensusAlgorithmTypes == null)
                this.consensusAlgorithmTypes = new ArrayList<Map<String, String>>();
            this.consensusAlgorithmTypes.addAll(consensusAlgorithmTypes);
            return this;
        }

        public Builder clearConsensusAlgorithmTypes() {
            if (this.consensusAlgorithmTypes != null)
                this.consensusAlgorithmTypes.clear();
            return this;
        }

        public Builder hashingAlgorithmType(Map<String, String> hashingAlgorithmType) {
            if (this.hashingAlgorithmTypes == null) this.hashingAlgorithmTypes = new ArrayList<Map<String, String>>();
            this.hashingAlgorithmTypes.add(hashingAlgorithmType);
            return this;
        }

        public Builder hashingAlgorithmTypes(Collection<? extends Map<String, String>> hashingAlgorithmTypes) {
            if (hashingAlgorithmTypes == null) {
                throw new NullPointerException("hashingAlgorithmTypes cannot be null");
            }
            if (this.hashingAlgorithmTypes == null) this.hashingAlgorithmTypes = new ArrayList<Map<String, String>>();
            this.hashingAlgorithmTypes.addAll(hashingAlgorithmTypes);
            return this;
        }

        public Builder clearHashingAlgorithmTypes() {
            if (this.hashingAlgorithmTypes != null)
                this.hashingAlgorithmTypes.clear();
            return this;
        }

        public Builder mktCapPenalty(BigDecimal mktCapPenalty) {
            this.mktCapPenalty = mktCapPenalty;
            return this;
        }

        public Builder circulatingMktCapUsd(BigDecimal circulatingMktCapUsd) {
            this.circulatingMktCapUsd = circulatingMktCapUsd;
            return this;
        }

        public Builder totalMktCapUsd(BigDecimal totalMktCapUsd) {
            this.totalMktCapUsd = totalMktCapUsd;
            return this;
        }

        public Builder assetDescription(String assetDescription) {
            this.assetDescription = assetDescription;
            return this;
        }

        public Builder assetDescriptionSummary(String assetDescriptionSummary) {
            this.assetDescriptionSummary = assetDescriptionSummary;
            return this;
        }

        public Builder projectLeader(Map<String, String> projectLeader) {
            if (this.projectLeaders == null) this.projectLeaders = new ArrayList<Map<String, String>>();
            this.projectLeaders.add(projectLeader);
            return this;
        }

        public Builder projectLeaders(Collection<? extends Map<String, String>> projectLeaders) {
            if (projectLeaders == null) {
                throw new NullPointerException("projectLeaders cannot be null");
            }
            if (this.projectLeaders == null) this.projectLeaders = new ArrayList<Map<String, String>>();
            this.projectLeaders.addAll(projectLeaders);
            return this;
        }

        public Builder clearProjectLeaders() {
            if (this.projectLeaders != null)
                this.projectLeaders.clear();
            return this;
        }

        public Builder associatedContactDetail(Map<String, String> associatedContactDetail) {
            if (this.associatedContactDetails == null)
                this.associatedContactDetails = new ArrayList<Map<String, String>>();
            this.associatedContactDetails.add(associatedContactDetail);
            return this;
        }

        public Builder associatedContactDetails(Collection<? extends Map<String, String>> associatedContactDetails) {
            if (associatedContactDetails == null) {
                throw new NullPointerException("associatedContactDetails cannot be null");
            }
            if (this.associatedContactDetails == null)
                this.associatedContactDetails = new ArrayList<Map<String, String>>();
            this.associatedContactDetails.addAll(associatedContactDetails);
            return this;
        }

        public Builder clearAssociatedContactDetails() {
            if (this.associatedContactDetails != null)
                this.associatedContactDetails.clear();
            return this;
        }

        public Builder seoTitle(String seoTitle) {
            this.seoTitle = seoTitle;
            return this;
        }

        public Builder seoDescription(String seoDescription) {
            this.seoDescription = seoDescription;
            return this;
        }

        public Asset build() {
            Map<String, String> assetAlternativeIds;
            switch (this.assetAlternativeIds$key == null ? 0 : this.assetAlternativeIds$key.size()) {
                case 0:
                    assetAlternativeIds = Collections.emptyMap();
                    break;
                case 1:
                    assetAlternativeIds = Collections.singletonMap(this.assetAlternativeIds$key.get(0), this.assetAlternativeIds$value.get(0));
                    break;
                default:
                    assetAlternativeIds = new LinkedHashMap<String, String>(this.assetAlternativeIds$key.size() < 1073741824 ? 1 + this.assetAlternativeIds$key.size() + (this.assetAlternativeIds$key.size() - 3) / 3 : Integer.MAX_VALUE);
                    for (int $i = 0; $i < this.assetAlternativeIds$key.size(); $i++)
                        assetAlternativeIds.put(this.assetAlternativeIds$key.get($i), (String) this.assetAlternativeIds$value.get($i));
                    assetAlternativeIds = Collections.unmodifiableMap(assetAlternativeIds);
            }
            List<AssetPlatform> supportedPlatforms;
            switch (this.supportedPlatforms == null ? 0 : this.supportedPlatforms.size()) {
                case 0:
                    supportedPlatforms = Collections.emptyList();
                    break;
                case 1:
                    supportedPlatforms = Collections.singletonList(this.supportedPlatforms.get(0));
                    break;
                default:
                    supportedPlatforms = Collections.unmodifiableList(new ArrayList<AssetPlatform>(this.supportedPlatforms));
            }
            List<AssetCustodian> assetCustodians;
            switch (this.assetCustodians == null ? 0 : this.assetCustodians.size()) {
                case 0:
                    assetCustodians = Collections.emptyList();
                    break;
                case 1:
                    assetCustodians = Collections.singletonList(this.assetCustodians.get(0));
                    break;
                default:
                    assetCustodians = Collections.unmodifiableList(new ArrayList<AssetCustodian>(this.assetCustodians));
            }
            List<Map<String, String>> assetSecurityMetrics;
            switch (this.assetSecurityMetrics == null ? 0 : this.assetSecurityMetrics.size()) {
                case 0:
                    assetSecurityMetrics = Collections.emptyList();
                    break;
                case 1:
                    assetSecurityMetrics = Collections.singletonList(this.assetSecurityMetrics.get(0));
                    break;
                default:
                    assetSecurityMetrics = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.assetSecurityMetrics));
            }
            List<Map<String, String>> burnAddresses;
            switch (this.burnAddresses == null ? 0 : this.burnAddresses.size()) {
                case 0:
                    burnAddresses = Collections.emptyList();
                    break;
                case 1:
                    burnAddresses = Collections.singletonList(this.burnAddresses.get(0));
                    break;
                default:
                    burnAddresses = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.burnAddresses));
            }
            List<Map<String, String>> lockedAddresses;
            switch (this.lockedAddresses == null ? 0 : this.lockedAddresses.size()) {
                case 0:
                    lockedAddresses = Collections.emptyList();
                    break;
                case 1:
                    lockedAddresses = Collections.singletonList(this.lockedAddresses.get(0));
                    break;
                default:
                    lockedAddresses = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.lockedAddresses));
            }
            List<ProtocolStandard> supportedStandards;
            switch (this.supportedStandards == null ? 0 : this.supportedStandards.size()) {
                case 0:
                    supportedStandards = Collections.emptyList();
                    break;
                case 1:
                    supportedStandards = Collections.singletonList(this.supportedStandards.get(0));
                    break;
                default:
                    supportedStandards = Collections.unmodifiableList(new ArrayList<ProtocolStandard>(this.supportedStandards));
            }
            List<Map<String, Object>> layerTwoSolutions;
            switch (this.layerTwoSolutions == null ? 0 : this.layerTwoSolutions.size()) {
                case 0:
                    layerTwoSolutions = Collections.emptyList();
                    break;
                case 1:
                    layerTwoSolutions = Collections.singletonList(this.layerTwoSolutions.get(0));
                    break;
                default:
                    layerTwoSolutions = Collections.unmodifiableList(new ArrayList<Map<String, Object>>(this.layerTwoSolutions));
            }
            List<Map<String, Object>> privacySolutions;
            switch (this.privacySolutions == null ? 0 : this.privacySolutions.size()) {
                case 0:
                    privacySolutions = Collections.emptyList();
                    break;
                case 1:
                    privacySolutions = Collections.singletonList(this.privacySolutions.get(0));
                    break;
                default:
                    privacySolutions = Collections.unmodifiableList(new ArrayList<Map<String, Object>>(this.privacySolutions));
            }
            List<Map<String, Object>> codeRepositories;
            switch (this.codeRepositories == null ? 0 : this.codeRepositories.size()) {
                case 0:
                    codeRepositories = Collections.emptyList();
                    break;
                case 1:
                    codeRepositories = Collections.singletonList(this.codeRepositories.get(0));
                    break;
                default:
                    codeRepositories = Collections.unmodifiableList(new ArrayList<Map<String, Object>>(this.codeRepositories));
            }
            List<Map<String, String>> otherSocialNetworks;
            switch (this.otherSocialNetworks == null ? 0 : this.otherSocialNetworks.size()) {
                case 0:
                    otherSocialNetworks = Collections.emptyList();
                    break;
                case 1:
                    otherSocialNetworks = Collections.singletonList(this.otherSocialNetworks.get(0));
                    break;
                default:
                    otherSocialNetworks = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.otherSocialNetworks));
            }
            List<Map<String, String>> otherDocumentUrls;
            switch (this.otherDocumentUrls == null ? 0 : this.otherDocumentUrls.size()) {
                case 0:
                    otherDocumentUrls = Collections.emptyList();
                    break;
                case 1:
                    otherDocumentUrls = Collections.singletonList(this.otherDocumentUrls.get(0));
                    break;
                default:
                    otherDocumentUrls = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.otherDocumentUrls));
            }
            List<Map<String, String>> explorerAddresses;
            switch (this.explorerAddresses == null ? 0 : this.explorerAddresses.size()) {
                case 0:
                    explorerAddresses = Collections.emptyList();
                    break;
                case 1:
                    explorerAddresses = Collections.singletonList(this.explorerAddresses.get(0));
                    break;
                default:
                    explorerAddresses = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.explorerAddresses));
            }
            List<Map<String, String>> rpcOperators;
            switch (this.rpcOperators == null ? 0 : this.rpcOperators.size()) {
                case 0:
                    rpcOperators = Collections.emptyList();
                    break;
                case 1:
                    rpcOperators = Collections.singletonList(this.rpcOperators.get(0));
                    break;
                default:
                    rpcOperators = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.rpcOperators));
            }
            List<Map<String, String>> assetIndustries;
            switch (this.assetIndustries == null ? 0 : this.assetIndustries.size()) {
                case 0:
                    assetIndustries = Collections.emptyList();
                    break;
                case 1:
                    assetIndustries = Collections.singletonList(this.assetIndustries.get(0));
                    break;
                default:
                    assetIndustries = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.assetIndustries));
            }
            List<Map<String, String>> consensusMechanisms;
            switch (this.consensusMechanisms == null ? 0 : this.consensusMechanisms.size()) {
                case 0:
                    consensusMechanisms = Collections.emptyList();
                    break;
                case 1:
                    consensusMechanisms = Collections.singletonList(this.consensusMechanisms.get(0));
                    break;
                default:
                    consensusMechanisms = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.consensusMechanisms));
            }
            List<Map<String, String>> consensusAlgorithmTypes;
            switch (this.consensusAlgorithmTypes == null ? 0 : this.consensusAlgorithmTypes.size()) {
                case 0:
                    consensusAlgorithmTypes = Collections.emptyList();
                    break;
                case 1:
                    consensusAlgorithmTypes = Collections.singletonList(this.consensusAlgorithmTypes.get(0));
                    break;
                default:
                    consensusAlgorithmTypes = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.consensusAlgorithmTypes));
            }
            List<Map<String, String>> hashingAlgorithmTypes;
            switch (this.hashingAlgorithmTypes == null ? 0 : this.hashingAlgorithmTypes.size()) {
                case 0:
                    hashingAlgorithmTypes = Collections.emptyList();
                    break;
                case 1:
                    hashingAlgorithmTypes = Collections.singletonList(this.hashingAlgorithmTypes.get(0));
                    break;
                default:
                    hashingAlgorithmTypes = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.hashingAlgorithmTypes));
            }
            List<Map<String, String>> projectLeaders;
            switch (this.projectLeaders == null ? 0 : this.projectLeaders.size()) {
                case 0:
                    projectLeaders = Collections.emptyList();
                    break;
                case 1:
                    projectLeaders = Collections.singletonList(this.projectLeaders.get(0));
                    break;
                default:
                    projectLeaders = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.projectLeaders));
            }
            List<Map<String, String>> associatedContactDetails;
            switch (this.associatedContactDetails == null ? 0 : this.associatedContactDetails.size()) {
                case 0:
                    associatedContactDetails = Collections.emptyList();
                    break;
                case 1:
                    associatedContactDetails = Collections.singletonList(this.associatedContactDetails.get(0));
                    break;
                default:
                    associatedContactDetails = Collections.unmodifiableList(new ArrayList<Map<String, String>>(this.associatedContactDetails));
            }

            return new Asset(this.id, this.symbol, this.uri, this.assetType, this.assetIssuerName, this.parentAssetSymbol, this.createdOn, this.updatedOn, this.publicNotice, this.name, this.launchDate, assetAlternativeIds, this.assetDescriptionSnippet, this.assetDecimalPoints, supportedPlatforms, assetCustodians, assetSecurityMetrics, this.supplyMax, this.supplyIssued, this.supplyTotal, this.supplyCirculating, this.supplyFuture, this.supplyLocked, this.supplyBurnt, this.supplyStaked, burnAddresses, lockedAddresses, this.hasSmartContractCapabilities, this.smartContractSupportType, this.lastBlockHashesPerSecond, this.lastBlockDifficulty, supportedStandards, layerTwoSolutions, privacySolutions, codeRepositories, otherSocialNetworks, this.heldTokenSale, this.heldEquitySale, this.websiteUrl, this.blogUrl, this.whitePaperUrl, otherDocumentUrls, explorerAddresses, rpcOperators, this.assetSymbolGlyph, assetIndustries, consensusMechanisms, consensusAlgorithmTypes, hashingAlgorithmTypes, this.mktCapPenalty, this.circulatingMktCapUsd, this.totalMktCapUsd, this.assetDescription, this.assetDescriptionSummary, projectLeaders, associatedContactDetails, this.seoTitle, this.seoDescription);
        }

        public String toString() {
            return "Asset.Builder(id=" + this.id + ", symbol=" + this.symbol + ", uri=" + this.uri + ", assetType=" + this.assetType + ", assetIssuerName=" + this.assetIssuerName + ", parentAssetSymbol=" + this.parentAssetSymbol + ", createdOn=" + this.createdOn + ", updatedOn=" + this.updatedOn + ", publicNotice=" + this.publicNotice + ", name=" + this.name + ", launchDate=" + this.launchDate + ", assetAlternativeIds$key=" + this.assetAlternativeIds$key + ", assetAlternativeIds$value=" + this.assetAlternativeIds$value + ", assetDescriptionSnippet=" + this.assetDescriptionSnippet + ", assetDecimalPoints=" + this.assetDecimalPoints + ", supportedPlatforms=" + this.supportedPlatforms + ", assetCustodians=" + this.assetCustodians + ", assetSecurityMetrics=" + this.assetSecurityMetrics + ", supplyMax=" + this.supplyMax + ", supplyIssued=" + this.supplyIssued + ", supplyTotal=" + this.supplyTotal + ", supplyCirculating=" + this.supplyCirculating + ", supplyFuture=" + this.supplyFuture + ", supplyLocked=" + this.supplyLocked + ", supplyBurnt=" + this.supplyBurnt + ", supplyStaked=" + this.supplyStaked + ", burnAddresses=" + this.burnAddresses + ", lockedAddresses=" + this.lockedAddresses + ", hasSmartContractCapabilities=" + this.hasSmartContractCapabilities + ", smartContractSupportType=" + this.smartContractSupportType + ", lastBlockHashesPerSecond=" + this.lastBlockHashesPerSecond + ", lastBlockDifficulty=" + this.lastBlockDifficulty + ", supportedStandards=" + this.supportedStandards + ", layerTwoSolutions=" + this.layerTwoSolutions + ", privacySolutions=" + this.privacySolutions + ", codeRepositories=" + this.codeRepositories + ", otherSocialNetworks=" + this.otherSocialNetworks + ", heldTokenSale=" + this.heldTokenSale + ", heldEquitySale=" + this.heldEquitySale + ", websiteUrl=" + this.websiteUrl + ", blogUrl=" + this.blogUrl + ", whitePaperUrl=" + this.whitePaperUrl + ", otherDocumentUrls=" + this.otherDocumentUrls + ", explorerAddresses=" + this.explorerAddresses + ", rpcOperators=" + this.rpcOperators + ", assetSymbolGlyph=" + this.assetSymbolGlyph + ", assetIndustries=" + this.assetIndustries + ", consensusMechanisms=" + this.consensusMechanisms + ", consensusAlgorithmTypes=" + this.consensusAlgorithmTypes + ", hashingAlgorithmTypes=" + this.hashingAlgorithmTypes + ", mktCapPenalty=" + this.mktCapPenalty + ", circulatingMktCapUsd=" + this.circulatingMktCapUsd + ", totalMktCapUsd=" + this.totalMktCapUsd + ", assetDescription=" + this.assetDescription + ", assetDescriptionSummary=" + this.assetDescriptionSummary + ", projectLeaders=" + this.projectLeaders + ", associatedContactDetails=" + this.associatedContactDetails + ", seoTitle=" + this.seoTitle + ", seoDescription=" + this.seoDescription + ")";
        }
    }

}