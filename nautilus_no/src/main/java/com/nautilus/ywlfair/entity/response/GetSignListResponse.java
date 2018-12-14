package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.entity.bean.ArticleInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 签到列表
 * Created by Administrator on 2016/4/22.
 */
public class GetSignListResponse extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private int signNum;
        private List<activity> activityInfo;

        public int getSignNum() {
            return signNum;
        }

        public void setSignNum(int signNum) {
            this.signNum = signNum;
        }

        public List<activity> getActivityInfo() {
            return activityInfo;
        }

        public void setActivityInfo(List<activity> activityInfo) {
            this.activityInfo = activityInfo;
        }
    }

    public class activity implements Serializable {
        private String imgUrl;
        private String actId;
        private String status;
        private String address;
        private String name;
        private String addTime;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }
    }
}
