package com.nautilus.ywlfair.entity.bean;

public class SignInfo extends BaseItem{

	private static final long serialVersionUID = -6227301521592488372L;

    private int signNum;

	private long hasSigned;
	
	private String signDesc;
	
	private String location;
	
	private String coordinates;
	
	private String signCheckLocation;
	
	private String signTime;
	
	private SignActiveInfo activityInfo;

    private String signRule;

    public int getSignNum() {
        return signNum;
    }

    public void setSignNum(int signNum) {
        this.signNum = signNum;
    }

    public long getHasSigned() {
        return hasSigned;
    }

    public void setHasSigned(long hasSigned) {
        this.hasSigned = hasSigned;
    }

    public String getSignDesc() {
        return signDesc;
    }

    public void setSignDesc(String signDesc) {
        this.signDesc = signDesc;
    }

    public String getLoaction() {
        return location;
    }

    public void setLoaction(String loaction) {
        this.location = loaction;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getSignCheckLocation() {
        return signCheckLocation;
    }

    public void setSignCheckLocation(String signCheckLocation) {
        this.signCheckLocation = signCheckLocation;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public SignActiveInfo getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(SignActiveInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public String getSignRule() {
        return signRule;
    }

    public void setSignRule(String signRule) {
        this.signRule = signRule;
    }
}
