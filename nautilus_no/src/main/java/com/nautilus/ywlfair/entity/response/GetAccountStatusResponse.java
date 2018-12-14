package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 账户状态response
 */
public class GetAccountStatusResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        private int PayAccountStatus;

        public int getPayAccountStatus() {
            return PayAccountStatus;
        }

        public void setPayAccountStatus(int payAccountStatus) {
            PayAccountStatus = payAccountStatus;
        }
    }

}
