package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.BoothActInfo;
import com.nautilus.ywlfair.entity.bean.ShowTextInfo;
import com.nautilus.ywlfair.entity.bean.WalletInfo;

import java.io.Serializable;

/**
 * 钱包信息response
 */
public class GetWalletInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{
        private WalletInfo wallet;

        public WalletInfo getWallet() {
            return wallet;
        }

        public void setWallet(WalletInfo wallet) {
            this.wallet = wallet;
        }
    }


}
