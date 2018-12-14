package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动分享信息Request
 */
public class PutUserInfoRequest extends InterfaceRequest<PutUserInfoResponse> {


    private static final String TAG = PutUserInfoRequest.class.getSimpleName();

    public PutUserInfoRequest(String userId, String avatar, String nickName, String sex, String city, String signature,
                              ResponseListener<PutUserInfoResponse> listener) {
        super(Method.PUT,Constant.URL.GET_USER_INFO,userId,
                RequestUtil.getPutUserInfoParams(avatar, nickName, sex, city, signature), listener);
    }

    @Override
    protected Response<PutUserInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PutUserInfoResponse putUserInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                putUserInfoResponse = new JsonUtil<PutUserInfoResponse>().json2Bean(
                        jsonString, PutUserInfoResponse.class.getName());
            }

            if (putUserInfoResponse != null) {
                CustomError customError = processResponse(putUserInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            putUserInfoResponse,
                            ForceCacheHttpHeaderParser.parseCacheHeaders(response, 100));
//                            HttpHeaderParser.parseCacheHeaders(response));
                }
            } else {
                return Response
                        .error(new ParseError(new Throwable("can not convert respond data.")));
            }

        } catch (UnsupportedEncodingException e) {
            Log.w(TAG, "parseNetworkResponse UnsupportedEncodingException: "
                    + new String(response.data));
            return Response.error(new ParseError(e));
        }
    }

}
