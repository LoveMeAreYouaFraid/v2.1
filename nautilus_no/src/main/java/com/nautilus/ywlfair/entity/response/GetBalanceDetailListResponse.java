package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.BalanceInfo;
import com.nautilus.ywlfair.entity.bean.PicInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 收支明细列表response
 */
public class GetBalanceDetailListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        List<BalanceInfo> balanceDetailList;

        public List<BalanceInfo> getBalanceDetailList() {
            return balanceDetailList;
        }

        public void setBalanceDetailList(List<BalanceInfo> balanceDetailList) {
            this.balanceDetailList = balanceDetailList;
        }
    }
}
