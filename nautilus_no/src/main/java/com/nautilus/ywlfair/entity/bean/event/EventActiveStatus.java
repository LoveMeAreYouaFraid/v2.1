package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.NautilusItem;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/14.
 */
public class EventActiveStatus implements Serializable {
    private int type ;//0 摊主状态变化  1  押金状态变化  2 报名状态变化 3 购摊状态变化  -1 读取到活动刷新UI -2登录之后刷新活动信息

    private int status;

    private NautilusItem nautilusItem;

    public EventActiveStatus(int type, int status, NautilusItem nautilusItem){
        this.type = type;

        this.status = status;

        this.nautilusItem = nautilusItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NautilusItem getNautilusItem() {
        return nautilusItem;
    }

    public void setNautilusItem(NautilusItem nautilusItem) {
        this.nautilusItem = nautilusItem;
    }
}
