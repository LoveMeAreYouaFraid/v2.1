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
import com.nautilus.ywlfair.entity.response.GetGoodsDetailResponse;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 商品详情Request
 */
public class GetGoodsDetailRequest extends InterfaceRequest<GetGoodsDetailResponse> {


    private static final String TAG = GetGoodsDetailRequest.class.getSimpleName();

    public GetGoodsDetailRequest(String goodsId, ResponseListener<GetGoodsDetailResponse> listener) {
        super(Method.GET, Constant.URL.GET_GOODS_DETAIL,goodsId,null, listener);
    }

    @Override
    protected Response<GetGoodsDetailResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetGoodsDetailResponse getGoodsDetailResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getGoodsDetailResponse = new JsonUtil<GetGoodsDetailResponse>().json2Bean(
                        jsonString, GetGoodsDetailResponse.class.getName());
            }

            if (getGoodsDetailResponse != null) {
                CustomError customError = processResponse(getGoodsDetailResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getGoodsDetailResponse,
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
