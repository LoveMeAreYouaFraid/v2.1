package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.AliPayInfo;
import com.nautilus.ywlfair.entity.bean.CommentInfo;

import java.io.Serializable;

/**
 * 绑定支付宝response
 */
public class PostBindAliPayResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private int aliPayBindStatus;

        private AliPayInfo aliPayInfo;

        public int getAliPayBindStatus() {
            return aliPayBindStatus;
        }

        public void setAliPayBindStatus(int aliPayBindStatus) {
            this.aliPayBindStatus = aliPayBindStatus;
        }

        public AliPayInfo getAliPayInfo() {
            return aliPayInfo;
        }

        public void setAliPayInfo(AliPayInfo aliPayInfo) {
            this.aliPayInfo = aliPayInfo;
        }
    }
}
