package com.nautilus.ywlfair.entity.bean;

/**
 * Created by lipeng on 2016/3/29.
 */
public class DebitRecordsInfo extends BaseItem {
    //    "price": 0.01,
//            "logTime": "1453103286000",
//            "desc": "破坏了桌子",
//            //操作类型  1.扣款  2.退款
//            “logType”:1
    private double price;
    private String logTime;
    private String desc;
    private String logType;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }
}
