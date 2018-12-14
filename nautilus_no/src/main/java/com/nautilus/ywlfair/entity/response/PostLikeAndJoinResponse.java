package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 点赞和想参加response
 */
public class PostLikeAndJoinResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private int wantJoinNum;

        private int likeNum;

        public int getWantJoinNum() {
            return wantJoinNum;
        }

        public void setWantJoinNum(int wantJoinNum) {
            this.wantJoinNum = wantJoinNum;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }
    }
}
