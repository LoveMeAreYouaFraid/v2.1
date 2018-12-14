package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ScoreInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 积分明细列表response
 */
public class GetScoreListResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable {

        @SerializedName("scoreDetailList")
        private List<ScoreInfo> scoreInfoList;

        private VendorScoreInfo vendorScoreInfo;

        public VendorScoreInfo getVendorScoreInfo() {
            return vendorScoreInfo;
        }

        public void setVendorScoreInfo(VendorScoreInfo vendorScoreInfo) {
            this.vendorScoreInfo = vendorScoreInfo;
        }

        public List<ScoreInfo> getScoreInfoList() {
            return scoreInfoList;
        }

        public void setScoreInfoList(List<ScoreInfo> scoreInfoList) {
            this.scoreInfoList = scoreInfoList;
        }
    }

    public class VendorScoreInfo{
        private String scoreHelpUrl;

        private int score;

        public String getScoreHelpUrl() {
            return scoreHelpUrl;
        }

        public void setScoreHelpUrl(String scoreHelpUrl) {
            this.scoreHelpUrl = scoreHelpUrl;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}
