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

import com.fibonsai.exsim.types.DepositFundsParams;
import com.fibonsai.exsim.types.FundsParams;
import com.fibonsai.exsim.types.WalletState;
import com.fibonsai.exsim.types.WithdrawFundsParams;
import org.springframework.lang.NonNull;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.Instant;

import static com.fibonsai.exsim.types.WalletState.*;

public class Wallet {

    private final Asset asset;
    private final String walletAddress;
    private final String owner;

    private BigDecimal amount = BigDecimal.ZERO;
    private WalletState state = OFFLINE;
    private Instant timestamp = Instant.now();

    public Wallet(String owner, Asset asset, String walletAddress) {
        this.owner = owner;
        this.asset = asset;
        this.walletAddress = walletAddress;
    }

    public String owner() {
        return owner;
    }

    public String address() {
        return walletAddress;
    }

    public Asset asset() {
        return asset;
    }

    public BigDecimal amount() {
        return amount;
    }

    public Instant timestamp() {
        return timestamp;
    }

    public WalletState state() {
        return state;
    }

    public Wallet setState(@NonNull WalletState state) {
        this.state = state;
        this.timestamp = Instant.now();
        return this;
    }

    public Wallet transaction(FundsParams params) throws Exception {
        if (state().is(OFFLINE, SYNC_ERROR, AUDIT_BLOCK, READ_ONLY)) {
            throw new IllegalStateException("Transaction is not possible. Wallet state is " + state());
        }
        if (!asset().equals(params.getAsset())) {
            throw new IllegalArgumentException("Transaction not possible using different iso4217: %s != %s".formatted(asset(), params.getAsset()));
        }
        switch (params) {
            case DepositFundsParams dfParams -> {
                if (state().equals(WITHDRAW_ONLY)) {
                    throw new IllegalStateException("Deposit is not possible. Wallet allow only withdraw transaction");
                }
                this.amount = this.amount.add(dfParams.getAmount());
            }
            case WithdrawFundsParams wfParams -> {
                var withdrawFundsAmount = wfParams.getAmount();
                if (withdrawFundsAmount.compareTo(amount) <= 0) {
                    this.amount = this.amount.subtract(wfParams.getAmount());
                } else {
                    throw new InsufficientResourcesException("Funds insufficient");
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + params);
        }
        this.timestamp = Instant.now();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;
        return walletAddress.equals(wallet.walletAddress);
    }

    @Override
    public int hashCode() {
        return walletAddress.hashCode();
    }

    @Override
    public String toString() {
        return """
                { "timestamp": %s, "asset": %s, "state": %s, "walletAddress": "%s", "owner": "%s", "amount": %s }
                """.formatted(timestamp(), asset(), state(), address(), owner(), amount());
    }
}
