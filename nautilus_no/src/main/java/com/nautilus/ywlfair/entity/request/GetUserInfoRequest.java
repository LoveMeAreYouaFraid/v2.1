package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetAccessTokenResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 获取用户信息Request
 */
public class GetUserInfoRequest extends InterfaceRequest<GetAccessTokenResponse> {


    private static final String TAG = GetUserInfoRequest.class.getSimpleName();

    public GetUserInfoRequest(String userId, ResponseListener<GetAccessTokenResponse> listener) {
        super(Constant.URL.GET_USER_INFO, userId, null, listener);
    }

    @Override
    protected Response<GetAccessTokenResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetAccessTokenResponse getAccessTokenResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAccessTokenResponse = new JsonUtil<GetAccessTokenResponse>().json2Bean(
                        jsonString, GetAccessTokenResponse.class.getName());
            }

            if (getAccessTokenResponse != null) {
                CustomError customError = processResponse(getAccessTokenResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAccessTokenResponse,
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
