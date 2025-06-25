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
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@JsonDeserialize(builder = Asset.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Asset(
        Long id,
        String symbol,
        String uri,
        AssetType assetType,
        String assetIssuerName,
        String parentAssetSymbol,
        Long createdOn,
        Long updatedOn,
        String publicNotice,
        String name,
        Long launchDate,
        List<Map<String, String>> assetAlternativeIds,
        String assetDescriptionSnippet,
        BigDecimal assetDecimalPoints,
        List<AssetPlatform> supportedPlatforms,
        List<AssetCustodian> assetCustodians,
        List<Map<String, String>> assetSecurityMetrics,
        BigDecimal supplyMax,
        BigDecimal supplyIssued,
        BigDecimal supplyTotal,
        BigDecimal supplyCirculating,
        BigDecimal supplyFuture,
        BigDecimal supplyLocked,
        BigDecimal supplyBurnt,
        BigDecimal supplyStaked,
        List<Map<String, String>> burnAddresses,
        List<Map<String, String>> lockedAddresses,
        Boolean hasSmartContractCapabilities,
        SmartContractSupportType smartContractSupportType,
        BigDecimal lastBlockHashesPerSecond,
        BigDecimal lastBlockDifficulty,
        List<Protocol> supportedStandards,
        List<Map<String, Object>> layerTwoSolutions,
        List<Map<String, Object>> privacySolutions,
        List<Map<String, Object>> codeRepositories,
        List<Map<String, String>> otherSocialNetworks,
        Boolean heldTokenSale,
        Boolean heldEquitySale,
        String websiteUrl,
        String blogUrl,
        String whitePaperUrl,
        List<OtherDocumentsUrl> otherDocumentUrls,
        List<Map<String, String>> explorerAddresses,
        List<Map<String, String>> rpcOperators,
        String assetSymbolGlyph,
        List<Map<String, String>> assetIndustries,
        List<Map<String, String>> consensusMechanisms,
        List<Map<String, String>> consensusAlgorithmTypes,
        List<Map<String, String>> hashingAlgorithmTypes,
        BigDecimal mktCapPenalty,
        BigDecimal circulatingMktCapUsd,
        BigDecimal totalMktCapUsd,
        String assetDescription,
        String assetDescriptionSummary,
        List<Map<String, String>> projectLeaders,
        List<Map<String, String>> associatedContactDetails,
        String seoTitle,
        String seoDescription

) implements Serializable, Comparable<Asset> {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public int compareTo(Asset asset) {
        return symbol().compareTo(asset.symbol());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Asset asset && symbol().equals(asset.symbol());
    }

    @Override
    public int hashCode() {
        return symbol().hashCode();
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

        @JsonProperty("ID")
        Long id = 0L;

        @JsonProperty("SYMBOL")
        String symbol = "";

        @JsonProperty("URI")
        String uri = "";

        @JsonProperty("ASSET_TYPE")
        AssetType assetType = AssetType.UNDEF;

        @JsonProperty("ASSET_ISSUER_NAME")
        String assetIssuerName = "";

        @JsonProperty("PARENT_ASSET_SYMBOL")
        String parentAssetSymbol = "";

        @JsonProperty("CREATED_ON")
        Long createdOn = 0L;

        @JsonProperty("UPDATED_ON")
        Long updatedOn = 0L;

        @JsonProperty("PUBLIC_NOTICE")
        String publicNotice = "";

        @JsonProperty("NAME")
        String name = "";

        @JsonProperty("LAUNCH_DATE")
        Long launchDate = 0L;

        @JsonProperty("ASSET_ALTERNATIVE_IDS")
        List<Map<String, String>> assetAlternativeIds = new ArrayList<>();

        @JsonProperty("ASSET_DESCRIPTION_SNIPPET")
        String assetDescriptionSnippet = "";

        @JsonProperty("ASSET_DECIMAL_POINTS")
        BigDecimal assetDecimalPoints = BigDecimal.ZERO;

        @JsonProperty("SUPPORTED_PLATFORMS")
        List<AssetPlatform> supportedPlatforms = new ArrayList<>();

        @JsonProperty("ASSET_CUSTODIANS")
        List<AssetCustodian> assetCustodians = new ArrayList<>();

        @JsonProperty("ASSET_SECURITY_METRICS")
        List<Map<String, String>> assetSecurityMetrics = new ArrayList<>();

        @JsonProperty("SUPPLY_MAX")
        BigDecimal supplyMax = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_ISSUED")
        BigDecimal supplyIssued = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_TOTAL")
        BigDecimal supplyTotal = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_CIRCULATING")
        BigDecimal supplyCirculating = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_FUTURE")
        BigDecimal supplyFuture = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_LOCKED")
        BigDecimal supplyLocked = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_BURNT")
        BigDecimal supplyBurnt = BigDecimal.ZERO;

        @JsonProperty("SUPPLY_STAKED")
        BigDecimal supplyStaked = BigDecimal.ZERO;

        @JsonProperty("BURN_ADDRESSES")
        List<Map<String, String>> burnAddresses = new ArrayList<>();

        @JsonProperty("LOCKED_ADDRESSES")
        List<Map<String, String>> lockedAddresses = new ArrayList<>();

        @JsonProperty("HAS_SMART_CONTRACT_CAPABILITIES")
        Boolean hasSmartContractCapabilities = false;

        @JsonProperty("SMART_CONTRACT_SUPPORT_TYPE")
        SmartContractSupportType smartContractSupportType = SmartContractSupportType.UNDEF;

        @JsonProperty("LAST_BLOCK_HASHES_PER_SECOND")
        BigDecimal lastBlockHashesPerSecond = BigDecimal.ZERO;

        @JsonProperty("LAST_BLOCK_DIFFICULTY")
        BigDecimal lastBlockDifficulty = BigDecimal.ZERO;

        @JsonProperty("SUPPORTED_STANDARDS")
        List<Protocol> supportedStandards = new ArrayList<>();

        @JsonProperty("LAYER_TWO_SOLUTIONS")
        List<Map<String, Object>> layerTwoSolutions = new ArrayList<>();

        @JsonProperty("PRIVACY_SOLUTIONS")
        List<Map<String, Object>> privacySolutions = new ArrayList<>();

        @JsonProperty("CODE_REPOSITORIES")
        List<Map<String, Object>> codeRepositories = new ArrayList<>();

        @JsonProperty("OTHER_SOCIAL_NETWORKS")
        List<Map<String, String>> otherSocialNetworks = new ArrayList<>();

        @JsonProperty("HELD_TOKEN_SALE")
        Boolean heldTokenSale = false;

        @JsonProperty("HELD_EQUITY_SALE")
        Boolean heldEquitySale = false;

        @JsonProperty("WEBSITE_URL")
        String websiteUrl = "";

        @JsonProperty("BLOG_URL")
        String blogUrl = "";

        @JsonProperty("WHITE_PAPER_URL")
        String whitePaperUrl = "";

        @JsonProperty("OTHER_DOCUMENT_URLS")
        List<OtherDocumentsUrl> otherDocumentUrls = new ArrayList<>();

        @JsonProperty("EXPLORER_ADDRESSES")
        List<Map<String, String>> explorerAddresses = new ArrayList<>();

        @JsonProperty("RPC_OPERATORS")
        List<Map<String, String>> rpcOperators = new ArrayList<>();

        @JsonProperty("ASSET_SYMBOL_GLYPH")
        String assetSymbolGlyph = "";

        @JsonProperty("ASSET_INDUSTRIES")
        List<Map<String, String>> assetIndustries = new ArrayList<>();

        @JsonProperty("CONSENSUS_MECHANISMS")
        List<Map<String, String>> consensusMechanisms = new ArrayList<>();

        @JsonProperty("CONSENSUS_ALGORITHM_TYPES")
        List<Map<String, String>> consensusAlgorithmTypes = new ArrayList<>();

        @JsonProperty("HASHING_ALGORITHM_TYPES")
        List<Map<String, String>> hashingAlgorithmTypes = new ArrayList<>();

        @JsonProperty("MKT_CAP_PENALTY")
        BigDecimal mktCapPenalty = BigDecimal.ZERO;

        @JsonProperty("CIRCULATING_MKT_CAP_USD")
        BigDecimal circulatingMktCapUsd = BigDecimal.ZERO;

        @JsonProperty("TOTAL_MKT_CAP_USD")
        BigDecimal totalMktCapUsd = BigDecimal.ZERO;

        @JsonProperty("ASSET_DESCRIPTION")
        String assetDescription = "";

        @JsonProperty("ASSET_DESCRIPTION_SUMMARY")
        String assetDescriptionSummary = "";

        @JsonProperty("PROJECT_LEADERS")
        List<Map<String, String>> projectLeaders = new ArrayList<>();

        @JsonProperty("ASSOCIATED_CONTACT_DETAILS")
        List<Map<String, String>> associatedContactDetails = new ArrayList<>();

        @JsonProperty("SEO_TITLE")
        String seoTitle = "";

        @JsonProperty("SEO_DESCRIPTION")
        String seoDescription = "";

        Builder() {
        }

        public Builder id(Long id) {
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

        public Builder assetAlternativeId(Map<String, String> assetAlternativeId) {
            if (this.assetAlternativeIds == null) this.assetAlternativeIds = new ArrayList<>();
            this.assetAlternativeIds.add(assetAlternativeId);
            return this;
        }

        public Builder assetAlternativeIds(Collection<? extends Map<String, String>> assetAlternativeIds) {
            if (assetAlternativeIds == null) return this;
            if (this.assetAlternativeIds == null) this.assetAlternativeIds = new ArrayList<>();
            this.assetAlternativeIds.addAll(assetAlternativeIds);
            return this;
        }

        public Builder clearAssetAlternativeIds() {
            if (this.assetAlternativeIds != null)
                this.assetAlternativeIds.clear();
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
            if (this.supportedPlatforms == null) this.supportedPlatforms = new ArrayList<>();
            this.supportedPlatforms.add(supportedPlatform);
            return this;
        }

        public Builder supportedPlatforms(Collection<? extends AssetPlatform> supportedPlatforms) {
            if (supportedPlatforms == null) return this;
            if (this.supportedPlatforms == null) this.supportedPlatforms = new ArrayList<>();
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
            if (assetCustodians == null) return this;
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
            if (assetSecurityMetrics == null) return this;
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
            if (burnAddresses == null) return this;
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
            if (lockedAddresses == null) return this;
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

        public Builder supportedStandard(Protocol protocol) {
            if (this.supportedStandards == null) this.supportedStandards = new ArrayList<Protocol>();
            this.supportedStandards.add(protocol);
            return this;
        }

        public Builder supportedStandards(Collection<? extends Protocol> supportedStandards) {
            if (supportedStandards == null) return this;
            if (this.supportedStandards == null) this.supportedStandards = new ArrayList<Protocol>();
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
            if (layerTwoSolutions == null) return this;
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
            if (privacySolutions == null) return this;
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
            if (codeRepositories == null) return this;
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
            if (otherSocialNetworks == null) return this;
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

        public Builder otherDocumentUrl(OtherDocumentsUrl otherDocumentUrl) {
            if (this.otherDocumentUrls == null) this.otherDocumentUrls = new ArrayList<OtherDocumentsUrl>();
            this.otherDocumentUrls.add(otherDocumentUrl);
            return this;
        }

        public Builder otherDocumentUrls(Collection<? extends OtherDocumentsUrl> otherDocumentUrls) {
            if (otherDocumentUrls == null) return this;
            if (this.otherDocumentUrls == null) this.otherDocumentUrls = new ArrayList<OtherDocumentsUrl>();
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
            if (explorerAddresses == null) return this;
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
            if (rpcOperators == null) return this;
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
            if (assetIndustries == null) return this;
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
            if (consensusMechanisms == null) return this;
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
            if (consensusAlgorithmTypes == null) return this;
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
            if (hashingAlgorithmTypes == null) return this;
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
            if (projectLeaders == null) return this;
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
            if (associatedContactDetails == null) return this;
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
            List<Map<String, String>> assetAlternativeIds = switch (this.assetAlternativeIds == null ? 0 : this.assetAlternativeIds.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.assetAlternativeIds.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.assetAlternativeIds));
            };
            List<AssetPlatform> supportedPlatforms = switch (this.supportedPlatforms == null ? 0 : this.supportedPlatforms.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.supportedPlatforms.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.supportedPlatforms));
            };
            List<AssetCustodian> assetCustodians = switch (this.assetCustodians == null ? 0 : this.assetCustodians.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.assetCustodians.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.assetCustodians));
            };
            List<Map<String, String>> assetSecurityMetrics = switch (this.assetSecurityMetrics == null ? 0 : this.assetSecurityMetrics.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.assetSecurityMetrics.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.assetSecurityMetrics));
            };
            List<Map<String, String>> burnAddresses = switch (this.burnAddresses == null ? 0 : this.burnAddresses.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.burnAddresses.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.burnAddresses));
            };
            List<Map<String, String>> lockedAddresses = switch (this.lockedAddresses == null ? 0 : this.lockedAddresses.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.lockedAddresses.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.lockedAddresses));
            };
            List<Protocol> supportedStandards = switch (this.supportedStandards == null ? 0 : this.supportedStandards.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.supportedStandards.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.supportedStandards));
            };
            List<Map<String, Object>> layerTwoSolutions = switch (this.layerTwoSolutions == null ? 0 : this.layerTwoSolutions.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.layerTwoSolutions.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.layerTwoSolutions));
            };
            List<Map<String, Object>> privacySolutions = switch (this.privacySolutions == null ? 0 : this.privacySolutions.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.privacySolutions.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.privacySolutions));
            };
            List<Map<String, Object>> codeRepositories = switch (this.codeRepositories == null ? 0 : this.codeRepositories.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.codeRepositories.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.codeRepositories));
            };
            List<Map<String, String>> otherSocialNetworks = switch (this.otherSocialNetworks == null ? 0 : this.otherSocialNetworks.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.otherSocialNetworks.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.otherSocialNetworks));
            };
            List<OtherDocumentsUrl> otherDocumentUrls = switch (this.otherDocumentUrls == null ? 0 : this.otherDocumentUrls.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.otherDocumentUrls.getFirst());
                default -> Collections.unmodifiableList(this.otherDocumentUrls);
            };
            List<Map<String, String>> explorerAddresses = switch (this.explorerAddresses == null ? 0 : this.explorerAddresses.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.explorerAddresses.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.explorerAddresses));
            };
            List<Map<String, String>> rpcOperators = switch (this.rpcOperators == null ? 0 : this.rpcOperators.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.rpcOperators.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.rpcOperators));
            };
            List<Map<String, String>> assetIndustries = switch (this.assetIndustries == null ? 0 : this.assetIndustries.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.assetIndustries.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.assetIndustries));
            };
            List<Map<String, String>> consensusMechanisms = switch (this.consensusMechanisms == null ? 0 : this.consensusMechanisms.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.consensusMechanisms.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.consensusMechanisms));
            };
            List<Map<String, String>> consensusAlgorithmTypes = switch (this.consensusAlgorithmTypes == null ? 0 : this.consensusAlgorithmTypes.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.consensusAlgorithmTypes.getFirst());
                default ->
                        Collections.unmodifiableList(new ArrayList<>(this.consensusAlgorithmTypes));
            };
            List<Map<String, String>> hashingAlgorithmTypes = switch (this.hashingAlgorithmTypes == null ? 0 : this.hashingAlgorithmTypes.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.hashingAlgorithmTypes.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.hashingAlgorithmTypes));
            };
            List<Map<String, String>> projectLeaders = switch (this.projectLeaders == null ? 0 : this.projectLeaders.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.projectLeaders.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.projectLeaders));
            };
            List<Map<String, String>> associatedContactDetails = switch (this.associatedContactDetails == null ? 0 : this.associatedContactDetails.size()) {
                case 0 -> Collections.emptyList();
                case 1 -> Collections.singletonList(this.associatedContactDetails.getFirst());
                default -> Collections.unmodifiableList(new ArrayList<>(this.associatedContactDetails));
            };

            return new Asset(this.id, this.symbol, this.uri, this.assetType, this.assetIssuerName, this.parentAssetSymbol, this.createdOn, this.updatedOn, this.publicNotice, this.name, this.launchDate, assetAlternativeIds, this.assetDescriptionSnippet, this.assetDecimalPoints, supportedPlatforms, assetCustodians, assetSecurityMetrics, this.supplyMax, this.supplyIssued, this.supplyTotal, this.supplyCirculating, this.supplyFuture, this.supplyLocked, this.supplyBurnt, this.supplyStaked, burnAddresses, lockedAddresses, this.hasSmartContractCapabilities, this.smartContractSupportType, this.lastBlockHashesPerSecond, this.lastBlockDifficulty, supportedStandards, layerTwoSolutions, privacySolutions, codeRepositories, otherSocialNetworks, this.heldTokenSale, this.heldEquitySale, this.websiteUrl, this.blogUrl, this.whitePaperUrl, otherDocumentUrls, explorerAddresses, rpcOperators, this.assetSymbolGlyph, assetIndustries, consensusMechanisms, consensusAlgorithmTypes, hashingAlgorithmTypes, this.mktCapPenalty, this.circulatingMktCapUsd, this.totalMktCapUsd, this.assetDescription, this.assetDescriptionSummary, projectLeaders, associatedContactDetails, this.seoTitle, this.seoDescription);
        }
    }

}