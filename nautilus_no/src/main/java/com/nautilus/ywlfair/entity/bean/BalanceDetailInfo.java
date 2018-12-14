package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/27.
 */
public class BalanceDetailInfo implements Serializable {
    private int orderType;

    private int balanceType;

    private String orderName;

    private double amount;

    private String orderId;

    private String addTime;

    private double tradeCommission;

    private double walletAmount;

    private int relatedId;

    private double orderAmount;

    private int balanceDetailId;

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getBalanceDetailId() {
        return balanceDetailId;
    }

    public void setBalanceDetailId(int balanceDetailId) {
        this.balanceDetailId = balanceDetailId;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(int balanceType) {
        this.balanceType = balanceType;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public double getTradeCommission() {
        return tradeCommission;
    }

    public void setTradeCommission(double tradeCommission) {
        this.tradeCommission = tradeCommission;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public int getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(int relatedId) {
        this.relatedId = relatedId;
    }
}
