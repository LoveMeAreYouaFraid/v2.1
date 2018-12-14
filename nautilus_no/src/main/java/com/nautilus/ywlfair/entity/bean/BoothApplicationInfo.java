package com.nautilus.ywlfair.entity.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lp on 2016/3/5.
 */
public class BoothApplicationInfo implements Serializable {
    private String userId = "",
            activityId = "",
            name = "",
            phone = "",
            weixin = "",
            stuffStyle = "",
            stuffStyleText = "",
            stuffKinds = "",
            stuffKindsText = "",
            foodCertPicUrl = "",
            foodElecMsg = "",
            shelfNum = "0",
            intrMsg = "",
            stuffPicUrl = "",
            largeGroundUrl = "",
            stuffPriceArea = "",
            stuffPriceAreaText = "",
            realshopFlag = "",
            realshopFlagText = "",
            logoUrl = "",
            specNeeds = "",
            mediaResMsg = "",
            citySurveyMsg = "",
            citySurveyMsgText = "",
            boothBuyType = "";
    private String roundId;
    private ArrayList<String> mSelectPath;
    private ArrayList<String> foodlist, goodsPicList, bigPicList;

    private ArrayList<String> foodUriList, goodsUriList, bigPicUriList;

    private List<AddViewClassInfo> addViewClassInf;

    private int foodPicNum, goodsPicNum, bigPicNum;

    public ArrayList<String> getmSelectPath() {
        return mSelectPath;
    }

    public void setmSelectPath(ArrayList<String> mSelectPath) {
        this.mSelectPath = mSelectPath;
    }

    public List<AddViewClassInfo> getAddViewClassInf() {
        return addViewClassInf;
    }

    public void setAddViewClassInf(List<AddViewClassInfo> addViewClassInf) {
        this.addViewClassInf = addViewClassInf;
    }

    public ArrayList<String> getFoodUriList() {
        return foodUriList;
    }

    public void setFoodUriList(ArrayList<String> foodUriList) {
        this.foodUriList = foodUriList;
    }

    public ArrayList<String> getGoodsUriList() {
        return goodsUriList;
    }

    public void setGoodsUriList(ArrayList<String> goodsUriList) {
        this.goodsUriList = goodsUriList;
    }

    public ArrayList<String> getBigPicUriList() {
        return bigPicUriList;
    }

    public void setBigPicUriList(ArrayList<String> bigPicUriList) {
        this.bigPicUriList = bigPicUriList;
    }

    public ArrayList<String> getFoodlist() {
        return foodlist;
    }

    public void setFoodlist(ArrayList<String> foodlist) {
        this.foodlist = foodlist;
    }

    public ArrayList<String> getGoodsPicList() {
        return goodsPicList;
    }

    public void setGoodsPicList(ArrayList<String> goodsPicList) {
        this.goodsPicList = goodsPicList;
    }

    public ArrayList<String> getBigPicList() {
        return bigPicList;
    }

    public void setBigPicList(ArrayList<String> bigPicList) {
        this.bigPicList = bigPicList;
    }

    public int getFoodPicNum() {
        return foodPicNum;
    }

    public void setFoodPicNum(int foodPicNum) {
        this.foodPicNum = foodPicNum;
    }

    public int getGoodsPicNum() {
        return goodsPicNum;
    }

    public void setGoodsPicNum(int goodsPicNum) {
        this.goodsPicNum = goodsPicNum;
    }

    public int getBigPicNum() {
        return bigPicNum;
    }

    public void setBigPicNum(int bigPicNum) {
        this.bigPicNum = bigPicNum;
    }

    private List<ArrayList<UpLoadPicInfo>> upLoadPicSet;

    public List<ArrayList<UpLoadPicInfo>> getUpLoadPicSet() {
        return upLoadPicSet;
    }

    public void setUpLoadPicSet(List<ArrayList<UpLoadPicInfo>> upLoadPicSet) {
        this.upLoadPicSet = upLoadPicSet;
    }

    private List<UpLoadPicInfo> foodUris;

    private List<UpLoadPicInfo> goodsUris;

    private List<UpLoadPicInfo> bigUris;

