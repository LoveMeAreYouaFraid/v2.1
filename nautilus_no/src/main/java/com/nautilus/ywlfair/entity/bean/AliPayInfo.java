package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/25.
 */
public class AliPayInfo implements Serializable {
    private int aliPayType;

    private String account;

    private String name;

    private String certificateNo;

    private String certificateImageUrl;

    public int getAliPayType() {
        return aliPayType;
    }

    public void setAliPayType(int aliPayType) {
        this.aliPayType = aliPayType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getCertificateImageUrl() {
        return certificateImageUrl;
    }

    public void setCertificateImageUrl(String certificateImageUrl) {
        this.certificateImageUrl = certificateImageUrl;
    }
}
