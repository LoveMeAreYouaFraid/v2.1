package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserOrdersResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 获取普通用户订单Request
 */
public class GetUserOrdersRequest extends InterfaceRequest<GetUserOrdersResponse> {


    private static final String TAG = GetUserOrdersRequest.class.getSimpleName();

    public GetUserOrdersRequest(String userId, String orderStatus, int start, int rows, String orderType,
                                ResponseListener<GetUserOrdersResponse> listener) {
        super(Method.GET, Constant.URL.GET_USER_ORDERS, userId,
                RequestUtil.getUserOrdersParams(orderStatus, start, rows, orderType), listener);
    }

    @Override
    protected Response<GetUserOrdersResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserOrdersResponse getUserOrdersResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserOrdersResponse = new JsonUtil<GetUserOrdersResponse>().json2Bean(
                        jsonString, GetUserOrdersResponse.class.getName());
            }

            if (getUserOrdersResponse != null) {
                CustomError customError = processResponse(getUserOrdersResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserOrdersResponse,
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
