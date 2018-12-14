package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2015/12/15.
 */
public class EventArticleType extends BaseItem {

    private int type;

    private int hasLike;

    public EventArticleType(int type, int hasLike){
        this.type = type;

        this.hasLike = hasLike;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHasLike() {
        return hasLike;
    }

    public void setHasLike(int hasLike) {
        this.hasLike = hasLike;
    }
}
