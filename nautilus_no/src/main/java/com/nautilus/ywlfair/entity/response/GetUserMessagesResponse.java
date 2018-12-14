package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.MessageInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 获取用户消息response
 */
public class GetUserMessagesResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{
        private String vendorMsgNum;
        private String normalMsgNum;
        private String shopMsgNum;

        public String getVendorMsgNum() {
            return vendorMsgNum;
        }

        public void setVendorMsgNum(String vendorMsgNum) {
            this.vendorMsgNum = vendorMsgNum;
        }

        public String getNormalMsgNum() {
            return normalMsgNum;
        }

        public void setNormalMsgNum(String normalMsgNum) {
            this.normalMsgNum = normalMsgNum;
        }

        public String getShopMsgNum() {
            return shopMsgNum;
        }

        public void setShopMsgNum(String shopMsgNum) {
            this.shopMsgNum = shopMsgNum;
        }

        private List<MessageInfo> messageList;

        public List<MessageInfo> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<MessageInfo> messageList) {
            this.messageList = messageList;
        }
    }
}
