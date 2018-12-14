package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

public class MessageInfo extends BaseItem {

    private static final long serialVersionUID = 4751656859760851256L;

    private int vendor = 0, shop = 0, normal = 0;

    public int getVendor() {
        return vendor;
    }

    public void setVendor(int vendor) {
        this.vendor = vendor;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    private int type;

    private int subType;

    private int receiver;

    private String content = "暂无消息.";

    private String extendField;

    private int readStatus;

    private String sendTime = "0";

    private Sender sender = new Sender();

    private int messageId;

    private String msgType = "-1";

    private boolean Click = false;

    private boolean ydVendor, ydNormal, ydShop;

    public boolean isYdVendor() {
        return ydVendor;
    }

    public void setYdVendor(boolean ydVendor) {
        this.ydVendor = ydVendor;
    }

    public boolean isYdNormal() {
        return ydNormal;
    }

    public void setYdNormal(boolean ydNormal) {
        this.ydNormal = ydNormal;
    }

    public boolean isYdShop() {
        return ydShop;
    }

    public void setYdShop(boolean ydShop) {
        this.ydShop = ydShop;
    }

    public boolean isClick() {
        return Click;
    }

    public void setClick(boolean click) {
        Click = click;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtendField() {
        return extendField;
    }

    public void setExtendField(String extendField) {
        this.extendField = extendField;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public class Sender implements Serializable {

        private String nickname = "";

        private int userId;

        private String avatar;

        private int userType;//2是官方

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
