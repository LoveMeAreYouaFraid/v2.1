package com.nautilus.ywlfair.common.utils.voley;

import com.android.volley.VolleyError;

/**
 * Copyright (©) 2015 Shanghai Duxue Networking Technology Co., Ltd
 * <p/>
 * 响应数据事件监听器
 *
 * @author jiangdongxiang
 * @version 1.0, 2015/3/20 10:54
 * @since 2015/3/20
 *
 * @param <T>
 */
public interface ResponseListener<T> {

    /**
     * 该方法在请求执行前被调用
     */
    void onStart();

    /**
     * 在获取到网络请求的响应之前，先获取缓存，如果有缓存，则该方法被调用
     *
     * @param response
     */
    void onCacheResponse(T response);
    
    /** Called when a response is received. */
    void onResponse(T response);

    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     */
    void onErrorResponse(VolleyError error);

    /**
     * 该方法在请求完成时调用（无论成功或者失败），且先于onResponse()和onErrorResponse()方法之前
     */
    void onFinish();

}
