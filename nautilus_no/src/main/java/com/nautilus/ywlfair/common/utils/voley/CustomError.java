package com.nautilus.ywlfair.common.utils.voley;

import com.android.volley.VolleyError;

/**
 * Copyright (©) 2014 Shanghai Russula Network Technology Co., Ltd
 * <p/>
 * TODO:
 *
 * @author jiangdongxiang
 * @version 1.0, 2014/11/27 16:15
 * @since 2014/11/27
 */
public class CustomError extends VolleyError {

    private InterfaceResponse response;

    public CustomError(InterfaceResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public CustomError(String message) {
        super(message);
    }

    public InterfaceResponse getResponse() {
        return response;
    }



}
