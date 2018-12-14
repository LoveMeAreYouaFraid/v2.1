package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.HomePagerItem;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * 首页信息response
 */
public class GetHomePagerResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private HomePagerItem homePagerItem;

    public HomePagerItem getHomePagerItem() {
        return homePagerItem;
    }

    public void setHomePagerItem(HomePagerItem homePagerItem) {
        this.homePagerItem = homePagerItem;
    }
}
