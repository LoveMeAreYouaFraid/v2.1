package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserBoothResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 用户摊位Request
 */
public class GetUserBoothRequest extends InterfaceRequest<GetUserBoothResponse> {

    private static final String TAG = GetUserBoothRequest.class.getSimpleName();

    public GetUserBoothRequest(String userId,String orderId, ResponseListener<GetUserBoothResponse> listener) {
        super(Method.GET,Constant.URL.GET_USER_BOOTHS,userId, RequestUtil.getUserBoothsParams(orderId), listener);
    }

    @Override
    protected Response<GetUserBoothResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserBoothResponse getUserBoothResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserBoothResponse = new JsonUtil<GetUserBoothResponse>().json2Bean(
                        jsonString, GetUserBoothResponse.class.getName());
            }

            if (getUserBoothResponse != null) {
                CustomError customError = processResponse(getUserBoothResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserBoothResponse,
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
