package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.BoothActInfo;
import com.nautilus.ywlfair.entity.bean.MyBoothInfo;
import com.nautilus.ywlfair.entity.bean.ShowTextInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 摊位详情response
 */
public class GetBoothDetailResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable{

        @SerializedName("boothInfo")
        private BoothDetailInfo boothDetailInfo;

        @SerializedName("actInfo")
        private BoothActInfo boothActInfo;

        @SerializedName("showText")
        private ShowTextInfo showTextInfo;

        public BoothDetailInfo getBoothDetailInfo() {
            return boothDetailInfo;
        }

        public void setBoothDetailInfo(BoothDetailInfo boothDetailInfo) {
            this.boothDetailInfo = boothDetailInfo;
        }

        public BoothActInfo getBoothActInfo() {
            return boothActInfo;
        }

        public void setBoothActInfo(BoothActInfo boothActInfo) {
            this.boothActInfo = boothActInfo;
        }

        public ShowTextInfo getShowTextInfo() {
            return showTextInfo;
        }

        public void setShowTextInfo(ShowTextInfo showTextInfo) {
            this.showTextInfo = showTextInfo;
        }
    }

    public class BoothDetailInfo implements Serializable{
        private double price;

        private int status;

        private long payTime;

        private String boothNo;

        private String orderId;

        private String roundNo;

        private long approachStartTime;

        private long approachEndTime;

        private int score;

        private String boothType;

        private int boothCategory;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getBoothType() {
            return boothType;
        }

        public void setBoothType(String boothType) {
            this.boothType = boothType;
        }

        public int getBoothCategory() {
            return boothCategory;
        }

        public void setBoothCategory(int boothCategory) {
            this.boothCategory = boothCategory;
        }

        public String getRoundNo() {
            return roundNo;
        }

        public void setRoundNo(String roundNo) {
            this.roundNo = roundNo;
        }

        public long getApproachStartTime() {
            return approachStartTime;
        }

        public void setApproachStartTime(long approachStartTime) {
            this.approachStartTime = approachStartTime;
        }

        public long getApproachEndTime() {
            return approachEndTime;
        }

        public void setApproachEndTime(long approachEndTime) {
            this.approachEndTime = approachEndTime;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getPayTime() {
            return payTime;
        }

        public void setPayTime(long payTime) {
            this.payTime = payTime;
        }

        public String getBoothNo() {
            return boothNo;
        }

        public void setBoothNo(String boothNo) {
            this.boothNo = boothNo;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
