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

package com.fibonsai.exsim.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@JsonDeserialize(builder = Api.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Api(
        String id,
        ApiType type,
        ApiAccess access,
        String url,
        Boolean authRequired,
        Map<String, String> authenticationSpecs,
        Boolean enableRateLimits,
        RateLimitSpec rateLimits,
        Long timeout,
        Map<String, String> apiSpecs,
        String description
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static Builder builder() {
        return new Builder();
    }

    public enum ApiType {
        REST,
        WS
    }

    public enum ApiAccess {
        PRIVATE,
        PUBLIC
    }

    @Override
    public String toString() {
        return "Api{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", access=" + access +
                ", url='" + url + '\'' +
                ", authRequired=" + authRequired +
                ", authenticationSpecs=" + authenticationSpecs +
                ", enableRateLimits=" + enableRateLimits +
                ", rateLimits=" + rateLimits +
                ", timeout=" + timeout +
                ", apiSpecs=" + apiSpecs +
                ", description='" + description + '\'' +
                '}';
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String id = "default";
        private ApiType type = ApiType.REST;
        private ApiAccess access = ApiAccess.PUBLIC;
        private String url = "http://localhost";
        private Boolean authRequired = false;
        private final Map<String, String> authenticationSpecs = new HashMap<>();
        private Boolean enableRateLimits;
        private RateLimitSpec rateLimits;
        private Long timeout;
        private Map<String, String> apiSpecs;
        private String description;

        Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(ApiType type) {
            this.type = type;
            return this;
        }

        public Builder access(ApiAccess access) {
            this.access = access;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder authRequired(Boolean authRequired) {
            this.authRequired = authRequired;
            return this;
        }

        public Builder authenticationSpecs(Map<String, String> authenticationSpecs) {
            if (authenticationSpecs == null) return this;
            this.authenticationSpecs.putAll(authenticationSpecs);
            return this;
        }

        public Builder enableRateLimits(Boolean enableRateLimits) {
            this.enableRateLimits = enableRateLimits;
            return this;
        }

        public Builder rateLimits(RateLimitSpec rateLimits) {
            this.rateLimits = rateLimits;
            return this;
        }

        public Builder timeout(Long timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder apiSpecs(Map<String, String> apiSpecs) {
            if (apiSpecs == null) return this;
            this.apiSpecs.putAll(apiSpecs);
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Api build() {
            Map<String, String> authenticationSpecs = this.authenticationSpecs.isEmpty() ?
                    Map.of() :
                    Collections.unmodifiableMap(this.authenticationSpecs);
            Map<String, String> apiSpecs = this.apiSpecs.isEmpty() ?
                    Map.of() :
                    Collections.unmodifiableMap(this.apiSpecs);

            return new Api(
                    this.id,
                    this.type,
                    this.access,
                    this.url,
                    this.authRequired,
                    authenticationSpecs,
                    this.enableRateLimits,
                    this.rateLimits,
                    this.timeout,
                    apiSpecs,
                    this.description);
        }
    }
}
