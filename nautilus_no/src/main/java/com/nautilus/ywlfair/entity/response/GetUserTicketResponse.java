package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.EntranceTicket;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;

import java.io.Serializable;
import java.util.List;

/**
 * 我的活动response
 */
public class GetUserTicketResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        @SerializedName("ticketInfoList")
        List<TicketInfoList> ticketInfoList;

        public List<TicketInfoList> getTicketInfoList() {
            return ticketInfoList;
        }

        public void setTicketInfoList(List<TicketInfoList> ticketInfoList) {
            this.ticketInfoList = ticketInfoList;
        }
    }
}
