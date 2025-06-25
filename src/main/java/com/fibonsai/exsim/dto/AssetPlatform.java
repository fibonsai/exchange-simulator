package com.fibonsai.exsim.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record AssetPlatform(
        Blockchain blockchain,
        BigDecimal blockchainAssetId,
        String tokenStandard,
        String explorerUrl,
        String smartContractAddress,
        long launchDate,
        BigDecimal decimals,
        boolean isInherited
) implements Serializable {

}
