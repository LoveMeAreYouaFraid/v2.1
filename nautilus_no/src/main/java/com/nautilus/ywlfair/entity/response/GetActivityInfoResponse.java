package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 活动详情response
 */
public class GetActivityInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        @SerializedName("activityInfo")
        private NautilusItem nautilusItem;

        public NautilusItem getNautilusItem() {
            return nautilusItem;
        }

        public void setNautilusItem(NautilusItem nautilusItem) {
            this.nautilusItem = nautilusItem;
        }

    }
}
