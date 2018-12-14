package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.SystemPushTag;
import com.nautilus.ywlfair.entity.bean.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 初始化response
 */
public class GetAppInitializationResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result implements Serializable {
        private String deviceId;

        private String actDrawLogUrl;

        private UserInfo userInfo;

        private int userLoginFlag;

        private String actDrawUrl;

        private String calendarUrl;

        private String actMainUrl;

        private int userMsg;

        @SerializedName("sysAccsPushTag")
        private List<SystemPushTag> systemPushTags;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public String getActDrawLogUrl() {
            return actDrawLogUrl;
        }

        public void setActDrawLogUrl(String actDrawLogUrl) {
            this.actDrawLogUrl = actDrawLogUrl;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

        public String getActDrawUrl() {
            return actDrawUrl;
        }

        public void setActDrawUrl(String actDrawUrl) {
            this.actDrawUrl = actDrawUrl;
        }

        public int getUserLoginFlag() {
            return userLoginFlag;
        }

        public void setUserLoginFlag(int userLoginFlag) {
            this.userLoginFlag = userLoginFlag;
        }

        public int getUserMsg() {
            return userMsg;
        }

        public void setUserMsg(int userMsg) {
            this.userMsg = userMsg;
        }

        public List<SystemPushTag> getSystemPushTags() {
            return systemPushTags;
        }

        public void setSystemPushTags(List<SystemPushTag> systemPushTags) {
            this.systemPushTags = systemPushTags;
        }

        public String getCalendarUrl() {
            return calendarUrl;
        }

        public void setCalendarUrl(String calendarUrl) {
            this.calendarUrl = calendarUrl;
        }

        public String getActMainUrl() {
            return actMainUrl;
        }

        public void setActMainUrl(String actMainUrl) {
            this.actMainUrl = actMainUrl;
        }
    }
}
