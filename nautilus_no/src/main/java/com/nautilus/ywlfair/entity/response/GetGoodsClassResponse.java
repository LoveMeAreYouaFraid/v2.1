package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.GoodsRecommendInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品分类response
 */
public class GetGoodsClassResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{
        @SerializedName("goodsClassInfoList")
        private List<GoodsClassInfo> goodsClassInfoList;

        public List<GoodsClassInfo> getGoodsClassInfoList() {
            return goodsClassInfoList;
        }

        public void setGoodsClassInfoList(List<GoodsClassInfo> goodsClassInfoList) {
            this.goodsClassInfoList = goodsClassInfoList;
        }
    }
}
