package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
public class SystemPushTag implements Serializable {
    private int id;

    private String tag;

    private int userId;

    private long creatTime;

    private long updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(long creatTime) {
        this.creatTime = creatTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
