package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 获取用户原创response
 */
public class GetUserArticlesResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private List<ArticleInfo> articleInfoList;

        public List<ArticleInfo> getArticleInfoList() {
            return articleInfoList;
        }

        public void setArticleInfoList(List<ArticleInfo> articleInfoList) {
            this.articleInfoList = articleInfoList;
        }
    }
}
