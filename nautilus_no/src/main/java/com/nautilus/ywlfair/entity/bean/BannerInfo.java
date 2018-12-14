package com.nautilus.ywlfair.entity.bean;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class BannerInfo extends BaseItem {
	private static final long serialVersionUID = 6058707928217558714L;

	private int type;
	private String title;
	private int itemId;
    private String date;
    @SerializedName("imgUrl")
	private String imageUrl;

	private String url;
	
	public static BannerInfo createBannerInfoItem(JSONObject jsonObject) throws JSONException {
		BannerInfo itemInfo = null;
		if (jsonObject != null) {
			itemInfo = new BannerInfo();
			
			itemInfo.setType(jsonObject.getInt("type"));
			itemInfo.setTitle(jsonObject.getString("title"));
			itemInfo.setItemId(jsonObject.getInt("itemId"));
			itemInfo.setImageUrl(jsonObject.getString("imgUrl"));
			itemInfo.setUrl(jsonObject.getString("url"));
		}
		
		return itemInfo;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
