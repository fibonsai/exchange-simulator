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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.io.Serial;
import java.io.Serializable;

@JsonDeserialize(builder = RateLimitSpec.Builder.class)
public record RateLimitSpec(
        Integer calls,
        Integer timeSpan,
        Integer timeWindowInSeconds
) implements Serializable {

    @Serial
    private static final long serialVersionUID = -7340731832345284129L;

    public static final RateLimitSpec DEFAULT = new RateLimitSpec(Integer.MAX_VALUE, 0, 1);

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "RateLimitSpec{" +
                "calls=" + calls +
                ", timeSpan=" + timeSpan +
                ", timeWindowInSeconds=" + timeWindowInSeconds +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "", buildMethodName = "build")
    public static class Builder {
        private Integer calls;
        private Integer timeSpan;
        private Integer timeWindowInSeconds;

        Builder() {
        }

        public Builder calls(Integer calls) {
            this.calls = calls;
            return this;
        }

        public Builder timeSpan(Integer timeSpan) {
            this.timeSpan = timeSpan;
            return this;
        }

        public Builder timeWindowInSeconds(Integer timeWindowInSeconds) {
            this.timeWindowInSeconds = timeWindowInSeconds;
            return this;
        }

        public RateLimitSpec build() {
            return new RateLimitSpec(this.calls, this.timeSpan, this.timeWindowInSeconds);
        }
    }
}
