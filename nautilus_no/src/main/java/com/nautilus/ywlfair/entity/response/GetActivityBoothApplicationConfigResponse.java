package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ActivityBoothConfig;
import com.nautilus.ywlfair.entity.bean.ActivityInfo;
import com.nautilus.ywlfair.entity.bean.ActivitySysConfig;
import com.nautilus.ywlfair.entity.bean.ActivitySysConfigInfoList;

import java.io.Serializable;
import java.util.List;

/**
 * 活动报名初始化接收
 * Created by Administrator on 2016/3/3.
 */
public class GetActivityBoothApplicationConfigResponse extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private ActivityInfo activityInfo;
        private ActivityBoothConfig activityBoothConfig;
        private ActivitySysConfigInfoList activitySysConfig;

        public ActivityInfo getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(ActivityInfo activityInfo) {
            this.activityInfo = activityInfo;
        }

        public ActivityBoothConfig getActivityBoothConfig() {
            return activityBoothConfig;
        }

        public void setActivityBoothConfig(ActivityBoothConfig activityBoothConfig) {
            this.activityBoothConfig = activityBoothConfig;
        }

        public ActivitySysConfigInfoList getActivitySysConfig() {
            return activitySysConfig;
        }

        public void setActivitySysConfig(ActivitySysConfigInfoList activitySysConfig) {
            this.activitySysConfig = activitySysConfig;
        }
    }
}


