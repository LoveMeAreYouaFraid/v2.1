package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/26.
 */
public class OfflineOrdersInfo implements Serializable {
    private String orderStatus = "";
    private String orderName = "";
    private double price;
    private String orderId = "";
    private String userId = "";
    private String nickname = "";
    private String avatar;
    private String addTime = "";
    private double tradeCommission;
    private double incomeAmount = 0.0;
    private String score = "";
    private String channel = "ALI";
    private String orderTime = "";
    private String balanceDetailId;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getBalanceDetailId() {
        return balanceDetailId;
    }

    public void setBalanceDetailId(String balanceDetailId) {
        this.balanceDetailId = balanceDetailId;
    }
}
