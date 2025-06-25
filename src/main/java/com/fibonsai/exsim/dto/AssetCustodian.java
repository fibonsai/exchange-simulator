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

package com.fibonsai.exsim.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;

@JsonDeserialize(builder = AssetCustodian.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssetCustodian(
        String name
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        @JsonProperty("NAME")
        private String name;

        Builder() {
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public AssetCustodian build() {
            return new AssetCustodian(this.name);
        }

        public String toString() {
            return "AssetCustodian.Builder(name=" + this.name + ")";
        }
    }
}
