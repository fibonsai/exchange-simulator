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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fibonsai.exsim.xchange_core.util.DateUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Converts double that represents unit milliseconds timestamp to Date. e.g. "1657444151.611" is
 * converted to "Sun Jul 10 11:09:11 CEST 2022"
 */
public class UnixTimestampNanoSecondsDeserializer extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    String value = jp.getValueAsString();
    long valueWithMilliseconds =
        new BigDecimal(value).multiply(BigDecimal.valueOf(1000)).longValue();
    return DateUtils.fromUnixTimeWithMilliseconds(valueWithMilliseconds);
  }
}
