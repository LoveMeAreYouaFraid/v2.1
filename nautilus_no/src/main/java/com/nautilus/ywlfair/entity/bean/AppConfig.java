package com.nautilus.ywlfair.entity.bean;

/**
 * Created by Administrator on 2016/5/14.
 */
public class AppConfig{

    private static AppConfig mInstance;

    private AppConfig() {}

    private AccessConfig accessConfig;

    public static synchronized AppConfig getInstance() {
        if(mInstance == null) {
            mInstance = new AppConfig();
        }
        return mInstance;
    }

//    public String getBaseUrl(String interfaceName, String param){
//
//        if(TextUtils.isEmpty(interfaceName)){
//            if(Constant.API_TYPE == Constant.CONFIG.TEST_API){
//                return Constant.URL.TEST_BASE_API + Constant.URL.APP_CONFIG;
//            }else{
//                return Constant.URL.FORMAL_BASE_API + Constant.URL.APP_CONFIG;
//            }
//        }else{
//
//            if(accessConfig == null){
//                accessConfig = CacheUtil.getCache(Constant.PRE_KEY.APP_CONFIG, AccessConfig.class);
//            }
//
//            String baseUrl = accessConfig.getkNautilusFairApiURL();
//
//            if(!baseUrl.endsWith("/")){
//
//                baseUrl = baseUrl + "/";
//            }
//
//            baseUrl = baseUrl + "v1.1/";
//
//            return  baseUrl+ String.format(interfaceName, param);
//        }
//
//    }

    public AccessConfig getAccessConfig() {
        return accessConfig;
    }

    public void setAccessConfig(AccessConfig accessConfig) {
        this.accessConfig = accessConfig;
    }
}
