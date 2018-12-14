package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2016/1/27.
 */
public class EventDeposit extends BaseItem {
    private int type;//押金类型  0需要交   1 可以退  2 结束了

    public EventDeposit(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
