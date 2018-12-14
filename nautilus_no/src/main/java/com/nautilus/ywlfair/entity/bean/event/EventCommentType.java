package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.CommentInfo;

import java.io.Serializable;

public class EventCommentType implements Serializable{

	private static final long serialVersionUID = 423077434969497548L;

	private int type;//0 发表新的评论  1 查看评论界赞数改变  2 回复数改变

	private CommentInfo commentInfo;

    private int changeNum;

	public EventCommentType(CommentInfo commentInfo, int type, int changeNum){
		this.type = type;
		
		this.commentInfo = commentInfo;

        this.changeNum = changeNum;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CommentInfo getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        this.commentInfo = commentInfo;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }
}
