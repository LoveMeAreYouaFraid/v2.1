package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by lipeng on 2016/3/29.
 */
public class DepositInfo extends BaseItem {
//    "price": 0.01,
//            "payTime": "1453103286000",
//            "orderId": "43E2C6CD96AA341489",
//            //交押金状态。0：可交押金；1:已交押金可申请退款；2：已申请退款；3:已锁定
//            “depositStatus”:0,
//            //剩余押金
//            “surplusAmount”:0,
//            //扣款记录
//            “deductionsNum”: 0,
//            //支付方式
//            “payChannel”:”WX”


    private double price;
    private String payTime;
    private String orderId;
    private int depositStatus;
    private String surplusAmount;
    private String deductionsNum;
    private String payChannel;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(int depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getDeductionsNum() {
        return deductionsNum;
    }

    public void setDeductionsNum(String deductionsNum) {
        this.deductionsNum = deductionsNum;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }
}
