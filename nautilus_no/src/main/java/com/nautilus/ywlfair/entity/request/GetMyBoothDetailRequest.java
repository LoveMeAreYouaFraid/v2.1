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
import com.nautilus.ywlfair.entity.response.GetBoothDetailResponse;
import com.nautilus.ywlfair.entity.response.GetBoothListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 摊位详情Request
 */
public class GetMyBoothDetailRequest extends InterfaceRequest<GetBoothDetailResponse> {


    private static final String TAG = GetMyBoothDetailRequest.class.getSimpleName();

    public GetMyBoothDetailRequest(String userId, String orderId, ResponseListener<GetBoothDetailResponse> listener) {
        super(Method.GET,Constant.URL.GET_BOOTH_DETAIL,userId, RequestUtil.getBoothDetailParams(orderId), listener);
    }

    @Override
    protected Response<GetBoothDetailResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetBoothDetailResponse getBoothDetailResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getBoothDetailResponse = new JsonUtil<GetBoothDetailResponse>().json2Bean(
                        jsonString, GetBoothDetailResponse.class.getName());
            }

            if (getBoothDetailResponse != null) {
                CustomError customError = processResponse(getBoothDetailResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getBoothDetailResponse,
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
