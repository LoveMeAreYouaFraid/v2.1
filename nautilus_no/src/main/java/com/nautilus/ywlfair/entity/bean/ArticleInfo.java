package com.nautilus.ywlfair.entity.bean;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleInfo extends BaseItem{

	private static final long serialVersionUID = -5103806660426073854L;

	private String imgUrl;
	
	private String title;
	
	private String dateTime;
	
	private long commentNum;
	
	private int likeNum;
	
	private String articleUrl;
	
	private long articleId;
	
	private int hasLike;

    private String editorName;

    private String desc;

    private String tagName;

    private int tag;
    //文章类型。1：原创文章；2：推荐文章
    private int articleType;

    public int getArticleType() {
        return articleType;
    }

    public void setArticleType(int articleType) {
        this.articleType = articleType;
    }

    public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getHasLike() {
		return hasLike;
	}

	public void setHasLike(int hasLike) {
		this.hasLike = hasLike;
	}

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }
}
