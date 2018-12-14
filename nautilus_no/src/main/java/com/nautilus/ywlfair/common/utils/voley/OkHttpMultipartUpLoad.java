package com.nautilus.ywlfair.common.utils.voley;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.MD5Utils;
import com.nautilus.ywlfair.entity.bean.PostPictureResponse;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/6/3.
 */
public class OkHttpMultipartUpLoad {

    private static OkHttpMultipartUpLoad mInstance;

    public static OkHttpMultipartUpLoad getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpMultipartUpLoad.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpMultipartUpLoad();
                }
            }
        }

        return mInstance;
    }

    private static final String NAUTILUS_URL_POST_PICTURE = "resource/picture";

    public void postPictures(String type, String filePath,
                             UploadFileCallBack callback) {
        String urlString =null;
//                AppConfig.getInstance().getBaseUrl(NAUTILUS_URL_POST_PICTURE, "") + "?type=" + type;

        systemTime = System.currentTimeMillis() + "";

        if (!TextUtils.isEmpty(filePath))
            postFile(urlString, filePath, callback);
    }

    String boundary = "*****";

    private void postFile(String actionUrl, String filePath,
                          final UploadFileCallBack callback) {

        callback.uploadStart();

        try {
            File file = new File(filePath);

            RequestBody requestBody = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
                    .addPart(MultipartBody.Part.createFormData("file", file.getName(),
                            RequestBody.create(MultipartBody.FORM, file)))
                    .build();

            Request request = new Request.Builder()
                    .url(actionUrl)
                    .post(requestBody)
                    .headers(Headers.of(getParamsWithoutUpdateTime()))
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    callback.uploadError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonString = response.body().string();

                    LogUtil.e("post file", jsonString + "");

                    PostPictureResponse postPictureResponse = null;

                    if(!TextUtils.isEmpty(jsonString)){
                        postPictureResponse = new JsonUtil<PostPictureResponse>().json2Bean(jsonString, PostPictureResponse.class.getName());
                    }

                    if(postPictureResponse == null || postPictureResponse.getStatus() != 0
                            || postPictureResponse.getResult() == null){
                        callback.uploadError(new Exception(postPictureResponse.getMessage()));
                    }else{
                        callback.uploadSuccess(postPictureResponse.getResult());
                    }

                }
            });

        } catch (Exception e) {
            callback.uploadError(e);
        }

    }

    private final OkHttpClient client = new OkHttpClient();

    private String systemTime;

    private Map<String, String> getParams(String accessToken) {

        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("appName", "NautilusFair");

        paramMap.put("version", Common.getInstance().getVersionCode() + "");

        paramMap.put("deviceType", "2");

        paramMap.put("deviceName", Common.getInstance().getModel());

        paramMap.put("deviceOs", Common.getInstance().getOsReleaseVersion());

        paramMap.put("serialNo", Common.getInstance().getIMSI());

        paramMap.put("time", systemTime);

        paramMap.put("accessToken", accessToken);

        return paramMap;
    }

    private String getSignature(String accessToken) {

        Map<String, String> paramMap = getParams(accessToken);

        Collection<String> keyset = paramMap.keySet();

        List<String> list = new ArrayList<>(keyset);

        // 对key键值按字典升序排序
        Collections.sort(list);

        String strJoins = "";
        for (int i = 0; i < list.size(); i++) {
            strJoins += list.get(i) + "=" + paramMap.get(list.get(i));
        }

        String secret = Constant.SIGNATURE_SECRET;

        String md5 = "";

        md5 = MD5Utils.md5(secret + strJoins);

        return md5;

    }

    /**
     * 处理参数列表，移除值为null的参数等
     *
     * @return
     * @throws AuthFailureError
     */
    public Map<String, String> getParamsWithoutUpdateTime() throws AuthFailureError {

        Map<String, String> mParams = getParams(MyApplication.getInstance().getAccessToken());

        mParams.put("signature", getSignature(MyApplication.getInstance().getAccessToken()));

        mParams.put("Content-Type", "application/x-www-form-urlencoded");
        // 移除params中键值为null的键值对，防止Volley报错
        Iterator<Map.Entry<String, String>> it = mParams.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String value = entry.getValue();
            if (value == null) {
                it.remove();
            }
        }
        LogUtil.e("params", mParams.toString());

        return mParams;
    }

    public interface UploadFileCallBack {
        void uploadStart();

        void uploadSuccess(UpLoadPicInfo upLoadPicInfo);

        void uploadError(Exception e);
    }
}
