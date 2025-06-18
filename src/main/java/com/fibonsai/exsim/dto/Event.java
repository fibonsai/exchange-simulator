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

package com.fibonsai.exsim.dto;

import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.UUID;

public record Event(EventType type, Object event, String traceId, Throwable error) {
    public enum EventType {
        ERROR,
        INFO,
        UNDEF
    }

    public Event(Object event) {
        this(EventType.UNDEF, event);
    }

    public Event(EventType type, Object event) {
        this(type, event, UUID.randomUUID().toString());
    }

    public Event(EventType type, Object event, @Nullable String traceId) {
        this(type, event, traceId, null);
    }

    public Event(EventType type, Object event, @Nullable String traceId, @Nullable Throwable error) {
        this.type = Objects.requireNonNullElse(type, EventType.UNDEF);
        this.event = Objects.requireNonNull(event);
        this.traceId = Objects.requireNonNullElse(traceId, UUID.randomUUID().toString());
        this.error = error;
    }

    public String errorMessage() {
        return error != null ? error.getMessage() : "";
    }

    @Override
    public String toString() {
        return """
                { "event": "%s", "type": "%s", "trace_id": "%s", "error": %s }
                """.formatted(event, type, traceId, error);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Event(EventType oType, Object oEvent, String oTraceId, Throwable oError)
                && oEvent.equals(event())
                && oType.equals(type())
                && oTraceId.equals(traceId())
                && oError != null ? oError.equals(error()) : error() == null;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + event.hashCode();
        result = 31 * result + traceId.hashCode();
        result = 31 * result + (error != null ? error.getClass().hashCode() : 0);
        result = 31 * result + errorMessage().hashCode();
        return result;
    }
}
