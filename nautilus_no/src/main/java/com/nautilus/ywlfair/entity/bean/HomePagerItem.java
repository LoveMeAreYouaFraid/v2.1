package com.nautilus.ywlfair.entity.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomePagerItem extends BaseItem {
	private static final long serialVersionUID = 1948502056022993823L;

    @SerializedName("articleInfoList")
    private List<HomePagerArticleInfo> articleInfoList;

    @SerializedName("carouselInfoList")
    private List<CarouselInfo> carouselInfoList;

    @SerializedName("activityList")
    private List<HomePagerActivityInfo> activityList;

    public List<HomePagerArticleInfo> getArticleInfoList() {
        return articleInfoList;
    }

    public void setArticleInfoList(List<HomePagerArticleInfo> articleInfoList) {
        this.articleInfoList = articleInfoList;
    }

    public List<CarouselInfo> getCarouselInfoList() {
        return carouselInfoList;
    }

    public void setCarouselInfoList(List<CarouselInfo> carouselInfoList) {
        this.carouselInfoList = carouselInfoList;
    }

    public List<HomePagerActivityInfo> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<HomePagerActivityInfo> activityList) {
        this.activityList = activityList;
    }
}
