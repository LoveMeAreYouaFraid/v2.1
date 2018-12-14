package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ActiveStatusInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.NautilusItem;

import java.io.Serializable;
import java.util.List;

/**
 * 活动状态response
 */
public class GetActiveStatusResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private ActiveStatusInfo statusInfo;

        private HomePagerActivityInfo activityInfo;

        public ActiveStatusInfo getStatusInfo() {
            return statusInfo;
        }

        public void setStatusInfo(ActiveStatusInfo statusInfo) {
            this.statusInfo = statusInfo;
        }

        public HomePagerActivityInfo getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(HomePagerActivityInfo activityInfo) {
            this.activityInfo = activityInfo;
        }
    }
}
