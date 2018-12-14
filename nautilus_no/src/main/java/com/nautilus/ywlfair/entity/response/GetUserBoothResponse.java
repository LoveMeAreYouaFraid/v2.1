package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 用户门票response
 */
public class GetUserBoothResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private List<MyBoothInfo> boothInfoList;

        private NautilusItem activityInfo;

        public NautilusItem getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(NautilusItem activityInfo) {
            this.activityInfo = activityInfo;
        }

        public List<MyBoothInfo> getBoothInfoList() {
            return boothInfoList;
        }

        public void setBoothInfoList(List<MyBoothInfo> boothInfoList) {
            this.boothInfoList = boothInfoList;
        }
    }
}
