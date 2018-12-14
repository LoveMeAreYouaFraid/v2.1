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
import com.nautilus.ywlfair.entity.response.GetActivityPicturesResponse;
import com.nautilus.ywlfair.entity.response.GetGoodsListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 商品列表Request
 */
public class GetGoodsListRequest extends InterfaceRequest<GetGoodsListResponse> {


    private static final String TAG = GetGoodsListRequest.class.getSimpleName();

    public GetGoodsListRequest(String classId,int level, int start, int rows, ResponseListener<GetGoodsListResponse> listener) {
        super(Method.GET,Constant.URL.GET_GOODS_LIST,"", RequestUtil.getGoodsListParams(classId, level, start, rows), listener);
    }

    @Override
    protected Response<GetGoodsListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetGoodsListResponse getGoodsListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getGoodsListResponse = new JsonUtil<GetGoodsListResponse>().json2Bean(
                        jsonString, GetGoodsListResponse.class.getName());
            }

            if (getGoodsListResponse != null) {
                CustomError customError = processResponse(getGoodsListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getGoodsListResponse,
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
