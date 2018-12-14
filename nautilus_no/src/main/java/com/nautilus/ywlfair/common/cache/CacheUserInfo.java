package com.nautilus.ywlfair.common.cache;

import android.text.TextUtils;

import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.common.utils.JsonUtil;

public class CacheUserInfo extends BaseCache
{
	
	public CacheUserInfo()
	{
		TAG = "EduUserInfoCache";
		DATATAG = "EduUserInfoTag";
	}
	
	public final void setUserInfo(UserInfo bean)
	{
		clearData();
		setStr(new JsonUtil<UserInfo>().bean2Json(bean));
	}
	
	public final UserInfo getUserInfo()
	{
		String json = getStr();
        if(!TextUtils.isEmpty(json)) {
            return new JsonUtil<UserInfo>().json2Bean(json, UserInfo.class.getName());
        }
        return null;
	}
}
