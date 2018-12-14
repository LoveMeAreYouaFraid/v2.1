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
import com.nautilus.ywlfair.entity.response.GetGoodsClassResponse;
import com.nautilus.ywlfair.entity.response.GetValidOrderResponse;

import java.io.UnsupportedEncodingException;

/**
 * 确认订单Request
 */
public class GetValidOrderRequest extends InterfaceRequest<GetValidOrderResponse> {


    private static final String TAG = GetValidOrderRequest.class.getSimpleName();

    public GetValidOrderRequest(String orderId,String channel,ResponseListener<GetValidOrderResponse> listener) {
        super(Constant.URL.GET_VALID_ORDER, orderId, RequestUtil.getValidOrderParams(channel), listener);
    }

    @Override
    protected Response<GetValidOrderResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetValidOrderResponse getValidOrderResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getValidOrderResponse = new JsonUtil<GetValidOrderResponse>().json2Bean(
                        jsonString, GetValidOrderResponse.class.getName());
            }

            if (getValidOrderResponse != null) {
                CustomError customError = processResponse(getValidOrderResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getValidOrderResponse,
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
