package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 商品详情response
 */
public class GetGoodsDetailResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        @SerializedName("goodsInfo")
        private GoodsInfo goodsInfo;

        public GoodsInfo getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(GoodsInfo goodsInfo) {
            this.goodsInfo = goodsInfo;
        }
    }
}
