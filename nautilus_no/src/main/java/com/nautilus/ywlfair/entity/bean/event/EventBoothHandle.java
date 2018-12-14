package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2015/12/15.
 */
public class EventBoothHandle extends BaseItem {

    private int type;//摊位状态

    private int boothType;//0已付款  1 待付款

    public EventBoothHandle( int boothType,int type){

        this.type = type;

        this.boothType = boothType;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBoothType() {
        return boothType;
    }

    public void setBoothType(int boothType) {
        this.boothType = boothType;
    }
}
