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

package com.fibonsai.exsim.util;

import com.fibonsai.exsim.dto.asset.Asset;
import com.fibonsai.exsim.dto.asset.AssetType;

import java.math.BigDecimal;
import java.util.Currency;

public class AssetUtil {

    public static final Asset NULL = Asset.builder().id(-1L).name("NULL").build();
    public static final String DEFAULT_SEPARATOR = "/";
    public static final Asset DEFAULT_BASE = NULL;
    public static final Asset DEFAULT_QUOTE = AssetUtil.fromCurrency(Currency.getInstance("USD"));

    public static Asset fromCurrency(Currency currency) {
        return Asset.builder()
                .id((long) currency.getNumericCode())
                .name(currency.getCurrencyCode())
                .assetDescription(currency.getDisplayName())
                .assetSymbolGlyph(currency.getSymbol())
                .assetDecimalPoints(BigDecimal.valueOf(currency.getDefaultFractionDigits()))
                .symbol(currency.getSymbol())
                .assetType(AssetType.FIAT)
                .build();
    }
}
