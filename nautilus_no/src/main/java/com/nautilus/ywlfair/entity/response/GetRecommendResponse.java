package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.RecommendInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 获取推荐列表response
 */
public class GetRecommendResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{
        private List<RecommendInfo> recommendInfoList;

        public List<RecommendInfo> getRecommendInfoList() {
            return recommendInfoList;
        }

        public void setRecommendInfoList(List<RecommendInfo> recommendInfoList) {
            this.recommendInfoList = recommendInfoList;
        }
    }
}
