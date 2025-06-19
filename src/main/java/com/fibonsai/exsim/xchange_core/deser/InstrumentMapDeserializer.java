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

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fibonsai.exsim.xchange_core.currency.CurrencyPair;
import com.fibonsai.exsim.xchange_core.derivative.FuturesContract;
import com.fibonsai.exsim.xchange_core.derivative.OptionsContract;

public class InstrumentMapDeserializer extends KeyDeserializer {

  @Override
  public Object deserializeKey(String s, DeserializationContext deserializationContext) {
    long count = s.chars().filter(ch -> ch == '/').count();
    // CurrencyPair (Base/Counter) i.e. BTC/USD
    if (count == 1) return new CurrencyPair(s);
    // Futures/Swaps (Base/Counter/Prompt) i.e. BTC/USD/200925
    if (count == 2) return new FuturesContract(s);
    // Options (Base/Counter/Prompt/StrikePrice/Put?Call) i.e. BTC/USD/200925/8956.67/P
    if (count == 4) return new OptionsContract(s);
    else return null;
  }
}
