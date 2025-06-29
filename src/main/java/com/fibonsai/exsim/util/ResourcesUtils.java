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

package com.fibonsai.exsim.util;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Objects;

@Slf4j
public class ResourcesUtils {
    public static InputStream getResourceAsStream(Class<?> fromClass, String name) {
        try {
            return Objects.requireNonNull(fromClass.getClassLoader().getResourceAsStream(name));
        } catch (Exception e) {
            String errorStr = "%s not found".formatted(name);
            log.error(errorStr);
            throw new RuntimeException(errorStr);
        }
    }
}
