package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.BalanceDetailInfo;
import com.nautilus.ywlfair.entity.bean.ScoreInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 收支明细response
 */
public class GetBalanceDetailResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable {

        private BalanceDetailInfo balanceDetail;

        public BalanceDetailInfo getBalanceDetail() {
            return balanceDetail;
        }

        public void setBalanceDetail(BalanceDetailInfo balanceDetail) {
            this.balanceDetail = balanceDetail;
        }
    }
}
