package com.nautilus.ywlfair.common.utils.voley;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.module.launch.LoginActivity;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.MD5Utils;
import com.nautilus.ywlfair.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Copyright (©) 2014 Shanghai Duxue Networking Technology Co., Ltd
 * <p/>
 * 接口请求类
 *
 * @author jiangdongxiang
 * @version 1.2, 14-10-17 13:43
 * @since 15-07-21
 */
public abstract class InterfaceRequest<T> extends Request<T> {

    private static final String TAG = InterfaceRequest.class.getSimpleName();

    // 字符集
    protected static final String CHARSET_NAME = "utf-8";

    // 需要发送的参数MAP
    private Map<String, String> mParams;

    // 重试次数
    private static final int RETRY_COUNT = 0;

    //超时时间
    private static final int R_SOCKET_TIMEOUT = 5000;

    // 响应Listener
    protected ResponseListener<T> mListener;

    // 缓存Data
    private String mCacheData;

    // 该接口是否需要登录后才能访问的标志位，若构造方法未包含此参数，则默认为真
    private boolean mIsNeedLogin = true;

    //调用接口当前系统时间
    private long systemTime;

    /**
     *
     * @param method 请求类型 {@link com.android.volley.Request.Method}
     * @param params 参数列表
     * @param listener 事件监听器
     */
    public InterfaceRequest(int method, String interfaceName,String param, Map<String, String> params,
                            ResponseListener<T> listener) {
        super(method, RequestUtil.getInterfaceUrl(interfaceName, param), null);
        systemTime = System.currentTimeMillis();
        mParams = params;
        mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(R_SOCKET_TIMEOUT, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     *
     * @param method 请求类型 {@link com.android.volley.Request.Method}
     * @param params 参数列表
     * @param isNeedLogin 是否需要登录后才能请求
     * @param listener 事件监听器
     */
    public InterfaceRequest(int method, String interfaceName,String param, Map<String, String> params,
                            boolean isNeedLogin, ResponseListener<T> listener) {
        super(method, RequestUtil.getInterfaceUrl(interfaceName, param), null);
        systemTime = System.currentTimeMillis();
        mParams = params;
        mIsNeedLogin = isNeedLogin;
        mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(R_SOCKET_TIMEOUT, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     *
     * @param params 参数列表
     * @param listener 事件监听器
     */
    public InterfaceRequest(String interfaceName,String param,Map<String, String> params,
                            ResponseListener<T> listener) {
        super(Method.GET, RequestUtil.getInterfaceUrl(interfaceName, param), null);
        systemTime = System.currentTimeMillis();
        mParams = params;
        mListener = listener;
        setRetryPolicy(new DefaultRetryPolicy(R_SOCKET_TIMEOUT, RETRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * 转换网络响应为需要的类型
     *
     * @param response
     * @return
     */
    @Override
    protected abstract Response<T> parseNetworkResponse(NetworkResponse response);

    /**
     * 处理Response
     *
     * @param response
     */
    @Override
    protected void deliverResponse(T response) {

        // 调用事件监听器的onFinish方法
        mListener.onFinish();

        // 若response不为空，调用事件监听器的onResponse方法
        if(response != null) {
            mListener.onResponse(response);
        }

        return;
    }

    /**
     * 处理错误
     *
     * @param error
     */
    @Override
    public void deliverError(VolleyError error) {

        /*
         * 若error为CustomError的实例，则进行进一步处理，
         * 否则调用事件监听器的onFinish及onErrorResponse方法
        */
        if (error instanceof CustomError) {

            // 获取服务器返回的错误码
            InterfaceResponse response = ((CustomError) error).getResponse();
            int errorCode = response.getStatus();

            // 根据错误码进行判断，决定是否重新获取AccessToken
            switch (errorCode) {
                // 若错误码为token过期或token为空，则重新请求AccessToken
                case Constant.RESPONSE.CODE.TOKEN_OUT_DATE:

                    LoginActivity.startLoginActivity(null);

                    MyApplication.getInstance().setLogin(false);

                    MyApplication.getInstance().setCurrentUser(null);

                    mListener.onFinish();

                    ((CustomError) error).getResponse().setMessage("登录已过期，请重新登录");
                    mListener.onErrorResponse(error);

                    break;
                // 若错误码为token错误
//                case RESPONSE.VALUE.ERROR_CODE_ACCESS_TOKEN:
//                    if(mIsNeedLogin) {
//                        LoginActivity.startLoginActivity(MyApplication.mainTabActivity);
//                    } else {
//                        requestAccessToken();
//                    }
//                    break;
                // 若错误码为token为未登录
//                case RESPONSE.VALUE.ERROR_CODE_NOT_LOGIN:
//                    //跳转到登录界面
//                    LoginActivity.startLoginActivity(MyApplication.mainTabActivity);
//                    break;
                default:
                    mListener.onFinish();
                    mListener.onErrorResponse(error);
                    break;
            }
        } else {
            mListener.onFinish();
            mListener.onErrorResponse(error);
        }
    }

    /**
     * 处理参数列表，移除值为null的参数等
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getParams() throws AuthFailureError {

        Map<String, String> params = getBaseMap();

        if (mParams != null && !mParams.isEmpty()) {
            params.putAll(mParams);

            Log.e(TAG, "getParams()1" + mParams.toString());
        }

        //若请求参数中有"update_time"，则从缓存中查找该数据
        if (params.containsKey(Constant.REQUEST.KEY.UPDATE_TIME)) {

            byte[] bytes = VolleyUtil.getDataInDiskCache(getCacheKey());

            LogUtil.e(TAG, "bytes == null: " + (bytes == null));

            if (bytes != null) {
                try {
                    //通过反射获取T的Class

                    String jsonString = new String(bytes, "UTF-8");

                    JSONObject jsonObject = new JSONObject(jsonString);

                    LogUtil.e(TAG, "jsonObject.has(Constant.RESPONSE.KEY.UPDATE_TIME): "
                            + (jsonObject.has(Constant.RESPONSE.KEY.UPDATE_TIME)));

                    if (jsonObject.has(Constant.RESPONSE.KEY.UPDATE_TIME)) {
                        String updateTime = jsonObject.getString(Constant.RESPONSE.KEY.UPDATE_TIME);
                        params.put(Constant.REQUEST.KEY.UPDATE_TIME, updateTime);
//                        mParams.put(Constant.REQUEST.KEY.UPDATE_TIME, updateTime);
                    } else {
                        params.put(Constant.REQUEST.KEY.UPDATE_TIME, "1");
                    }

                } catch (UnsupportedEncodingException | JSONException e) {
                    Log.w(TAG, "parseNetworkResponse UnsupportedEncodingException | JSONException: ", e);
                    params.put(Constant.REQUEST.KEY.UPDATE_TIME, "1");
                }
            } else {
                params.put(Constant.REQUEST.KEY.UPDATE_TIME, "1");
            }
        }

        // 移除params中键值为null的键值对，防止Volley报错
//        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, String> entry = it.next();
//            String value = entry.getValue();
//            if (TextUtils.isEmpty(value)) {
//                it.remove();
//            }
//        }

        LogUtil.e(TAG, "getParams()2: " + params.toString());

        return params;
    }

    /**
     * 增加基本参数
     *
     * @return
     */
    private Map<String, String> getBaseMap() {
        return new TreeMap<>();
    }

    /**
     * 生成URL，因Volley不支持将GET类型请求的参数加入到URL中，
     * 故需要在此方法内将GET类型请求的参数加入URL中
     *
     * @return
     */
    @Override
    public String getUrl() {

        // 为GET类型的请求在URL中增加自定义参数
        if (getMethod() == Method.GET) {

            try {

                Map<String, String> params = getParams();

                Uri.Builder builder = Uri.parse(super.getUrl()).buildUpon();

                for (Map.Entry<String, String> entry : params.entrySet()) {

                    if (!TextUtils.isEmpty(entry.getValue())) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }

                }

                LogUtil.e(TAG, "getUrl(): " + builder.toString());

                return builder.toString();

            } catch (AuthFailureError e) {
                LogUtil.e(TAG, "getUrl(): AuthFailureError: ", e);
            }
        }

        LogUtil.e(TAG, "getUrl(): " + super.getUrl());

        return super.getUrl();

    }

    /**
     * 重写该方法，忽略影响缓存获取的字段
     */
    @Override
    public String getCacheKey() {

        LogUtil.e(TAG, "getCacheKey()");

        // 对GET类型请求的Cache key进行处理，忽略URL参数中的部分字段，使得Cache可用
        if (getMethod() == Method.GET) {
            try {
                Map<String, String> params = getParamsWithoutUpdateTime();
                Uri.Builder builder = Uri.parse("").buildUpon();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (!TextUtils.isEmpty(entry.getValue())
                            && !entry.getKey().equals(Constant.REQUEST.KEY.ACCESS_TOKEN)
                            && !entry.getKey().equals(Constant.REQUEST.KEY.UPDATE_TIME)) {
                        builder.appendQueryParameter(entry.getKey(), entry.getValue());
                    }
                }

                LogUtil.e(TAG, "getCacheKey(): builder.toString()" + builder.toString());

                return builder.toString();
            } catch (AuthFailureError e) {
            }
        }

        return super.getCacheKey();
    }

    /**
     * 生成请求的Header
     *
     * @return
     * @throws AuthFailureError
     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.isEmpty()) {
            headers = new HashMap<>();
        }

        headers.put("appName", "NautilusFair");
        headers.put("version", Common.getInstance().getVersionCode() + "");
        headers.put("deviceType", "2");
        headers.put("deviceName", Common.getInstance().getModel());
        headers.put("deviceOs", Common.getInstance().getOsReleaseVersion());
        headers.put("serialNo", Common.getInstance().getIMSI());
        headers.put("time", systemTime + "");

        String token = MyApplication.getInstance().getAccessToken();

        if(TextUtils.isEmpty(token)){
            token = "default_token";
        }

        headers.put("accessToken", token);

        headers.put("signature", getSignature(token));

        return headers;
    }

    private String getSignature(String accessToken) {
        Map<String, String> paramMap =  new HashMap<>();
        paramMap.put("appName", "NautilusFair");
        paramMap.put("version", Common.getInstance().getVersionCode() + "");
        paramMap.put("deviceType", "2");
        paramMap.put("deviceName", Common.getInstance().getModel());
        paramMap.put("deviceOs", Common.getInstance().getOsReleaseVersion());
        paramMap.put("serialNo", Common.getInstance().getIMSI());
        paramMap.put("time", systemTime +"");

        paramMap.put("accessToken", accessToken);

        Collection<String> keyset = paramMap.keySet();
        List<String> list = new ArrayList<String>(keyset);

        // 对key键值按字典升序排序
        Collections.sort(list);

        String strJoins = "";
        for (int i = 0; i < list.size(); i++) {
            strJoins += list.get(i) + "=" + paramMap.get(list.get(i));
        }

        String secret = Constant.SIGNATURE_SECRET;

        String md5 = MD5Utils.md5(secret + strJoins);

        return md5;

    }


    /**
     * 处理返回的Response，若其中含有Error对象，则返回Error
     *
     * @param response
     * @return
     */
    public CustomError processResponse(InterfaceResponse response) {
        if (response.getStatus() != 0) {
            return new CustomError(response);
        } else {
            return null;
        }
    }

    /**
     * 从磁盘缓存中获取Response
     */
    public void getCache() {

        LogUtil.e(TAG, "getCache(): ");

        //根据CacheKey获取缓存字节数组
        String cacheKey = getCacheKey();

        byte[] cacheBytes = VolleyUtil.getDataInDiskCache(cacheKey);

        //若缓存数据为空，则返回
        if(cacheBytes == null) {
            return;
        }

        String cacheString;

        try {

            cacheString = new String(cacheBytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "parseNetworkResponse UnsupportedEncodingException: ", e);
            return;
        }

        //若缓存数据为空，则返回
        if(TextUtils.isEmpty(cacheString)) {
            return;
        }

        //通过反射获取T的Class
        Class clazz = (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass())
                    .getActualTypeArguments()[0];

        //将缓存数据转换为Response
        T response = new JsonUtil<T>().json2Bean(cacheString, clazz.getName());

        if(response != null) {
            mCacheData = cacheString;
            mListener.onCacheResponse(response);
        }

    }


    public ResponseListener getResponseListener() {
        return mListener;
    }

    /**
     * 处理参数列表，移除值为null的参数等
     *
     * @return
     * @throws AuthFailureError
     */
    public Map<String, String> getParamsWithoutUpdateTime() throws AuthFailureError {

        LogUtil.e(TAG, "getParamsWithoutUpdateTime()");

        Map<String, String> params = getBaseMap();

        if (mParams != null && !mParams.isEmpty()) {
            params.putAll(mParams);
        }
        // 移除params中键值为null的键值对，防止Volley报错
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String value = entry.getValue();
            if (value == null) {
                it.remove();
            }
        }

        LogUtil.e(TAG, "getParamsWithoutUpdateTime(): " + params.toString());

        return params;
    }

    /**
     * 获取缓存中的data数据
     *
     * @return
     */
    public String getCacheData() {
        return mCacheData;
    }
}
