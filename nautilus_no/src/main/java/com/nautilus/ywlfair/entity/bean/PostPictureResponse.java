package com.nautilus.ywlfair.entity.bean;

import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/4.
 */
public class PostPictureResponse extends InterfaceResponse implements Serializable{

    private UpLoadPicInfo result;

    public UpLoadPicInfo getResult() {
        return result;
    }

    public void setResult(UpLoadPicInfo result) {
        this.result = result;
    }
}
