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
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Matija Mazi
 */
public class SqlUtcTimeDeserializer extends JsonDeserializer<Date> {

  private SimpleDateFormat dateFormat;

  public SqlUtcTimeDeserializer() {
    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  @Override
  public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

    String str = jp.getValueAsString();
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {
      throw new InvalidFormatException(null, "Error parsing as date", str, Date.class);
    }
  }
}
