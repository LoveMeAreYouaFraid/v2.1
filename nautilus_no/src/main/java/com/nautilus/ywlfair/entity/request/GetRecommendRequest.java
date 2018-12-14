package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetRecommendResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 获取推荐列表Request
 */
public class GetRecommendRequest extends InterfaceRequest<GetRecommendResponse> {


    private static final String TAG = GetRecommendRequest.class.getSimpleName();

    public GetRecommendRequest(int start, int rows, ResponseListener<GetRecommendResponse> listener) {
        super(Constant.URL.GET_RECOMMENDS, "", RequestUtil.getRecommendParams(start, rows), listener);
    }

    @Override
    protected Response<GetRecommendResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetRecommendResponse getRecommendResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getRecommendResponse = new JsonUtil<GetRecommendResponse>().json2Bean(
                        jsonString, GetRecommendResponse.class.getName());
            }

            if (getRecommendResponse != null) {
                CustomError customError = processResponse(getRecommendResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getRecommendResponse,
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
