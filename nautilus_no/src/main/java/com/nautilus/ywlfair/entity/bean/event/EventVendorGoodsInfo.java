package com.nautilus.ywlfair.entity.bean.event;

import com.nautilus.ywlfair.entity.bean.BaseItem;

/**
 * Created by Administrator on 2016/1/27.
 */
public class EventVendorGoodsInfo extends BaseItem {
    private int type;//押金类型  0货品信息   1 品牌信息  2 店铺信息

    public EventVendorGoodsInfo(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
