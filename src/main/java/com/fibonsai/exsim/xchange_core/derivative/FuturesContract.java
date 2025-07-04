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

package com.fibonsai.exsim.xchange_core.derivative;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fibonsai.exsim.xchange_core.currency.Currency;
import com.fibonsai.exsim.xchange_core.currency.CurrencyPair;
import com.fibonsai.exsim.xchange_core.currency.Instrument;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class FuturesContract extends Instrument
    implements Derivative, Comparable<FuturesContract>, Serializable {

  private static final long serialVersionUID = 6876906648149216819L;

  private static final Comparator<FuturesContract> COMPARATOR =
      Comparator.comparing(FuturesContract::getCurrencyPair)
          .thenComparing(FuturesContract::getPrompt);

  /** The CurrencyPair the FuturesContract is based upon */
  private final CurrencyPair currencyPair;

  /** The Date when the FuturesContract expires, when null it is perpetual */
  private final String prompt;

  public FuturesContract(CurrencyPair currencyPair, String prompt) {
    this.currencyPair = currencyPair;
    this.prompt = prompt;
  }

  @JsonCreator
  public FuturesContract(final String symbol) {
    String[] parts = symbol.split("/");
    if (parts.length < 3) {
      throw new IllegalArgumentException("Could not parse futures contract from '" + symbol + "'");
    }

    String base = parts[0];
    String counter = parts[1];

    String prompt = parts[2];
    this.currencyPair = new CurrencyPair(base, counter);

    this.prompt = prompt;
  }

  @Override
  public CurrencyPair getCurrencyPair() {
    return currencyPair;
  }

  public String getPrompt() {
    return prompt;
  }

  public boolean isPerpetual() {
    return this.prompt.matches("(?i)PERP|SWAP|PERPETUAL");
  }

  @Override
  public int compareTo(final FuturesContract that) {
    return COMPARATOR.compare(this, that);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final FuturesContract contract = (FuturesContract) o;
    return Objects.equals(currencyPair, contract.currencyPair)
        && Objects.equals(prompt, contract.prompt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currencyPair, prompt);
  }

  @Override
  public Currency getBase() {
    return currencyPair.getBase();
  }

  @Override
  public Currency getCounter() {
    return currencyPair.getCounter();
  }

  @JsonValue
  @Override
  public String toString() {

    return currencyPair + "/" + prompt;
  }
}
