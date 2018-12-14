package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 摊位列表response
 */
public class GetBoothListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        @SerializedName("list")
        private List<MyBoothInfo> myBoothInfoList;

        private int count;

        public List<MyBoothInfo> getMyBoothInfoList() {
            return myBoothInfoList;
        }

        public void setMyBoothInfoList(List<MyBoothInfo> myBoothInfoList) {
            this.myBoothInfoList = myBoothInfoList;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
