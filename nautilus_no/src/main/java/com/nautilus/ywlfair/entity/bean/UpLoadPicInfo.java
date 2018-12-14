package com.nautilus.ywlfair.entity.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class UpLoadPicInfo extends BaseItem{

	private String normalPictureUrl;
	
	private String normalPicturePath;

	private String thumbnailUrl;
	
	private String thumbnailPath;
	
	
	public static UpLoadPicInfo createActiveRecord(JSONObject jsonObject) throws JSONException, ParseException{
		UpLoadPicInfo picInfo = null;
		
		if(jsonObject != null){
			picInfo = new UpLoadPicInfo();
			
			picInfo.setNormalPicturePath(jsonObject.getString("normalPicturePath"));
			
			picInfo.setNormalPictureUrl(jsonObject.getString("normalPictureUrl"));
			
			picInfo.setThumbnailUrl(jsonObject.getString("thumbnailUrl"));
			
			picInfo.setThumbnailPath(jsonObject.getString("thumbnailPath"));
		}
		
		return picInfo;
	}


    public String getNormalPictureUrl() {
        return normalPictureUrl;
    }

    public void setNormalPictureUrl(String normalPictureUrl) {
        this.normalPictureUrl = normalPictureUrl;
    }

    public String getNormalPicturePath() {
        return normalPicturePath;
    }

    public void setNormalPicturePath(String normalPicturePath) {
        this.normalPicturePath = normalPicturePath;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}
