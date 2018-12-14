package com.nautilus.ywlfair.entity.bean;

public class BoothCategory extends BaseItem {
    private static final long serialVersionUID = 1107629763298724300L;

    public final static String TABLE_NAME = "BoothCategory";

    private String exCid;
    private int limit;
    private String boothType;
    private int num;
    private double price;
    private int boothId;
    private int leftNum;
    private double desposit;
    private int boothAlarm;
    private double discount;
    private String desc;


    public double getDesposit() {
        return desposit;
    }

    public void setDesposit(double desposit) {
        this.desposit = desposit;
    }

    public int getBoothAlarm() {
        return boothAlarm;
    }

    public void setBoothAlarm(int boothAlarm) {
        this.boothAlarm = boothAlarm;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExCid() {
        return exCid;
    }

    public void setExCid(String exCid) {
        this.exCid = exCid;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getBoothType() {
        return boothType;
    }

    public void setBoothType(String boothType) {
        this.boothType = boothType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getBoothId() {
        return boothId;
    }

    public void setBoothId(int boothId) {
        this.boothId = boothId;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(int leftNum) {
        this.leftNum = leftNum;
    }
}
