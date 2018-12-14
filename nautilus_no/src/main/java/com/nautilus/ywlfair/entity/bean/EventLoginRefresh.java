package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/15.
 */
public class EventLoginRefresh implements Serializable {
    private int type;//0 刷新活动信息（报名状态等）

    public EventLoginRefresh(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
