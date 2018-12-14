package com.nautilus.ywlfair.entity.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class NautilusItem extends BaseItem {
    private static final long serialVersionUID = -8696087151847596677L;

    private String actId;
    private String trafficInfo;
    private List<PicInfo> picInfoList;
    private List<VideoInfo> videoInfoList;
    private String organizer;
    private BoothInfo boothInfo;
    private int type;
    private String startTime;
    private String endTime;
    private String addrMap;
    private String listPic;
    private String address;
    private String name;
    private String mapStaticImgUrl;
    private String introduction;
    private PosterInfo posterInfo;
    private String price;
    private int wantJoinNum;
    private int likeNum;
    private int hasLike;
    private int isDrawOpen;
    private List<UserInfo> wantJoinUserInfoList;
    private int hasWantJoin;
    private List<CommentInfo> commentInfoList;

    public int getIsDrawOpen() {
        return isDrawOpen;
    }

    public void setIsDrawOpen(int isDrawOpen) {
        this.isDrawOpen = isDrawOpen;
    }

    private int activityStatus;
    private int isBoothBuy;

    private int isBoothApply;

    private int isDeposit;

    private int isVendor;

    private int isBoothTimeLimit;

    private List<TicketInfo> ticketInfoList;

    private int hasSellOutSubscribe;

    private String boothBuyOrder;

    private String attentionMsg;

    public String getAttentionMsg() {
        return attentionMsg;
    }

    public void setAttentionMsg(String attentionMsg) {
        this.attentionMsg = attentionMsg;
    }

    public String getBoothBuyOrder() {
        return boothBuyOrder;
    }

    public void setBoothBuyOrder(String boothBuyOrder) {
        this.boothBuyOrder = boothBuyOrder;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getTrafficInfo() {
        return trafficInfo;
    }

    public void setTrafficInfo(String trafficInfo) {
        this.trafficInfo = trafficInfo;
    }

    public List<CommentInfo> getCommentInfoList() {
        return commentInfoList;
    }

    public void setCommentInfoList(List<CommentInfo> commentInfoList) {
        this.commentInfoList = commentInfoList;
    }

    public List<UserInfo> getWantJoinUserInfoList() {
        return wantJoinUserInfoList;
    }

    public void setWantJoinUserInfoList(List<UserInfo> wantJoinUserInfoList) {
        wantJoinUserInfoList = wantJoinUserInfoList;
    }

    public List<PicInfo> getPicInfoList() {
        return picInfoList;
    }

    public void setPicInfoList(List<PicInfo> picInfoList) {
        this.picInfoList = picInfoList;
    }

    public List<VideoInfo> getVideoInfoList() {
        return videoInfoList;
    }

    public void setVideoInfoList(List<VideoInfo> videoInfoList) {
        this.videoInfoList = videoInfoList;
    }

    public String getOrganizer() {
        return organizer;
    }


    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public BoothInfo getBoothInfo() {
        return boothInfo;
    }

    public void setBoothInfo(BoothInfo boothInfo) {
        this.boothInfo = boothInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAddrMap() {
        return addrMap;
    }

    public void setAddrMap(String addrMap) {
        this.addrMap = addrMap;
    }

    public String getListPic() {
        return listPic;
    }

    public void setListPic(String listPic) {
        this.listPic = listPic;
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public PosterInfo getPosterInfo() {
        return posterInfo;
    }

    public void setPosterInfo(PosterInfo posterInfo) {
        this.posterInfo = posterInfo;
    }

    public int getWantJoinNum() {
        return wantJoinNum;
    }

    public void setWantJoinNum(int wantJoinNum) {
        this.wantJoinNum = wantJoinNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public List<TicketInfo> getTicketInfoList() {
        return ticketInfoList;
    }

    public void setTicketInfoList(List<TicketInfo> ticketInfoList) {
        this.ticketInfoList = ticketInfoList;
    }

    public String getMapStaticImgUrl() {
        return mapStaticImgUrl;
    }

    public void setMapStaticImgUrl(String mapStaticImgUrl) {
        this.mapStaticImgUrl = mapStaticImgUrl;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getHasWantJoin() {
        return hasWantJoin;
    }

    public void setHasWantJoin(int hasWantJoin) {
        this.hasWantJoin = hasWantJoin;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getIsBoothBuy() {
        return isBoothBuy;
    }

    public void setIsBoothBuy(int isBoothBuy) {
        this.isBoothBuy = isBoothBuy;
    }

    public int getIsBoothApply() {
        return isBoothApply;
    }

    public void setIsBoothApply(int isBoothApply) {
        this.isBoothApply = isBoothApply;
    }

    public int getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(int isDeposit) {
        this.isDeposit = isDeposit;
    }

    public int getIsVendor() {
        return isVendor;
    }

    public void setIsVendor(int isVendor) {
        this.isVendor = isVendor;
    }

    public int getHasSellOutSubscribe() {
        return hasSellOutSubscribe;
    }

    public void setHasSellOutSubscribe(int hasSellOutSubscribe) {
        this.hasSellOutSubscribe = hasSellOutSubscribe;
    }

    public int getIsBoothTimeLimit() {
        return isBoothTimeLimit;
    }

    public void setIsBoothTimeLimit(int isBoothTimeLimit) {
        this.isBoothTimeLimit = isBoothTimeLimit;
    }
}
