package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 活动分享信息response
 */
public class GetShareInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{
        private ActiveShareInfo shareInfo;

        public ActiveShareInfo getActiveShareInfo() {
            return shareInfo;
        }

        public void setActiveShareInfo(ActiveShareInfo activeShareInfo) {
            this.shareInfo = activeShareInfo;
        }
    }
}
