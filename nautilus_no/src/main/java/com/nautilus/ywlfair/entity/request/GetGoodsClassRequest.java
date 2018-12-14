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
import com.nautilus.ywlfair.entity.response.GetGoodsClassResponse;
import com.nautilus.ywlfair.entity.response.GetGoodsHomePagerResponse;

import java.io.UnsupportedEncodingException;

/**
 * 商品分类Request
 */
public class GetGoodsClassRequest extends InterfaceRequest<GetGoodsClassResponse> {


    private static final String TAG = GetGoodsClassRequest.class.getSimpleName();

    public GetGoodsClassRequest(ResponseListener<GetGoodsClassResponse> listener) {
        super(Constant.URL.GET_GOODS_CLASS, "", null , listener);
    }

    @Override
    protected Response<GetGoodsClassResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetGoodsClassResponse getGoodsClassResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getGoodsClassResponse = new JsonUtil<GetGoodsClassResponse>().json2Bean(
                        jsonString, GetGoodsClassResponse.class.getName());
            }

            if (getGoodsClassResponse != null) {
                CustomError customError = processResponse(getGoodsClassResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getGoodsClassResponse,
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
