package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/28.
 */
public class TicketInfoList implements Serializable {
    private String ticketCode;
    private String ticketImgUrl;
    private String NumberOfPerTicket;
    private String status = "2";

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getTicketImgUrl() {
        return ticketImgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTicketImgUrl(String ticketImgUrl) {
        this.ticketImgUrl = ticketImgUrl;
    }

    public String getNumberOfPerTicket() {
        return NumberOfPerTicket;
    }

    public void setNumberOfPerTicket(String numberOfPerTicket) {
        NumberOfPerTicket = numberOfPerTicket;
    }
}
