package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class GoodsInfo implements Serializable {
    private int goodsId;

    private double price;

    private String goodsUrl;

    private String imageUrl;

    private int hasLike;

    private String vendorNickname;

    private String goodsName;

    private double courierFee;

    private List<SkuInfo> skuInfoList;

    private String shareUrl;

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<SkuInfo> getSkuInfoList() {
        return skuInfoList;
    }

    public void setSkuInfoList(List<SkuInfo> skuInfoList) {
        this.skuInfoList = skuInfoList;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public String getVendorNickname() {
        return vendorNickname;
    }

    public void setVendorNickname(String vendorNickname) {
        this.vendorNickname = vendorNickname;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getCourierFee() {
        return courierFee;
    }

    public void setCourierFee(double courierFee) {
        this.courierFee = courierFee;
    }
}
