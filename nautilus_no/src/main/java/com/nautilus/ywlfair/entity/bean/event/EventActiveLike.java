package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2015/12/15.
 */
public class EventActiveLike extends BaseItem {
    private int hasLike;

    public EventActiveLike(int hasLike){
        this.hasLike = hasLike;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }
}
