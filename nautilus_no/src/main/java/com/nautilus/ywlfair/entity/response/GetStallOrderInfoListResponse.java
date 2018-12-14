package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.OfflineOrdersInfo;

import java.io.Serializable;
import java.util.List;

/**
 *摊主订单列表
 */
public class GetStallOrderInfoListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private List<OfflineOrdersInfo> orderInfoList;

        public List<OfflineOrdersInfo> getOrderInfoList() {
            return orderInfoList;
        }

        public void setOrderInfoList(List<OfflineOrdersInfo> orderInfoList) {
            this.orderInfoList = orderInfoList;
        }
    }
}
