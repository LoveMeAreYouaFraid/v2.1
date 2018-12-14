package com.nautilus.ywlfair.entity.bean;

/**
 * Created by Administrator on 2016/3/3.
 */
public class ActivityBoothConfig extends BaseItem {
    private String  actId;
    private String   attentionMsg;
    private String   feeMsg;
    private String addrMsg;
    private String weixinMsg;
    private String actName;
    private String id;
    private String roundNo;
    private String roundId;

    public String getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(String roundNo) {
        this.roundNo = roundNo;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getAttentionMsg() {
        return attentionMsg;
    }

    public void setAttentionMsg(String attentionMsg) {
        this.attentionMsg = attentionMsg;
    }

    public String getFeeMsg() {
        return feeMsg;
    }

    public void setFeeMsg(String feeMsg) {
        this.feeMsg = feeMsg;
    }

    public String getAddrMsg() {
        return addrMsg;
    }

    public void setAddrMsg(String addrMsg) {
        this.addrMsg = addrMsg;
    }

    public String getWeixinMsg() {
        return weixinMsg;
    }

    public void setWeixinMsg(String weixinMsg) {
        this.weixinMsg = weixinMsg;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
