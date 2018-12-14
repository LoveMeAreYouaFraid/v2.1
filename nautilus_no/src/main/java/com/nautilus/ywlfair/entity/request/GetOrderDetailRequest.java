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
import com.nautilus.ywlfair.entity.response.GetGoodsDetailResponse;
import com.nautilus.ywlfair.entity.response.GetOrderDetailResponse;

import java.io.UnsupportedEncodingException;

/**
 * 订单详情Request
 */
public class GetOrderDetailRequest extends InterfaceRequest<GetOrderDetailResponse> {


    private static final String TAG = GetOrderDetailRequest.class.getSimpleName();

    public GetOrderDetailRequest(String orderId, String orderType, ResponseListener<GetOrderDetailResponse> listener) {
        super(Method.GET, Constant.URL.GET_ORDER_DETAIL,orderId, RequestUtil.getOrderDetailParams(orderType), listener);
    }

    @Override
    protected Response<GetOrderDetailResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetOrderDetailResponse getOrderDetailResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getOrderDetailResponse = new JsonUtil<GetOrderDetailResponse>().json2Bean(
                        jsonString, GetOrderDetailResponse.class.getName());
            }

            if (getOrderDetailResponse != null) {
                CustomError customError = processResponse(getOrderDetailResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getOrderDetailResponse,
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
