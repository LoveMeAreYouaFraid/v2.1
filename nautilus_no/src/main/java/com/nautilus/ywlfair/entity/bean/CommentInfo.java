package com.nautilus.ywlfair.entity.bean;

import java.util.List;

public class CommentInfo extends BaseItem {
	private static final long serialVersionUID = 3571664411397800224L;

	private int commentId;

	private List<PicInfo> photos;

	private String content;

	private Author author;

	private int isAt;

	private int toUserId;

	private int itemType;

	private int atUser;

	private int itemId;

	private String addTime;

	private String updateTime;

	private int replyNum;

	private int likeNum;

	private String location;

	private String coordinates;

	private int hasLike;

	private int floor;

	private int delFlag;

    private int replyCommentId;

    private int rating;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private List<ReplyComment> replyCommentList;

    public List<ReplyComment> getReplyCommentList() {
        return replyCommentList;
    }

    public void setReplyCommentList(List<ReplyComment> replyCommentList) {
        this.replyCommentList = replyCommentList;
    }

    public int getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(int replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public List<PicInfo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PicInfo> photos) {
        this.photos = photos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getIsAt() {
        return isAt;
    }

    public void setIsAt(int isAt) {
        this.isAt = isAt;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getAtUser() {
        return atUser;
    }

    public void setAtUser(int atUser) {
        this.atUser = atUser;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
