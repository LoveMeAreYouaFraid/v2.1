package com.nautilus.ywlfair.common.utils.voley;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (©) 2014 Shanghai Russula Network Technology Co., Ltd
 * <p/>
 * 所有接口的响应数据的基类，定义了错误信息成员变量
 *
 * @author jiangdongxiang
 * @version 1.0, 14-10-17 13:43
 * @since 14-10-17
 */
public class InterfaceResponse {

    // 最后更新时间
    @SerializedName("time")
    private String time;

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
