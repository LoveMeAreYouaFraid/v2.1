package com.nautilus.ywlfair.common.utils;

import android.text.TextUtils;


public class CacheUtil {

    public static <T> T getCache(String key, Class<T> clazz) {

        String json = PreferencesUtil.getString(key);

        if (!TextUtils.isEmpty(json)) {
            return new JsonUtil<T>().json2Bean(json, clazz.getName());
        }
        return null;
    }

    public static <T> void putCache(String key, T object) {

        if(object == null) {
//            PreferencesUtil.clearKey(key);
            return;
        }

        String json = new JsonUtil<T>().bean2Json(object);
        if (!TextUtils.isEmpty(json)) {
            PreferencesUtil.putString(key, json);
        }
    }

    public static void clearCache(String key) {
        PreferencesUtil.clearKey(key);
    }

}
