package com.nautilus.ywlfair.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.InitResultCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.PostAppInitializationRequest;
import com.nautilus.ywlfair.entity.request.PostSystemPushNexusRequest;
import com.nautilus.ywlfair.entity.response.GetAppInitializationResponse;

/**
 * Created by Administrator on 2016/3/14.
 */
public class PushManager {

    private static final String TAG = "ali_push";

    private static CloudPushService cloudPushService;

    /**
     * 初始化AlibabaSDK
     *
     * @param applicationContext
     */
    public static void initOneSDK(final Context applicationContext) {
        AlibabaSDK.asyncInit(applicationContext, new InitResultCallback() {

            @Override
            public void onSuccess() {
                //alibabaSDK初始化成功后，初始化移动推送通道
                initCloudChannel(applicationContext);
            }

            @Override
            public void onFailure(int code, String message) {
                LogUtil.e(TAG, "init onesdk failed : " + message);
            }
        });
    }

    /**
     * 初始化移动推送通道
     *
     * @param applicationContext
     */
    private static void initCloudChannel(Context applicationContext) {
        cloudPushService = AlibabaSDK.getService(CloudPushService.class);

        if (cloudPushService != null) {
            cloudPushService.register(applicationContext, new CommonCallback() {

                @Override
                public void onSuccess() {

                    MyApplication.getInstance().setDeviceId(cloudPushService.getDeviceId());

                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {

                    MyApplication.getInstance().setDeviceId("");

                }
            });
        }
    }

    public static void bindPushAccount() {

        if (MyApplication.getInstance().isLogin()) {
            UserInfo userInfo = GetUserInfoUtil.getUserInfo();

            if (cloudPushService == null) {
                return;
            }

            cloudPushService.bindAccount(String.valueOf(userInfo.getUserId()));

            if (GetUserInfoUtil.getUserInfo().getUserType() == 0) {
                cloudPushService.addTag("normal", null);
            } else {
                cloudPushService.addTag("owner", null);
            }

            bindPushNexus(Request.Method.PUT, MyApplication.getInstance().getDeviceId(),
                    userInfo.getUserId(), userInfo.getNickname());
        }

    }

    public static void unBindPushAccount(int userId, String nickName) {

        if (cloudPushService == null) {
            return;
        }

        cloudPushService.unbindAccount();

        bindPushNexus(Request.Method.PUT, MyApplication.getInstance().getDeviceId(), userId, nickName);
    }

    private static void bindPushNexus(int method, String deviceId, int userId, String nickname) {

        PostSystemPushNexusRequest request = new PostSystemPushNexusRequest(method, deviceId,
                userId, nickname, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {

            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onFinish() {

            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }


}
