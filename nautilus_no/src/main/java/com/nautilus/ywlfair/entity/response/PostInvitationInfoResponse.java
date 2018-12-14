package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 登录返回的response
 */
public class PostInvitationInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult {
        String invitationVendorNickname;
        String vendorUserId;

        public String getInvitationVendorNickname() {
            return invitationVendorNickname;
        }

        public void setInvitationVendorNickname(String invitationVendorNickname) {
            this.invitationVendorNickname = invitationVendorNickname;
        }

        public String getVendorUserId() {
            return vendorUserId;
        }

        public void setVendorUserId(String vendorUserId) {
            this.vendorUserId = vendorUserId;
        }
    }
}
