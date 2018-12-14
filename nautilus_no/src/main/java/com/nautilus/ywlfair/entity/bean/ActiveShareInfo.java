package com.nautilus.ywlfair.entity.bean;

import android.text.TextUtils;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ActiveShareInfo extends BaseItem implements Serializable {

    private static final long serialVersionUID = 2808812932504562463L;

    private String contentUrl;

    private String actImgUrl;

    private String actDesc;

    private String articleImgUrl;

    private String articleDesc;

    private String title;

    private String desc;

    private String imageUrl;

    private String url;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getActImgUrl() {
        return TextUtils.isEmpty(actImgUrl) ? getArticleImgUrl() : actImgUrl;
    }

    public void setActImgUrl(String actImgUrl) {
        this.actImgUrl = actImgUrl;
    }

    public String getActDesc() {
        return TextUtils.isEmpty(actDesc) ? getArticleDesc() : actDesc;
    }

    public void setActDesc(String actDesc) {
        this.actDesc = actDesc;
    }

    public String getArticleImgUrl() {
        return articleImgUrl;
    }

    public void setArticleImgUrl(String articleImgUrl) {
        this.articleImgUrl = articleImgUrl;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }
}
