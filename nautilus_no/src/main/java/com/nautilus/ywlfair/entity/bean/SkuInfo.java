package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/7.
 */
public class SkuInfo implements Serializable {
    private double price;

    private String skuAttrImageUrl;

    private String skuAttrValue;

    private String skuAttrName;

    private int leftNum;

    private int skuId;

    private int postDays;

    public int getPostDays() {
        return postDays;
    }

    public void setPostDays(int postDays) {
        this.postDays = postDays;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSkuAttrImageUrl() {
        return skuAttrImageUrl;
    }

    public void setSkuAttrImageUrl(String skuAttrImageUrl) {
        this.skuAttrImageUrl = skuAttrImageUrl;
    }

    public String getSkuAttrValue() {
        return skuAttrValue;
    }

    public void setSkuAttrValue(String skuAttrValue) {
        this.skuAttrValue = skuAttrValue;
    }

    public String getSkuAttrName() {
        return skuAttrName;
    }

    public void setSkuAttrName(String skuAttrName) {
        this.skuAttrName = skuAttrName;
    }

    public int getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(int leftNum) {
        this.leftNum = leftNum;
    }

    public int getSkuId() {
        return skuId;
    }

    public void setSkuId(int skuId) {
        this.skuId = skuId;
    }
}
