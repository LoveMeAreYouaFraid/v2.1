package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 点赞列表
 * Created by Administrator on 2016/4/22.
 */
public class GetPraiseUserListResponse extends InterfaceResponse implements Serializable {
    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable {

        private List<PraiseUser> userList;

        public List<PraiseUser> getUserList() {
            return userList;
        }

        public void setUserList(List<PraiseUser> userList) {
            this.userList = userList;
        }
    }

    public class PraiseUser implements Serializable {
        private String nickname;

        private long addTime;

        private String avatar;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
