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

@JsonDeserialize(builder = OtherDocumentsUrl.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OtherDocumentsUrl(
        String type,
        String url,
        Integer version,
        String comment
) {

    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {

        @JsonProperty("TYPE")
        private String type;

        @JsonProperty("URL")
        private String url;

        @JsonProperty("VERSION")
        private Integer version;

        @JsonProperty("COMMENT")
        private String comment;

        Builder() {
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public OtherDocumentsUrl build() {
            return new OtherDocumentsUrl(this.type, this.url, this.version, this.comment);
        }

        public String toString() {
            return "OtherDocumentsUrl.Builder(type=" + this.type + ", url=" + this.url + ", version=" + this.version + ", comment=" + this.comment + ")";
        }
    }
}
