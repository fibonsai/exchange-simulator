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

package com.fibonsai.exsim.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fibonsai.exsim.dto.AssetCustodian;
import com.fibonsai.exsim.dto.Protocol;
import com.fibonsai.exsim.dto.deser.AssetCustodianDeserializer;
import com.fibonsai.exsim.dto.deser.ProtocolDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonMapperConfigurator {

    @Bean
    public ObjectMapper objectMapper() {
        Version version = new Version(1, 0 , 0, null, null, null);
        SimpleModule assetProtocolModule = new SimpleModule("ProtocolDeserializer", version);
        SimpleModule assetCustodianModule = new SimpleModule("AssetCustodianDeserializer", version);
        assetProtocolModule.addDeserializer(Protocol.class, new ProtocolDeserializer());
        assetCustodianModule.addDeserializer(AssetCustodian.class, new AssetCustodianDeserializer());
        return new ObjectMapper()
                .registerModule(assetProtocolModule)
                .registerModule(assetCustodianModule)
                .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
