package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class GoodsRecommendInfo implements Serializable{
    private String imgUrl;

    private String goodsUrl;

    private int goodsId;

    private int hasLike;

    private String goodsName;

    private String goodsDescri;

    private int type;//类型。1表示大图，2表示左侧中图，3表示右侧两个小图

    private List<SkuInfo> skuInfoList;

    public List<SkuInfo> getSkuInfoList() {
        return skuInfoList;
    }

    public void setSkuInfoList(List<SkuInfo> skuInfoList) {
        this.skuInfoList = skuInfoList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsDescri() {
        return goodsDescri;
    }

    public void setGoodsDescri(String goodsDescri) {
        this.goodsDescri = goodsDescri;
    }
}
