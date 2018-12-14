package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

public class BaseItem implements Serializable {
	private static final long serialVersionUID = 646871514154988151L;
	public static final String BASEITEM_KEY_STATUS = "status";
	public static final String BASEITEM_KEY_MESSAGE = "message";
	public static final String BASEITEM_KEY_TIME = "time";
	public static final String BASEITEM_KEY_RESULT = "result";
	
	private long mUptime;
	private String mMessage;
	private int mStatus;
	
	
	public long getUptime() {
		return mUptime;
	}

	public void setUptime(long uptime) {
		this.mUptime = uptime;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int status) {
		this.mStatus = status;
	}
}
