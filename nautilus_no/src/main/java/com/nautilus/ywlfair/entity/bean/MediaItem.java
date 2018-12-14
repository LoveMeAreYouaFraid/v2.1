package com.nautilus.ywlfair.entity.bean;

public class MediaItem extends BaseItem {
	private static final long serialVersionUID = -3636379418200401349L;

	private String exCid;
	private long mediaId;
	private int mediaType; // 0 is pic, 1 is video, 2 is mp3
	private String mediaTitle;
	private String mediaUrl;
	private long mediaUploadDate;
	private String mediaDesc;
	private String thumbnailUrl;
	

	public String getExCid() {
		return exCid;
	}

	public void setExCid(String exCid) {
		this.exCid = exCid;
	}

	public long getMediaId() {
		return mediaId;
	}

	public void setMediaId(long mediaId) {
		this.mediaId = mediaId;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaTitle() {
		return mediaTitle;
	}

	public void setMediaTitle(String mediaTitle) {
		this.mediaTitle = mediaTitle;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public long getMediaUploadDate() {
		return mediaUploadDate;
	}

	public void setMediaUploadDate(long mediaUploadDate) {
		this.mediaUploadDate = mediaUploadDate;
	}

	public String getMediaDesc() {
		return mediaDesc;
	}

	public void setMediaDesc(String mediaDesc) {
		this.mediaDesc = mediaDesc;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
}
