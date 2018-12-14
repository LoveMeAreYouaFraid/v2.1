package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/27.
 */
public class WalletInfo implements Serializable {
    private double amount;

    private double withdrawAmount;

    private String qrCodeUrl;

    private int hasPayPassword;

    private int aliPayBindStatus;

    private AliPayInfo aliPayInfo;

    private String walletHelpUrl;

    public String getWalletHelpUrl() {
        return walletHelpUrl;
    }

    public void setWalletHelpUrl(String walletHelpUrl) {
        this.walletHelpUrl = walletHelpUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public int getHasPayPassword() {
        return hasPayPassword;
    }

    public void setHasPayPassword(int hasPayPassword) {
        this.hasPayPassword = hasPayPassword;
    }

    public int getAliPayBindStatus() {
        return aliPayBindStatus;
    }

    public void setAliPayBindStatus(int aliPayBindStatus) {
        this.aliPayBindStatus = aliPayBindStatus;
    }

    public AliPayInfo getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(AliPayInfo aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }
}
