package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 活动列表response
 */
public class GetActivitiesResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private List<NautilusItem> activityInfoList;

        public List<NautilusItem> getActivityInfoList() {
            return activityInfoList;
        }

        public void setActivityInfoList(List<NautilusItem> activityInfoList) {
            this.activityInfoList = activityInfoList;
        }
    }
}
