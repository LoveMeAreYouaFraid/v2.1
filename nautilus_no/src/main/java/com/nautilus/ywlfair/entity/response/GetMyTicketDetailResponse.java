package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;
import com.nautilus.ywlfair.entity.bean.TicketsOrderInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class GetMyTicketDetailResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private TicketsOrderInfo orderInfo;
        private List<TicketInfoList> ticketInfoList;

        public TicketsOrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(TicketsOrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public List<TicketInfoList> getTicketInfoList() {
            return ticketInfoList;
        }

        public void setTicketInfoList(List<TicketInfoList> ticketInfoList) {
            this.ticketInfoList = ticketInfoList;
        }
    }
}
