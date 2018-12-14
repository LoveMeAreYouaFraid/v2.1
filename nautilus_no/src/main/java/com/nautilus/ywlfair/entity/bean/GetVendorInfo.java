package com.nautilus.ywlfair.entity.bean;

import android.net.Uri;

import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;

import java.util.ArrayList;
import java.util.List;

public class GetVendorInfo extends BaseItem {
    private static final long serialVersionUID = 4754276859760851256L;


    private long userId;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String weibo;
    private String weixin;
    private String weixinPicUrl;
    private String alipayAccount;
    private int hasShop;
    private String onlineShopType;
    private String onlineShopName;
    private String onLineShopAddress;
    private String realShopAddress;
    private String realShopCity;
    private int productFrom;
    private String productKind;
    private String cityCode;
    private String productKindName;
    private String productPrice;
    private List<PicInfo> productPicUrls;//返回的时候 图片url 集合
    private String productUrl;//上传的时候 url 用","分割
    private String productBrand;
    private String productLogUrl;
    private int submitStatus;
    private String idCard;
    private double deposit;
    private int depositFlag = 3;//没拿到 就不显示
    private double depositSurplus;

    private List<PicInfo> idCardUrls;

    private ArrayList<Uri> mList;

    private int authType = 1;

    private String company;

    private int invoiceType;

    private int jobType;

    private int hasPayPassword;

    public int getHasPayPassword() {
        return hasPayPassword;
    }

    public void setHasPayPassword(int hasPayPassword) {
        this.hasPayPassword = hasPayPassword;
    }

    private GetVendorInfoResponse.VendorLevelInfo vendorLevelInfo;

    public GetVendorInfoResponse.VendorLevelInfo getVendorLevelInfo() {
        return vendorLevelInfo;
    }

    public void setVendorLevelInfo(GetVendorInfoResponse.VendorLevelInfo vendorLevelInfo) {
        this.vendorLevelInfo = vendorLevelInfo;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getCompany() {
        return company;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWeixinPicUrl() {
        return weixinPicUrl;
    }

    public void setWeixinPicUrl(String weixinPicUrl) {
        this.weixinPicUrl = weixinPicUrl;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public int getHasShop() {
        return hasShop;
    }

    public void setHasShop(int hasShop) {
        this.hasShop = hasShop;
    }

    public String getOnlineShopType() {
        return onlineShopType;
    }

    public void setOnlineShopType(String onlineShopType) {
        this.onlineShopType = onlineShopType;
    }

    public String getOnlineShopName() {
        return onlineShopName;
    }

    public void setOnlineShopName(String onlineShopName) {
        this.onlineShopName = onlineShopName;
    }

    public String getOnLineShopAddress() {
        return onLineShopAddress;
    }

    public void setOnLineShopAddress(String onLineShopAddress) {
        this.onLineShopAddress = onLineShopAddress;
    }

    public String getRealShopAddress() {
        return realShopAddress;
    }

    public void setRealShopAddress(String realShopAddress) {
        this.realShopAddress = realShopAddress;
    }

    public String getRealShopCity() {
        return realShopCity;
    }

    public void setRealShopCity(String realShopCity) {
        this.realShopCity = realShopCity;
    }

    public int getProductFrom() {
        return productFrom;
    }

    public void setProductFrom(int productFrom) {
        this.productFrom = productFrom;
    }

    public String getProductKind() {
        return productKind;
    }

    public void setProductKind(String productKind) {
        this.productKind = productKind;
    }

    public String getProductKindName() {
        return productKindName;
    }

    public void setProductKindName(String productKindName) {
        this.productKindName = productKindName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public List<PicInfo> getProductPicUrls() {
        return productPicUrls;
    }

    public void setProductPicUrls(List<PicInfo> productPicUrls) {
        this.productPicUrls = productPicUrls;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductLogUrl() {
        return productLogUrl;
    }

    public void setProductLogUrl(String productLogUrl) {
        this.productLogUrl = productLogUrl;
    }

    public int getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(int submitStatus) {
        this.submitStatus = submitStatus;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public int getDepositFlag() {
        return depositFlag;
    }

    public void setDepositFlag(int depositFlag) {
        this.depositFlag = depositFlag;
    }

    public double getDepositSurplus() {
        return depositSurplus;
    }

    public void setDepositSurplus(double depositSurplus) {
        this.depositSurplus = depositSurplus;
    }

    public List<PicInfo> getIdCardUrls() {
        return idCardUrls;
    }

    public void setIdCardUrls(List<PicInfo> idCardUrls) {
        this.idCardUrls = idCardUrls;
    }

    public ArrayList<Uri> getmList() {
        return mList;
    }

    public void setmList(ArrayList<Uri> mList) {
        this.mList = mList;
    }
}
