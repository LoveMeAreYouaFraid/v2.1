package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.GetVendorInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.VendorInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.VendorLevel;

import java.io.Serializable;
import java.util.List;

/**
 * 摊主信息response
 */
public class GetVendorInfoResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private GetVendorInfo vendor;

        private UserInfo userInfo;

        public GetVendorInfo getVendor() {
            return vendor;
        }

        public void setVendor(GetVendorInfo vendor) {
            this.vendor = vendor;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }

    }

    public class VendorLevelInfo implements Serializable{
        private int level;

        private String levelName;

        private int privilegeCount;

        private int prevLevel;

        private String prevLevelName;

        private int nextLevel;

        private String nextLevelName;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public int getPrivilegeCount() {
            return privilegeCount;
        }

        public void setPrivilegeCount(int privilegeCount) {
            this.privilegeCount = privilegeCount;
        }

        public int getPrevLevel() {
            return prevLevel;
        }

        public void setPrevLevel(int prevLevel) {
            this.prevLevel = prevLevel;
        }

        public String getPrevLevelName() {
            return prevLevelName;
        }

        public void setPrevLevelName(String prevLevelName) {
            this.prevLevelName = prevLevelName;
        }

        public int getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(int nextLevel) {
            this.nextLevel = nextLevel;
        }

        public String getNextLevelName() {
            return nextLevelName;
        }

        public void setNextLevelName(String nextLevelName) {
            this.nextLevelName = nextLevelName;
        }
    }
}
