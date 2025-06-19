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

package com.fibonsai.exsim.xchange_core.currency;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fibonsai.exsim.xchange_core.deser.InstrumentDeserializer;

import java.io.Serializable;

/**
 * Base object for financial instruments supported in xchange such as CurrencyPair, Future or Option
 */
@JsonDeserialize(using = InstrumentDeserializer.class)
public abstract class Instrument implements Serializable {

  private static final long serialVersionUID = 414711266389792746L;

  public abstract Currency getBase();

  public abstract Currency getCounter();
}
