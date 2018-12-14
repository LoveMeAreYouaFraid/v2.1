package com.nautilus.ywlfair.entity.bean;

/**
 * Created by Administrator on 2016/3/3.
 */
public class BoothActInfo extends BaseItem  {

    private String posterMain;

    private String boothType;

    private double price;

    private long startdate;

    private long enddate;

    private String address;

    private String name;

    private int actId;

    private long approachStartTime;

    private long approachEndTime;

    public long getApproachStartTime() {
        return approachStartTime;
    }

    public void setApproachStartTime(long approachStartTime) {
        this.approachStartTime = approachStartTime;
    }

    public long getApproachEndTime() {
        return approachEndTime;
    }

    public void setApproachEndTime(long approachEndTime) {
        this.approachEndTime = approachEndTime;
    }

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public String getPosterMain() {
        return posterMain;
    }

    public void setPosterMain(String posterMain) {
        this.posterMain = posterMain;
    }

    public String getBoothType() {
        return boothType;
    }

    public void setBoothType(String boothType) {
        this.boothType = boothType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
