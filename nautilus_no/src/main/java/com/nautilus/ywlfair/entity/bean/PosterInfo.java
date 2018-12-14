package com.nautilus.ywlfair.entity.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class PosterInfo extends BaseItem {
	private static final long serialVersionUID = -5532433616875689437L;
	
	private String imgUrl;
	private String desc;
	
	public static PosterInfo createPosterInfo(JSONObject jsonObject) throws JSONException {
		PosterInfo posterInfo = null;
		if (jsonObject != null) {
			String imgUrlString = jsonObject.getString("imgUrl");
			String descString = jsonObject.getString("desc");
			posterInfo = new PosterInfo(imgUrlString, descString);
		}
		return posterInfo;
	}
	
	public PosterInfo(String imgUrl, String desc) {
		this.imgUrl = imgUrl;
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
