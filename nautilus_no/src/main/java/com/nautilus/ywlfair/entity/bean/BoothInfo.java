package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

public class BoothInfo implements Serializable {
    private String type;

    private String boothType;

    private int boothCategory;

    private double boothPrice;

    private String boothMsg;

    private String actName;

    private String actImage;

    private String roundNo;

    private long actStartTime;

    private long actEndTime;

    private long roundapproachStartTime;

    private long roundApproachEndTime;

    private String actVenue;

    private String actAddress;

    private double boothDisCount;

    private String actId;

    private String roundId;

    private String boothId;

    private double vendorScore;

    private double boothScore;

    public double getVendorScore() {
        return vendorScore;
    }

    public void setVendorScore(double vendorScore) {
        this.vendorScore = vendorScore;
    }

    public double getBoothScore() {
        return boothScore;
    }

    public void setBoothScore(double boothScore) {
        this.boothScore = boothScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBoothType() {
        return boothType;
    }

    public void setBoothType(String boothType) {
        this.boothType = boothType;
    }

    public int getBoothCategory() {
        return boothCategory;
    }

    public void setBoothCategory(int boothCategory) {
        this.boothCategory = boothCategory;
    }

    public double getBoothPrice() {
        return boothPrice;
    }

    public void setBoothPrice(double boothPrice) {
        this.boothPrice = boothPrice;
    }

    public String getBoothMsg() {
        return boothMsg;
    }

    public void setBoothMsg(String boothMsg) {
        this.boothMsg = boothMsg;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActImage() {
        return actImage;
    }

    public void setActImage(String actImage) {
        this.actImage = actImage;
    }

    public String getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(String roundNo) {
        this.roundNo = roundNo;
    }

    public long getActStartTime() {
        return actStartTime;
    }

    public void setActStartTime(long actStartTime) {
        this.actStartTime = actStartTime;
    }

    public long getActEndTime() {
        return actEndTime;
    }

    public void setActEndTime(long actEndTime) {
        this.actEndTime = actEndTime;
    }

    public long getRoundapproachStartTime() {
        return roundapproachStartTime;
    }

    public void setRoundapproachStartTime(long roundapproachStartTime) {
        this.roundapproachStartTime = roundapproachStartTime;
    }

    public long getRoundApproachEndTime() {
        return roundApproachEndTime;
    }

    public void setRoundApproachEndTime(long roundApproachEndTime) {
        this.roundApproachEndTime = roundApproachEndTime;
    }

    public String getActVenue() {
        return actVenue;
    }

    public void setActVenue(String actVenue) {
        this.actVenue = actVenue;
    }

    public String getActAddress() {
        return actAddress;
    }

    public void setActAddress(String actAddress) {
        this.actAddress = actAddress;
    }

    public double getBoothDisCount() {
        return boothDisCount;
    }

    public void setBoothDisCount(double boothDisCount) {
        this.boothDisCount = boothDisCount;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getBoothId() {
        return boothId;
    }

    public void setBoothId(String boothId) {
        this.boothId = boothId;
    }
}
