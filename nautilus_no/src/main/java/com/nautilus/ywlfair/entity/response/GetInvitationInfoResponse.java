package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.InvitationInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;

import java.io.Serializable;

/**
 * 登录返回的response
 */
public class GetInvitationInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult {
        private InvitationInfo invitationInfo;

        public InvitationInfo getInvitationInfo() {
            return invitationInfo;
        }

        public void setInvitationInfo(InvitationInfo invitationInfo) {
            this.invitationInfo = invitationInfo;
        }
    }
}
