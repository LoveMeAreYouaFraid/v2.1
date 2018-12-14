package com.nautilus.ywlfair.entity.bean;

/**
 * Created by Administrator on 2016/1/23.
 */
public class DepositInfo extends BaseItem {

    //    "id": 135,
//            "deductionsNum": 0,
//            "surplusAmount": 0.01,
//            "price": 0.01,
//            "payTime": 1459327072000,
//            "payChannel": "ALI",
//            "orderId": "CA5BEE28FDD564612",
//            "depositStatus": 1
    private int id;

    private double price;

    private String payTime;

    private String orderId;

    private int depositStatus;

    private double surplusAmount;

    private int deductionsNum;

    private String payChannel;

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public double getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(double surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public int getDeductionsNum() {
        return deductionsNum;
    }

    public void setDeductionsNum(int deductionsNum) {
        this.deductionsNum = deductionsNum;
    }
}
