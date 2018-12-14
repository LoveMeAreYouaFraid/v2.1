package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;
import com.nautilus.ywlfair.entity.bean.TicketListItem;

import java.io.Serializable;
import java.util.List;

/**
 * 活动门票列表response
 */
public class GetTicketListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        private List<TicketListItem> ticketInfo;

        public List<TicketListItem> getTicketInfo() {
            return ticketInfo;
        }

        public void setTicketInfo(List<TicketListItem> ticketInfo) {
            this.ticketInfo = ticketInfo;
        }
    }
}
