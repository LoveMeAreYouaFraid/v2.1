package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.ActiveRecord;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 我的活动response
 */
public class GetUserRecordResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        @SerializedName("activityRecordList")
        private List<ActiveRecord> activeRecordList;

        public List<ActiveRecord> getActiveRecordList() {
            return activeRecordList;
        }

        public void setActiveRecordList(List<ActiveRecord> activeRecordList) {
            this.activeRecordList = activeRecordList;
        }
    }
}
