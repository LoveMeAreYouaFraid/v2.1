package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.OrderInfo;

import java.io.Serializable;

/**
 * 订单详情response
 */
public class GetOrderDetailResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        private OrderInfo orderInfo;

        public OrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }
    }
}
