package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2015/12/15.
 */
public class EventGoodsLike extends BaseItem {
    private int hasLike;

    private int type;//0是 首页   1是 第一层  2 是 第二层以此类推

    public EventGoodsLike(int hasLike, int type){
        this.hasLike = hasLike;

        this.type = type;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
