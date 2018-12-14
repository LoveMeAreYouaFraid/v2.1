package com.nautilus.ywlfair.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.nautilus.ywlfair.common.MyApplication;

/**
 * Copyright (©) 2014
 * <p/>
 * 配置文件读写工具类
 * 
 * @author eastonc
 * @version 1.0, 14-7-28 13:43
 * @since 14-7-28
 */
public class PreferencesUtil {

    private static SharedPreferences getPreference(Context paramContext, String preferencesName) {
        return paramContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);

    }

    /**
     * 清除指定名称的配置文件内数据
     * 
     * @param paramContext
     * @param preferencesName
     */
    public static void clearAll(Context paramContext, String preferencesName) {
        getPreference(paramContext, preferencesName).edit().clear().commit();
    }

    /**
     * 获取String类型的键值
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     * @return Key value
     */
    public static String getString(Context paramContext, String preferencesName, String keyString) {
        if (keyString == null)
            return null;
        return getPreference(paramContext, preferencesName).getString(keyString, null);
    }

    /**
     * 存储String类型的键值对
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     * @param valueString
     */
    public static void putString(Context paramContext, String preferencesName, String keyString,
            String valueString) {
        if (keyString != null)
            getPreference(paramContext, preferencesName).edit().putString(keyString, valueString)
                    .commit();
    }

    public static void clearKey(Context paramContext, String preferencesName, String keyString) {
        if (keyString != null)
            getPreference(paramContext, preferencesName).edit().clear().commit();
    }

    /**
     * 移除String类型的键值对
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     */
    public static void removeString(Context paramContext, String preferencesName, String keyString) {
        if (keyString != null)
            getPreference(paramContext, preferencesName).edit().remove(keyString).commit();
    }

    /**
     * 获取int类型的键值，如获取不到则返回0
     * 
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public static int getInt(Context context, String prefName, String key) {
        return key == null ? 0 : getPreference(context, prefName).getInt(key, 0);
    }

    /**
     * 获取int类型的键值，需要指定默认值
     * 
     * @param context
     * @param prefName
     * @param key
     * @param defValue
     * @return Key value
     */
    public static int getInt(Context context, String prefName, String key, int defValue) {
        return key == null ? defValue : getPreference(context, prefName).getInt(key, defValue);
    }

    /**
     * 存储int类型的键值对
     * 
     * @param context
     * @param prefName
     * @param key
     * @param value
     */
    public static void putInt(Context context, String prefName, String key, int value) {
        if (key != null) {
            getPreference(context, prefName).edit().putInt(key, value).commit();
        }
    }

    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public static long getLong(Context context, String prefName, String key) {
        return key == null ? 0L : getPreference(context, prefName).getLong(key, 0L);
    }

    public static long getLong(Context context, String prefName, String key, long defValue) {
        return key == null ? defValue : getPreference(context, prefName).getLong(key, defValue);
    }

    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @param value
     */
    public static void putLong(Context context, String prefName, String key, long value) {
        if (key != null) {
            getPreference(context, prefName).edit().putLong(key, value).commit();
        }
    }
    

    /**
     * 
     * @return
     */
    private static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
    }

    /**
     * 获取String类型的键值
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     * @return Key value
     */
    public static String getString(String keyString) {
        if (keyString == null)
            return null;
        return getDefaultSharedPreferences().getString(keyString, null);
    }

    /**
     * 存储String类型的键值对
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     * @param valueString
     */
    public static void putString(String keyString, String valueString) {
        if (keyString != null)
            getDefaultSharedPreferences().edit().putString(keyString, valueString).commit();
    }

    /**
     * 清空键名为 keyString 的键值对
     * 
     * @param keyString
     */
    public static void clearKey(String keyString) {
        if (!TextUtils.isEmpty(keyString))
            getDefaultSharedPreferences().edit().remove(keyString).commit();
//            getDefaultSharedPreferences().edit().clear().commit();
    }

    /**
     * 移除键名为 keyString 的键值对
     * 
     * @param paramContext
     * @param preferencesName
     * @param keyString
     */
    public static void removeKey(String keyString) {
        if (keyString != null)
            getDefaultSharedPreferences().edit().remove(keyString).commit();
    }

    /**
     * 获取int类型的键值，如获取不到则返回0
     * 
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return key == null ? 0 : getDefaultSharedPreferences().getInt(key, 0);
    }

    /**
     * 获取int类型的键值，需要指定默认值
     * 
     * @param context
     * @param prefName
     * @param key
     * @param defValue
     * @return Key value
     */
    public static int getInt(String key, int defValue) {
        return key == null ? defValue : getDefaultSharedPreferences().getInt(key, defValue);
    }

    /**
     * 存储int类型的键值对
     * 
     * @param context
     * @param prefName
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        if (key != null) {
            getDefaultSharedPreferences().edit().putInt(key, value).commit();
        }
    }

    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public static long getLong(String key) {
        return key == null ? 0L : getDefaultSharedPreferences().getLong(key, 0L);
    }

    public static long getLong(String key, long defValue) {
        return key == null ? defValue : getDefaultSharedPreferences().getLong(key, defValue);
    }

    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @param value
     */
    public static void putLong(String key, long value) {
        if (key != null) {
            getDefaultSharedPreferences().edit().putLong(key, value).commit();
        }
    }

    
    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @return
     */
    public static boolean getBoolean(String key) {
        return key == null ? false : getDefaultSharedPreferences().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return key == null ? defValue : getDefaultSharedPreferences().getBoolean(key, defValue);
    }
    
    /**
     * 
     * @param context
     * @param prefName
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        if (key != null) {
            getDefaultSharedPreferences().edit().putBoolean(key, value).commit();
        }
    }

}
