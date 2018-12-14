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
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetStallOrderInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 活动状态Request
 */
public class GetOrderStallRequest extends InterfaceRequest<GetStallOrderInfoResponse> {


    private static final String TAG = GetOrderStallRequest.class.getSimpleName();

    public GetOrderStallRequest(String orderId, ResponseListener<GetStallOrderInfoResponse> listener) {
        super(Method.GET, Constant.URL.GET_OFF_LINE_ORDER, orderId, null, listener);
    }

    @Override
    protected Response<GetStallOrderInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetStallOrderInfoResponse getAgctiveStatusResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAgctiveStatusResponse = new JsonUtil<GetStallOrderInfoResponse>().json2Bean(
                        jsonString, GetStallOrderInfoResponse.class.getName());
            }

            if (getAgctiveStatusResponse != null) {
                CustomError customError = processResponse(getAgctiveStatusResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAgctiveStatusResponse,
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
