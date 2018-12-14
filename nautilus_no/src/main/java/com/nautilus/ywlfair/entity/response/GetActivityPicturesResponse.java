package com.nautilus.ywlfair.entity.response;

import com.google.gson.annotations.SerializedName;
import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 活动图片列表response
 */
public class GetActivityPicturesResponse extends InterfaceResponse implements Serializable {

    @SerializedName("result")
    private MyResult result;

    public MyResult getResult() {
        return result;
    }

    public void setResult(MyResult result) {
        this.result = result;
    }

    public class MyResult implements Serializable{

        List<PicInfo> pictureInfoList;

        public List<PicInfo> getPictureInfoList() {
            return pictureInfoList;
        }

        public void setPictureInfoList(List<PicInfo> pictureInfoList) {
            this.pictureInfoList = pictureInfoList;
        }
    }
}
