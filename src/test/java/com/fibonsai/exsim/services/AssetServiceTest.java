
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

package com.fibonsai.exsim.services;

import com.fibonsai.exsim.config.JacksonMapperConfigurator;
import com.fibonsai.exsim.dto.asset.Asset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Currency;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AssetService.class, JacksonMapperConfigurator.class})
class AssetServiceTest {

    @Autowired
    private AssetService assetService;

    @BeforeEach
    void setUp() {
        assetService.init();
    }

    @Test
    void testDefaultAsset() {
        Asset defaultAsset = assetService.defaultAsset();
        assertEquals(AssetService.DEFAULT_ASSET, defaultAsset.symbol());
    }

    @Test
    void testAddCurrency() {
        Currency currency = Currency.getInstance("EUR");
        assetService.add(currency);
        assertTrue(assetService.assets().containsKey("EUR"));
    }

    @Test
    void testAddAsset() {
        Asset asset = Asset.builder().symbol("TEST").build();
        assetService.add(asset);
        assertTrue(assetService.assets().containsKey("TEST"));
    }

    @Test
    void testLoadFiatAssets() {
        assetService.loadFiatAssets();
        assertFalse(assetService.assets().isEmpty());
    }

    @Test
    void testLoadFromFile() {
        assetService.loadFromFile();
        assertFalse(assetService.assets().isEmpty());
    }

    @Test
    void testAssets_mapIsReadOnly() {
        Map<String, Asset> assets = assetService.assets();
        assertThrows(UnsupportedOperationException.class, () -> assets.put("test", Asset.builder().build()));
    }
}
