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

package com.fibonsai.exsim;

import java.util.Objects;

public record Event(EventType type, Object event) {
    public enum EventType {
        ERROR,
        UNDEF
    }

    public Event(Object event) {
        this(EventType.UNDEF, event);
    }

    public Event(EventType type, Object event) {
        this.type = Objects.requireNonNullElse(type, EventType.UNDEF);
        this.event = Objects.requireNonNull(event);
    }

    @Override
    public String toString() {
        return """
                { "event": "%s", "type": "%s" }
                """.formatted(event, type);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Event(EventType type1, Object event1) && event1.equals(event()) && type1.equals(type());
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + event.hashCode();
        return result;
    }
}
