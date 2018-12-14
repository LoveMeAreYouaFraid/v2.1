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
import com.nautilus.ywlfair.entity.response.GetStallOrderInfoListResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/6/25.
 */

public class GetStallOrderInfoListRequest extends InterfaceRequest<GetStallOrderInfoListResponse> {
    private static final String TAG = GetStallOrderInfoListRequest.class.getSimpleName();

    public GetStallOrderInfoListRequest(String userId, String orderStatus, String start, String rows, ResponseListener<GetStallOrderInfoListResponse> listener) {

        super(Method.GET, Constant.URL.GET_VENDOR_OFF_LINE_ORDERS, userId, RequestUtil.GetStallOrderInfoListParams(orderStatus, start, rows), listener);
    }


    @Override
    protected Response<GetStallOrderInfoListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetStallOrderInfoListResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<GetStallOrderInfoListResponse>().json2Bean(
                        jsonString, GetStallOrderInfoListResponse.class.getName());
            }

            if (interfaceResponse != null) {
                CustomError customError = processResponse(interfaceResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            interfaceResponse,
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
