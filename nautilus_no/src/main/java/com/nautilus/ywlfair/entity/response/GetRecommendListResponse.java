package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 首页信息response
 */
public class GetRecommendListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        @SerializedName("recommendInfoList")
        List<ArticleInfo> recommendInfoList;

        @SerializedName("bannerInfoList")
        List<HomePagerArticleInfo> bannerInfoList;

        public List<ArticleInfo> getArticleInfoList() {
            return recommendInfoList;
        }

        public void setArticleInfoList(List<ArticleInfo> recommendInfoList) {
            this.recommendInfoList = recommendInfoList;
        }

        public List<HomePagerArticleInfo> getBannerInfoList() {
            return bannerInfoList;
        }

        public void setBannerInfoList(List<HomePagerArticleInfo> bannerInfoList) {
            this.bannerInfoList = bannerInfoList;
        }
    }
}
