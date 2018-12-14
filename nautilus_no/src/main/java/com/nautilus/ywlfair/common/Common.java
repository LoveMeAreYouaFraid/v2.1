package com.nautilus.ywlfair.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class Common {

	private static Common mInstance;
	
	/**
     * 屏幕高
     */
    private int screenHeight;
    
    /**
     * 屏幕宽
     */
    private int screenWidth;
    
    private float scaledDensity;
    
    private int statusHeight;
    
    public static synchronized Common getInstance() {
        if (mInstance == null) {
            mInstance = new Common();
        }
        return mInstance;
    }
    
    /**
     * Get pixels of screen height
     *
     * @return screen height
     */
    public int getScreenHeight() {
        if (screenHeight == 0) {
        	
        	DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        	
            screenHeight = dm.heightPixels;
        }
        return screenHeight;
    }

    /**
     * Get pixels of screen height
     *
     * @return screen height
     */
    public int getScreenWidth() {
        if (screenWidth == 0) {
        	
        	DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        	
            screenWidth = dm.widthPixels;
        }
        return screenWidth;
    }

    public float getScaledDensity() {
        if (scaledDensity == 0) {
        	DisplayMetrics dm = MyApplication.getInstance().getResources().getDisplayMetrics();
        	
            scaledDensity =  dm.scaledDensity;
        }
        return scaledDensity;
    }
    
    /**
     * 获取程序版本号
     *
     * @return 程序版本号
     */
    public String getVersionCode() {
        Context context = MyApplication.getInstance();
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
    
    /**
     * 获取设备型号
     *
     * @return 设备型号
     */
    public String getModel() {
        return Build.MODEL;
    }
    
    /**
     * 获取系统版本名
     *
     * @return 系统版本名
     */
    public String getOsReleaseVersion() {
        return Build.VERSION.RELEASE;
    }
    
    /**
     * 获取IMSI
     *
     * @return IMSI
     */
    public String getIMSI() {
        String imsi = ((TelephonyManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        return imsi;
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return resultData;
    }
}
