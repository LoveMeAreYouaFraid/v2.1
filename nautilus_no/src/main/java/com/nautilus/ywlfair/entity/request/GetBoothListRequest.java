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
import com.nautilus.ywlfair.entity.response.GetBoothListResponse;
import com.nautilus.ywlfair.entity.response.GetGoodsListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 摊位列表Request
 */
public class GetBoothListRequest extends InterfaceRequest<GetBoothListResponse> {


    private static final String TAG = GetBoothListRequest.class.getSimpleName();

    public GetBoothListRequest(String userId, int type, int start, int rows, ResponseListener<GetBoothListResponse> listener) {
        super(Method.GET,Constant.URL.GET_BOOTH_LIST,userId, RequestUtil.getBoothListParams(type, start, rows), listener);
    }

    @Override
    protected Response<GetBoothListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetBoothListResponse getBoothListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getBoothListResponse = new JsonUtil<GetBoothListResponse>().json2Bean(
                        jsonString, GetBoothListResponse.class.getName());
            }

            if (getBoothListResponse != null) {
                CustomError customError = processResponse(getBoothListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getBoothListResponse,
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
