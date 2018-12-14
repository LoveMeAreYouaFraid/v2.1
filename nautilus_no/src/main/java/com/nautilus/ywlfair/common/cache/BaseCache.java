package com.nautilus.ywlfair.common.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.entity.bean.UserInfo;

public class BaseCache
{
	
	public static String	TAG;
	public static String	DATATAG;
	
	public SharedPreferences getSharedPreferences(String TAG)
	{
		return MyApplication.getInstance().getSharedPreferences(TAG,Context.MODE_PRIVATE);
	}
	
	protected final void setStr(String saveJson)
	{
		
		SharedPreferences sharedPreferences = getSharedPreferences(TAG);
		sharedPreferences.edit().putString(DATATAG, saveJson).commit();
	}
	
	
	
	public final String getStr()
	{
		SharedPreferences sharedPreferences = getSharedPreferences(TAG);
		return sharedPreferences.getString(DATATAG, "");
	}
	
	/**
	 * 根据ID存储数据
	 * 
	 * @param saveJson
	 * @param id
	 */
	protected final void setStr(String saveJson, String id)
    {
        
        SharedPreferences sharedPreferences = getSharedPreferences(TAG);
        sharedPreferences.edit().putString(DATATAG + "_" + id, saveJson).commit();
    }
    
	/**
	 * 根据ID读取数据
	 * 
	 * @param id
	 * @return
	 */
    public final String getStr(String id)
    {
        SharedPreferences sharedPreferences = getSharedPreferences(TAG);
        return sharedPreferences.getString(DATATAG + "_" + id, "");
    }
	
	public static  String getUserFlag()
	{
		UserInfo eduUser = null;
		String userFlag = "";
		if (MyApplication.getInstance().getCurrentUser() != null) {
			eduUser  = MyApplication.getInstance().getCurrentUser();
		}else {
			eduUser = new CacheUserInfo().getUserInfo();
		}
		if(eduUser != null){
			userFlag  = eduUser.getUserId()+"";
		}
		return userFlag;
	}
	
	public void clearData()
	{
		SharedPreferences sharedPreferences = getSharedPreferences(TAG);
		sharedPreferences.edit().clear().commit();
	}
	
}
