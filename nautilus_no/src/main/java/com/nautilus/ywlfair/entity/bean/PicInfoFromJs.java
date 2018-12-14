package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class PicInfoFromJs implements Serializable {

    private List<String> photos;

    private String type;

    private int index;

    private String url;

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
