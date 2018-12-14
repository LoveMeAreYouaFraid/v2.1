package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 收款二维码response
 */
public class GetQrCodeResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        private OfflinePayQrCode offlinePayQrCode;

        public OfflinePayQrCode getOfflinePayQrCode() {
            return offlinePayQrCode;
        }

        public void setOfflinePayQrCode(OfflinePayQrCode offlinePayQrCode) {
            this.offlinePayQrCode = offlinePayQrCode;
        }
    }

    public class OfflinePayQrCode{

        private String offlinePayHelpUrl;

        private String qrCodeUrl;

        public String getOfflinePayHelpUrl() {
            return offlinePayHelpUrl;
        }

        public void setOfflinePayHelpUrl(String offlinePayHelpUrl) {
            this.offlinePayHelpUrl = offlinePayHelpUrl;
        }

        public String getQrCodeUrl() {
            return qrCodeUrl;
        }

        public void setQrCodeUrl(String qrCodeUrl) {
            this.qrCodeUrl = qrCodeUrl;
        }
    }
}
