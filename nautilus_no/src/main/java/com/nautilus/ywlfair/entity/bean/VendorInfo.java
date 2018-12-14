package com.nautilus.ywlfair.entity.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VendorInfo extends BaseItem {
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
    private String cityCode;
    private String onlineShopType;
    private String onlineShopName;
    private String onLineShopAddress;
    private String realShopAddress;
    private String realShopCity;
    private int productFrom;
    private String productKind;
    private String productKindName;
    private String productPrice;
    private List<PicInfo> productPicUrls;//返回的时候 图片url 集合
    private String productUrl;//上传的时候 url 用","分割
    private String productBrand;
    private String productLogUrl;
    private int submitStatus;
    private String idCard;
    private int deposit;
    private int depositFlag;
    private int depositSurplus;

    private String addressString;

    private String productKindString;

    private String idCardUrlString;

    private ArrayList<String> mList;

    private ArrayList<String> brandPicList;

    private List<String> goodsInfoSelectPath;

    private List<String> brandLogoSelectPath;

    private Map<Integer, String> idCartPicMap;

    private int authType;

    private String company;

    private int invoiceType;

    private int jobType;

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getCompany() {
        return company;
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

    public Map<Integer, String> getIdCartPicMap() {
        return idCartPicMap;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public void setIdCartPicMap(Map<Integer, String> idCartPicMap) {
        this.idCartPicMap = idCartPicMap;
    }

    public List<String> getBrandLogoSelectPath() {
        return brandLogoSelectPath;
    }

    public void setBrandLogoSelectPath(List<String> brandLogoSelectPath) {
        this.brandLogoSelectPath = brandLogoSelectPath;
    }

    public List<String> getGoodsInfoSelectPath() {
        return goodsInfoSelectPath;
    }

    public void setGoodsInfoSelectPath(List<String> goodsInfoSelectPath) {
        this.goodsInfoSelectPath = goodsInfoSelectPath;
    }

    public String getProductKindString() {
        return productKindString;
    }

    public void setProductKindString(String productKindString) {
        this.productKindString = productKindString;
    }

    public String getIdCardUrlString() {
        return idCardUrlString;
    }

    public void setIdCardUrlString(String idCardUrlString) {
        this.idCardUrlString = idCardUrlString;
    }

    public ArrayList<String> getBrandPicList() {
        return brandPicList;
    }

    public void setBrandPicList(ArrayList<String> brandPicList) {
        this.brandPicList = brandPicList;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public ArrayList<String> getmList() {
        return mList;
    }

    public void setmList(ArrayList<String> mList) {
        this.mList = mList;
    }

    public long getUserId() {
        return userId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getDepositFlag() {
        return depositFlag;
    }

    public void setDepositFlag(int depositFlag) {
        this.depositFlag = depositFlag;
    }

    public int getDepositSurplus() {
        return depositSurplus;
    }

    public void setDepositSurplus(int depositSurplus) {
        this.depositSurplus = depositSurplus;
    }

    public String getRealShopCity() {
        return realShopCity;
    }

    public void setRealShopCity(String realShopCity) {
        this.realShopCity = realShopCity;
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

    public String getProductKindName() {
        return productKindName;
    }

    public void setProductKindName(String productKindName) {
        this.productKindName = productKindName;
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

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }


}
