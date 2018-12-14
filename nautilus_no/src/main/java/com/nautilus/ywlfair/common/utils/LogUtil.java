package com.nautilus.ywlfair.common.utils;

import android.util.Log;

import com.nautilus.ywlfair.BuildConfig;

public class LogUtil {
    
    public static int v(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            return Log.v(tag, msg);
        }
        return 0;
    }
    
    public static int d(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            return Log.d(tag, msg);
        }
        return 0;
    }
    
    public static int i(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            return Log.i(tag, msg);
        }
        return 0;
    }
    
    public static int w(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            return Log.w(tag, msg);
        }
        return 0;
    }
    
    public static int e(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            return Log.e(tag, msg);
        }
        return 0;
    }
    
    public static int e(String tag, String msg, Throwable tr) {
        if(BuildConfig.DEBUG) {
            return Log.e(tag, msg, tr);
        }
        return 0;
    }

}
