package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/5.
 */
public class PicInfo implements Serializable{
    private String imgUrl;

    private String desc;

    private String thumbnailUrl;

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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
