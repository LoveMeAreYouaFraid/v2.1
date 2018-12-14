package com.nautilus.ywlfair.entity.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class TicketInfo extends BaseItem {
	private static final long serialVersionUID = -5006555328592257305L;
	private String exCid;
	private int limit;
	private int num;
	private String desc;
	private double price;
	private int ticketId;
	private String ticketName;
	private int leftNum;
	private String type;
	

	public String getExCid() {
		return exCid;
	}

	public void setExCid(String exCid) {
		this.exCid = exCid;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketName() {
		return ticketName;
	}

	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
