package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;

import java.io.Serializable;
import java.util.List;

/**想去的市集
 * Created by Administrator on 2016/4/27.
 */
public class GetMissActivityListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private List<HomePagerActivityInfo> activityRecordList;

        public List<HomePagerActivityInfo> getActivityRecordList() {
            return activityRecordList;
        }

        public void setActivityRecordList(List<HomePagerActivityInfo> activityRecordList) {
            this.activityRecordList = activityRecordList;
        }
    }
}
