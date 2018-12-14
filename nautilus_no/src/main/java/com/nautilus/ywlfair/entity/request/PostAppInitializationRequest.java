package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetAppInitializationResponse;

import java.io.UnsupportedEncodingException;

/**
 * 初始化Request
 */
public class PostAppInitializationRequest extends InterfaceRequest<GetAppInitializationResponse> {


    private static final String TAG = PostAppInitializationRequest.class.getSimpleName();

    public PostAppInitializationRequest(String deviceId,ResponseListener<GetAppInitializationResponse> listener) {
        super(Method.POST,Constant.URL.GET_APP_INITIALIZATION,"", RequestUtil.getInitializationParams(deviceId), listener);
    }

    @Override
    protected Response<GetAppInitializationResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetAppInitializationResponse getAppInitializationRequest = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAppInitializationRequest = new JsonUtil<GetAppInitializationResponse>().json2Bean(
                        jsonString, GetAppInitializationResponse.class.getName());
            }

            if (getAppInitializationRequest != null) {
                CustomError customError = processResponse(getAppInitializationRequest);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAppInitializationRequest,
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
