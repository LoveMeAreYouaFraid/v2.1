package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.DepositInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 押金记录response
 */
public class GetVendorDepositsResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private List<DepositInfo> depositInfoList;

        public List<DepositInfo> getDepositInfoList() {
            return depositInfoList;
        }

        public void setDepositInfoList(List<DepositInfo> depositInfoList) {
            this.depositInfoList = depositInfoList;
        }
    }
}
