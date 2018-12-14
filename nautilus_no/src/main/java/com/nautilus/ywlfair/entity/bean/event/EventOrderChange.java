package com.nautilus.ywlfair.entity.bean.event;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/14.
 */
public class EventOrderChange implements Serializable {
    private int status;

    private int orderStatus;

    public EventOrderChange(int status, int orderStatus){
        this.status = status;

        this.orderStatus = orderStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
