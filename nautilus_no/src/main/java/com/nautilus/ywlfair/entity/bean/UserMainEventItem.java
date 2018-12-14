package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

public class UserMainEventItem implements Serializable{
	private int type;//1 更换头像 2 修改未读消息
	
	private String avatar;
	
	private int changeCount;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getChangeCount() {
		return changeCount;
	}

	public void setChangeCount(int changeCount) {
		this.changeCount = changeCount;
	}
	
	
}
