package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.CommentInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 活动评论列表response
 */
public class GetActivityCommentsResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        private List<CommentInfo> commentInfoList;

        public List<CommentInfo> getCommentInfoList() {
            return commentInfoList;
        }

        public void setCommentInfoList(List<CommentInfo> commentInfoList) {
            this.commentInfoList = commentInfoList;
        }
    }
}
