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

package com.fibonsai.exsim.xchange_core.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fibonsai.exsim.xchange_core.currency.Currency;
import com.fibonsai.exsim.xchange_core.currency.CurrencyPair;

import java.io.IOException;

public class CurrencyPairDeserializer extends JsonDeserializer<CurrencyPair> {

  public static CurrencyPair getCurrencyPairFromString(String currencyPairString) {

    if (currencyPairString == null || currencyPairString.isEmpty()) {
      return null;
    }

    /*
     * Preserve case if exchange is sending mixed-case, otherwise toUpperCase()
     */
    final boolean isMixedCase =
        currencyPairString.matches(".*[a-z]+.*") && currencyPairString.matches(".*[A-Z]+.*");
    if (!isMixedCase) {
      currencyPairString = currencyPairString.toUpperCase();
    }

    /*
     * Assume all symbols are alphanumeric; anything else is a separator
     */
    final String symbols[] = currencyPairString.split("[^a-zA-Z0-9]");
    if (symbols.length == 2) {
      return new CurrencyPair(symbols[0], symbols[1]);
    }

    /*
     * The common case of two 3-character symbols (eg: "BTCUSD")
     */
    if (currencyPairString.length() == 6) {
      final String tradeCurrency = currencyPairString.substring(0, 3);
      final String priceCurrency = currencyPairString.substring(3);
      return new CurrencyPair(tradeCurrency, priceCurrency);
    }

    /*
     * Last-ditch effort to obtain the correct CurrencyPair (eg: "DOGEBTC", "BCBTC", or even "USDEUSD")
     */
    int bestGuess = currencyPairString.length() / 2;
    int bestLength = 0;
    for (int i = 1; i < currencyPairString.length() - 1; ++i) {
      final Currency tradeCurrency =
          Currency.getInstanceNoCreate(currencyPairString.substring(0, i));
      final Currency priceCurrency = Currency.getInstanceNoCreate(currencyPairString.substring(i));
      if (tradeCurrency != null) {
        if (priceCurrency != null) {
          return new CurrencyPair(tradeCurrency, priceCurrency);
        } else if (i > bestLength) {
          bestLength = i;
          bestGuess = i;
        }
      } else if (priceCurrency != null && currencyPairString.length() - i > bestLength) {
        bestLength = currencyPairString.length() - i;
        bestGuess = i;
      }
    }
    final String tradeCurrency = currencyPairString.substring(0, bestGuess);
    final String priceCurrency = currencyPairString.substring(bestGuess);
    return new CurrencyPair(tradeCurrency, priceCurrency);
  }

  @Override
  public CurrencyPair deserialize(JsonParser jsonParser, final DeserializationContext ctxt)
      throws IOException {

    final ObjectCodec oc = jsonParser.getCodec();
    final JsonNode node = oc.readTree(jsonParser);
    final String currencyPairString = node.asText();

    return getCurrencyPairFromString(currencyPairString);
  }
}
