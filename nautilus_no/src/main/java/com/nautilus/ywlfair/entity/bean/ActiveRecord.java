package com.nautilus.ywlfair.entity.bean;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class ActiveRecord extends BaseItem {

	private static final long serialVersionUID = 6888089353232616687L;
	
	private int recordType;
	
	private int hasTicket;
	
	private int hasSigned;
	
	private int hasBooth;

    private int ticketNum;
	
	private NautilusItem activityInfo;
	
	public static ActiveRecord createActiveRecord(JSONObject jsonObject) throws JSONException, ParseException{
		ActiveRecord activeRecord = null;
		
		if(jsonObject != null){
			activeRecord = new ActiveRecord();
			
//			activeRecord.setRecordType(jsonObject.getLong("recordType"));
//
//			activeRecord.setHasTicket(jsonObject.getLong("hasTicket"));
//
//			activeRecord.setHasSigned(jsonObject.getLong("hasSigned"));
//
//			activeRecord.setHasBooth(jsonObject.getLong("hasBooth"));
		}
		
		return activeRecord;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setRecordType(int recordType) {
		this.recordType = recordType;
	}

	public int getHasTicket() {
		return hasTicket;
	}

	public void setHasTicket(int hasTicket) {
		this.hasTicket = hasTicket;
	}

	public int getHasSigned() {
		return hasSigned;
	}

	public void setHasSigned(int hasSigned) {
		this.hasSigned = hasSigned;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getHasBooth() {
		return hasBooth;
	}

	public void setHasBooth(int hasBooth) {
		this.hasBooth = hasBooth;
	}

	public NautilusItem getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(NautilusItem activityInfo) {
		this.activityInfo = activityInfo;
	}

    public int getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(int ticketNum) {
        this.ticketNum = ticketNum;
    }
}
