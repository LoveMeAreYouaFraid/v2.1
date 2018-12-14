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
import com.nautilus.ywlfair.entity.response.GetGoodsHomePagerResponse;
import com.nautilus.ywlfair.entity.response.GetHomePagerResponse;

import java.io.UnsupportedEncodingException;

/**
 * 商品首页信息Request
 */
public class GetGoodsHomePagerRequest extends InterfaceRequest<GetGoodsHomePagerResponse> {


    private static final String TAG = GetGoodsHomePagerRequest.class.getSimpleName();

    public GetGoodsHomePagerRequest(ResponseListener<GetGoodsHomePagerResponse> listener) {
        super(Constant.URL.GET_GOODS_HOMEPAGER, "", null , listener);
    }

    @Override
    protected Response<GetGoodsHomePagerResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetGoodsHomePagerResponse getGoodsHomePagerResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getGoodsHomePagerResponse = new JsonUtil<GetGoodsHomePagerResponse>().json2Bean(
                        jsonString, GetGoodsHomePagerResponse.class.getName());
            }

            if (getGoodsHomePagerResponse != null) {
                CustomError customError = processResponse(getGoodsHomePagerResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getGoodsHomePagerResponse,
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
