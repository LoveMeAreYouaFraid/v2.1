package com.nautilus.ywlfair.entity.bean.event;

import java.io.Serializable;

public class EventProductType implements Serializable{
	
	private static final long serialVersionUID = 423077434969497548L;

	private String category;
	
	private String typeId;
	
	public EventProductType(String category, String typeId){
		this.category = category;
		
		this.typeId = typeId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	
}
