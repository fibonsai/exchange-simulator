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
import java.util.Date;
import java.util.Locale;

/**
 * Deserializes an rfc1123 formatted Date String to a Java Date rfc1123 format: 'EEE, dd MMM yyyy
 * HH:mm:ss zzz'
 *
 * @author jamespedwards42
 */
public class Rfc1123DateDeserializer extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser jp, final DeserializationContext ctxt) throws IOException {

    return DateUtils.fromRfc1123DateString(jp.getValueAsString(), Locale.US);
  }
}
