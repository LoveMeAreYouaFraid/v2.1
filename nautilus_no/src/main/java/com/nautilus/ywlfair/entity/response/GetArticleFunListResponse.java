package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;
import com.nautilus.ywlfair.entity.bean.NautilusItem;

import java.io.Serializable;
import java.util.List;

/**
 * 玩转市集response
 */
public class GetArticleFunListResponse extends InterfaceResponse implements Serializable {

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
        private List<HomePagerArticleInfo> homePagerArticleInfoList;

        public List<HomePagerArticleInfo> getHomePagerArticleInfoList() {
            return homePagerArticleInfoList;
        }

        public void setHomePagerArticleInfoList(List<HomePagerArticleInfo> homePagerArticleInfoList) {
            this.homePagerArticleInfoList = homePagerArticleInfoList;
        }
    }
}
