package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.entity.bean.BannerArticleInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 推荐和原创列表response
 */
public class GetArticleAndRecommendResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        @SerializedName("articleInfoList")
        List<ArticleInfo> articleInfoList;

        @SerializedName("bannerInfoList")
        List<BannerArticleInfo> bannerInfoList;

        public List<BannerArticleInfo> getBannerInfoList() {
            return bannerInfoList;
        }

        public void setBannerInfoList(List<BannerArticleInfo> bannerInfoList) {
            this.bannerInfoList = bannerInfoList;
        }

        public List<ArticleInfo> getArticleInfoList() {
            return articleInfoList;
        }

        public void setArticleInfoList(List<ArticleInfo> articleInfoList) {
            this.articleInfoList = articleInfoList;
        }
    }
}
