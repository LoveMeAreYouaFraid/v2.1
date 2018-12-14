package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.PostUserOrderResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 创建订单Request
 */
public class PostCreateOrderRequest extends InterfaceRequest<PostUserOrderResponse> {


    private static final String TAG = PostCreateOrderRequest.class.getSimpleName();

    public PostCreateOrderRequest(String userId, String itemType, String skuId, String itemPrice, String itemNum,
                                  String phone, String channel, String userMessage, String addressId, String orderNo, String boothId,
                                  String vendorUserId, String payType, String score, ResponseListener<PostUserOrderResponse> listener) {
        super(Method.POST, Constant.URL.POST_CREATE_ORDER, userId,
                RequestUtil.getCreateOrderParams(itemType, skuId, itemPrice, itemNum,
                        phone, channel, userMessage, addressId, orderNo, boothId, vendorUserId, payType, score), listener);
    }

    @Override
    protected Response<PostUserOrderResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostUserOrderResponse postUserOrderResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                postUserOrderResponse = new JsonUtil<PostUserOrderResponse>().json2Bean(
                        jsonString, PostUserOrderResponse.class.getName());
            }

            if (postUserOrderResponse != null) {
                CustomError customError = processResponse(postUserOrderResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            postUserOrderResponse,
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
