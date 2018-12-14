package com.nautilus.ywlfair.common;

import android.app.Application;
import android.content.Context;

import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.nautilus.ywlfair.common.utils.PushManager;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.smtt.sdk.QbSdk;

public class MyApplication extends Application {

    private static MyApplication mApplication;

    private UserInfo currentUser = null;

    private boolean isLogin = false;

    private double latitude = 0;

    private double longitude = 0;

    private String locationDescription = "未获取到位置信息";

    private long MessageCount = 0;

    private String accessToken = "";

    private String deviceId = "";

    public static String calendarUrl;

    public static Integer mainTabActivityIndex = 0;

    private String actDrawUrl;

    private String actDrawLogUrl;

    private String actMainUrl;

    private int userType = 0;//0普通用户 1 摊主

    public static synchronized MyApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        VolleyUtil.init(getApplicationContext());

        initImageLoader(getApplicationContext());

        PushManager.initOneSDK(MyApplication.getInstance().getApplicationContext());

        initXiaomiHuawei();

        preInitTbs();
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration loaderConfig = new ImageLoaderConfiguration.Builder(context)
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb sd�?(本地)缓存的最大�??
                .diskCacheFileCount(500)  // 可以缓存的文件数�?
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs() // 打印debug log
                .build(); //�?始构�?

        ImageLoader.getInstance().init(loaderConfig);
    }

    private void initXiaomiHuawei() {
        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(this, "2882303761517439610", "w0PFyZ9503y8xLiJZty+EQ==");

        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(this);
    }

    private void preInitTbs(){
        if(!QbSdk.isTbsCoreInited()){
            QbSdk.preInit(this);
        }
    }


    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInfo currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getActDrawUrl() {
        return actDrawUrl;
    }

    public void setActDrawUrl(String actDrawUrl) {
        this.actDrawUrl = actDrawUrl;
    }

    public String getActDrawLogUrl() {
        return actDrawLogUrl;
    }

    public void setActDrawLogUrl(String actDrawLogUrl) {
        this.actDrawLogUrl = actDrawLogUrl;
    }

    public long getMessageCount() {
        return MessageCount;
    }

    public void setMessageCount(long messageCount) {
        MessageCount = messageCount;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getActMainUrl() {
        return actMainUrl;
    }

    public void setActMainUrl(String actMainUrl) {
        this.actMainUrl = actMainUrl;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
