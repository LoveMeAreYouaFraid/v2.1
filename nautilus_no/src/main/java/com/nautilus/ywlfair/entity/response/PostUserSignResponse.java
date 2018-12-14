package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.SignInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 提交签到response
 */
public class PostUserSignResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private SignInfo signInfo;

        public SignInfo getSignInfo() {
            return signInfo;
        }

        public void setSignInfo(SignInfo signInfo) {
            this.signInfo = signInfo;
        }
    }
}
