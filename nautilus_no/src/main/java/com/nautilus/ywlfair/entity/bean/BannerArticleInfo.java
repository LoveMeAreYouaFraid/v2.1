package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/25.
 */
public class BannerArticleInfo implements Serializable {
    private String recommendTitle;

    private String recommendImageUrl;

    private RecommendInfo recommendInfo;

    public String getRecommendTitle() {
        return recommendTitle;
    }

    public void setRecommendTitle(String recommendTitle) {
        this.recommendTitle = recommendTitle;
    }

    public String getRecommendImageUrl() {
        return recommendImageUrl;
    }

    public void setRecommendImageUrl(String recommendImageUrl) {
        this.recommendImageUrl = recommendImageUrl;
    }

    public RecommendInfo getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(RecommendInfo recommendInfo) {
        this.recommendInfo = recommendInfo;
    }
}
