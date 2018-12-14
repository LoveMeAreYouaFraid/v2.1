package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/17.
 */
public class GutTicketCodeStatusResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {
        private int codeStatus;

        public int getCodeStatus() {
            return codeStatus;
        }

        public void setCodeStatus(int codeStatus) {
            this.codeStatus = codeStatus;
        }
    }
}
