package com.nautilus.ywlfair.common.utils;

import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.entity.bean.UserInfo;

/**
 * Created by Administrator on 2016/5/20.
 */
public class GetUserInfoUtil {

    public static UserInfo getUserInfo(){
        UserInfo userInfo = null;

        userInfo = MyApplication.getInstance().getCurrentUser();

        if(userInfo == null){
            CacheUserInfo cacheUserInfo = new CacheUserInfo();

            userInfo = cacheUserInfo.getUserInfo();

            MyApplication.getInstance().setCurrentUser(userInfo);
        }

        if(userInfo == null){
            userInfo = new UserInfo();
        }

        return userInfo;
    }
}
