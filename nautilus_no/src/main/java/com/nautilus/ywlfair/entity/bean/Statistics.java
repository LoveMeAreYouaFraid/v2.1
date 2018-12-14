package com.nautilus.ywlfair.entity.bean;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class Statistics extends BaseItem {

	private static final long serialVersionUID = 5212756903121237091L;
	private int commentCount;
	private int messageCount;
	private int orderCount;
	private int actRecordCount;
	private int articleCount;

	public static Statistics createStatistics(JSONObject jsonObject)
			throws JSONException, ParseException {
		Statistics statistics = null;

		if (jsonObject != null) {
			statistics = new Statistics();

			statistics.setCommentCount(jsonObject.getInt("commentCount"));

			statistics.setMessageCount(jsonObject.getInt("messageCount"));

			statistics.setOrderCount(jsonObject.getInt("orderCount"));

			statistics.setActRecordCount(jsonObject.getInt("actRecordCount"));

			statistics.setArticleCount(jsonObject.getInt("articleCount"));

		}

		return statistics;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public int getArticleCount() {
		return articleCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getActRecordCount() {
		return actRecordCount;
	}

	public void setActRecordCount(int actRecordCount) {
		this.actRecordCount = actRecordCount;
	}

	public int setCommentCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

}
