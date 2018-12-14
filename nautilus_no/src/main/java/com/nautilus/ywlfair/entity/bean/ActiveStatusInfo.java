package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ActiveStatusInfo implements Serializable {
    private int isDrawOpen;

    private int hasLike;

    private int hasSellOutSubscribe;

    private int activityStatus;

    private int ticketStatus;

    private String actId;

    private int isBoothBuy;

    private int isBoothApply;

    private int isDeposit;

    private int isBoothTimeLimit;

    private String boothOrderId;

    public String getBoothOrderId() {
        return boothOrderId;
    }

    public void setBoothOrderId(String boothOrderId) {
        this.boothOrderId = boothOrderId;
    }

    public int getIsBoothBuy() {
        return isBoothBuy;
    }

    public void setIsBoothBuy(int isBoothBuy) {
        this.isBoothBuy = isBoothBuy;
    }

    public int getIsBoothApply() {
        return isBoothApply;
    }

    public void setIsBoothApply(int isBoothApply) {
        this.isBoothApply = isBoothApply;
    }

    public int getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(int isDeposit) {
        this.isDeposit = isDeposit;
    }

    public int getIsBoothTimeLimit() {
        return isBoothTimeLimit;
    }

    public void setIsBoothTimeLimit(int isBoothTimeLimit) {
        this.isBoothTimeLimit = isBoothTimeLimit;
    }

    public int getIsDrawOpen() {
        return isDrawOpen;
    }

    public void setIsDrawOpen(int isDrawOpen) {
        this.isDrawOpen = isDrawOpen;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getHasSellOutSubscribe() {
        return hasSellOutSubscribe;
    }

    public void setHasSellOutSubscribe(int hasSellOutSubscribe) {
        this.hasSellOutSubscribe = hasSellOutSubscribe;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }
}
