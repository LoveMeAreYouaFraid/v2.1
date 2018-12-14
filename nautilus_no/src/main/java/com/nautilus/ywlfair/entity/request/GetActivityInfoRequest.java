package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetActivityInfoResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动详情Request
 */
public class GetActivityInfoRequest extends InterfaceRequest<GetActivityInfoResponse> {


    private static final String TAG = GetActivityInfoRequest.class.getSimpleName();

    public GetActivityInfoRequest(String itemId, ResponseListener<GetActivityInfoResponse> listener) {
        super(Method.GET,Constant.URL.GET_ACTIVITY,itemId,null, listener);
    }

    @Override
    protected Response<GetActivityInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivityInfoResponse getActivityInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivityInfoResponse = new JsonUtil<GetActivityInfoResponse>().json2Bean(
                        jsonString, GetActivityInfoResponse.class.getName());
            }

            if (getActivityInfoResponse != null) {
                CustomError customError = processResponse(getActivityInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivityInfoResponse,
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
