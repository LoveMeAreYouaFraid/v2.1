package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/25.
 */
public class GetUserActivityResponse extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private String count;


        @SerializedName("activityInfoList")
        private List<HomePagerActivityInfo> activityInfoList;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public List<HomePagerActivityInfo> getActivityInfoList() {
            return activityInfoList;
        }

        public void setActivityInfoList(List<HomePagerActivityInfo> activityInfoList) {
            this.activityInfoList = activityInfoList;
        }
    }


}