    public List<UpLoadPicInfo> getFoodUris() {
        return foodUris;
    }

    public void setFoodUris(List<UpLoadPicInfo> foodUris) {
        this.foodUris = foodUris;
    }

    public List<UpLoadPicInfo> getGoodsUris() {
        return goodsUris;
    }

    public void setGoodsUris(List<UpLoadPicInfo> goodsUris) {
        this.goodsUris = goodsUris;
    }

    public List<UpLoadPicInfo> getBigUris() {
        return bigUris;
    }

    public void setBigUris(List<UpLoadPicInfo> bigUris) {
        this.bigUris = bigUris;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getStuffStyle() {
        return stuffStyle;
    }

    public void setStuffStyle(String stuffStyle) {
        this.stuffStyle = stuffStyle;
    }

    public String getStuffStyleText() {
        return stuffStyleText;
    }

    public void setStuffStyleText(String stuffStyleText) {
        this.stuffStyleText = stuffStyleText;
    }

    public String getStuffKinds() {
        return stuffKinds;
    }

    public void setStuffKinds(String stuffKinds) {
        this.stuffKinds = stuffKinds;
    }

    public String getStuffKindsText() {
        return stuffKindsText;
    }

    public void setStuffKindsText(String stuffKindsText) {
        this.stuffKindsText = stuffKindsText;
    }

    public String getFoodCertPicUrl() {
        return foodCertPicUrl;
    }

    public void setFoodCertPicUrl(String foodCertPicUrl) {
        this.foodCertPicUrl = foodCertPicUrl;
    }

    public String getFoodElecMsg() {
        return foodElecMsg;
    }

    public void setFoodElecMsg(String foodElecMsg) {
        this.foodElecMsg = foodElecMsg;
    }

    public String getShelfNum() {
        return shelfNum;
    }

    public void setShelfNum(String shelfNum) {
        this.shelfNum = shelfNum;
    }

    public String getIntrMsg() {
        return intrMsg;
    }

    public void setIntrMsg(String intrMsg) {
        this.intrMsg = intrMsg;
    }

    public String getStuffPicUrl() {
        return stuffPicUrl;
    }

    public void setStuffPicUrl(String stuffPicUrl) {
        this.stuffPicUrl = stuffPicUrl;
    }

    public String getLargeGroundUrl() {
        return largeGroundUrl;
    }

    public void setLargeGroundUrl(String largeGroundUrl) {
        this.largeGroundUrl = largeGroundUrl;
    }

    public String getStuffPriceArea() {
        return stuffPriceArea;
    }

    public void setStuffPriceArea(String stuffPriceArea) {
        this.stuffPriceArea = stuffPriceArea;
    }

    public String getStuffPriceAreaText() {
        return stuffPriceAreaText;
    }

    public void setStuffPriceAreaText(String stuffPriceAreaText) {
        this.stuffPriceAreaText = stuffPriceAreaText;
    }

    public String getRealshopFlag() {
        return realshopFlag;
    }

    public void setRealshopFlag(String realshopFlag) {
        this.realshopFlag = realshopFlag;
    }

    public String getRealshopFlagText() {
        return realshopFlagText;
    }

    public void setRealshopFlagText(String realshopFlagText) {
        this.realshopFlagText = realshopFlagText;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSpecNeeds() {
        return specNeeds;
    }

    public void setSpecNeeds(String specNeeds) {
        this.specNeeds = specNeeds;
    }

    public String getMediaResMsg() {
        return mediaResMsg;
    }

    public void setMediaResMsg(String mediaResMsg) {
        this.mediaResMsg = mediaResMsg;
    }

    public String getCitySurveyMsg() {
        return citySurveyMsg;
    }

    public void setCitySurveyMsg(String citySurveyMsg) {
        this.citySurveyMsg = citySurveyMsg;
    }

    public String getCitySurveyMsgText() {
        return citySurveyMsgText;
    }

    public void setCitySurveyMsgText(String citySurveyMsgText) {
        this.citySurveyMsgText = citySurveyMsgText;
    }

    public String getBoothBuyType() {
        return boothBuyType;
    }

    public void setBoothBuyType(String boothBuyType) {
        this.boothBuyType = boothBuyType;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }
}
