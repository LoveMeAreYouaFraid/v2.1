package com.nautilus.ywlfair.entity.bean;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

public class SignActiveInfo extends BaseItem{

	private static final long serialVersionUID = 2390598283352264828L;
	
	private String startTime;
	
	private String address;
	
	private String name;
	
	private String endTime;
	
	private PosterInfo posterInfo;

    private int actId;

    private String organizer;

    private int type;

    private String addrMap;

    private int likeNum;

    private String listPic;

    private int activityStatus;

    private int wantJoinNum;

    private String introduction;

    private String mapStaticImgUrl;
	
	public static SignActiveInfo createItem(JSONObject jsonObject) throws JSONException, ParseException {
		SignActiveInfo signActiveInfo = null;
		if(jsonObject != null){
			signActiveInfo = new SignActiveInfo();

            signActiveInfo.setActId(jsonObject.getInt("actId"));

            signActiveInfo.setOrganizer(jsonObject.getString("organizer"));

            signActiveInfo.setType(jsonObject.getInt("type"));

            signActiveInfo.setAddrMap(jsonObject.getString("addrMap"));

            signActiveInfo.setLikeNum(jsonObject.getInt("likeNum"));

            signActiveInfo.setListPic(jsonObject.getString("listPic"));

            signActiveInfo.setActivityStatus(jsonObject.getInt("activityStatus"));

            signActiveInfo.setWantJoinNum(jsonObject.getInt("wantJoinNum"));

            signActiveInfo.setIntroduction(jsonObject.getString("introduction"));

            signActiveInfo.setMapStaticImgUrl(jsonObject.getString("mapStaticImgUrl"));
			
			signActiveInfo.setStartTime(jsonObject.getString("startTime"));
			
			signActiveInfo.setAddress(jsonObject.getString("address"));
			
			signActiveInfo.setName(jsonObject.getString("name"));
			
			signActiveInfo.setEndTime(jsonObject.getString("endTime"));
			
			signActiveInfo.setPosterInfo(PosterInfo.createPosterInfo(jsonObject.getJSONObject("posterInfo")));
		}
		
		return signActiveInfo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public PosterInfo getPosterInfo() {
		return posterInfo;
	}

	public void setPosterInfo(PosterInfo posterInfo) {
		this.posterInfo = posterInfo;
	}

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAddrMap() {
        return addrMap;
    }

    public void setAddrMap(String addrMap) {
        this.addrMap = addrMap;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public String getListPic() {
        return listPic;
    }

    public void setListPic(String listPic) {
        this.listPic = listPic;
    }

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getWantJoinNum() {
        return wantJoinNum;
    }

    public void setWantJoinNum(int wantJoinNum) {
        this.wantJoinNum = wantJoinNum;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMapStaticImgUrl() {
        return mapStaticImgUrl;
    }

    public void setMapStaticImgUrl(String mapStaticImgUrl) {
        this.mapStaticImgUrl = mapStaticImgUrl;
    }
}
