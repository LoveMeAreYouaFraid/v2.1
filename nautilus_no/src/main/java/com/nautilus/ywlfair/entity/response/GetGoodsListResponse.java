package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.GoodsRecommendInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品列表response
 */
public class GetGoodsListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        @SerializedName("goodsInfoList")
        private List<GoodsInfo> goodsInfoList;

        public List<GoodsInfo> getGoodsInfoList() {
            return goodsInfoList;
        }

        public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
            this.goodsInfoList = goodsInfoList;
        }
    }
}
