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

package com.fibonsai.exsim.services;

import org.springframework.stereotype.Service;

@Service
public class MarketDataService extends AbstractService {

    private final AssetService assetService;

    public MarketDataService(AssetService assetService) {
        super();
        this.assetService = assetService;
    }

    public void prepareAssets() {
        assetService.init();
    }

    public void start() {

    }
}
