package com.nautilus.ywlfair.entity.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ActivitySysConfigInfoList extends BaseItem {
    private List<ActivitySysConfig> type1;
    private List<ActivitySysConfig> type2;
    private List<ActivitySysConfig> type3;
    private List<ActivitySysConfig> type4;

    public List<ActivitySysConfig> getType1() {
        return type1;
    }

    public void setType1(List<ActivitySysConfig> type1) {
        this.type1 = type1;
    }

    public List<ActivitySysConfig> getType2() {
        return type2;
    }

    public void setType2(List<ActivitySysConfig> type2) {
        this.type2 = type2;
    }

    public List<ActivitySysConfig> getType3() {
        return type3;
    }

    public void setType3(List<ActivitySysConfig> type3) {
        this.type3 = type3;
    }

    public List<ActivitySysConfig> getType4() {
        return type4;
    }

    public void setType4(List<ActivitySysConfig> type4) {
        this.type4 = type4;
    }
}
