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
import com.nautilus.ywlfair.entity.response.GetHomePagerResponse;
import com.nautilus.ywlfair.entity.response.GetRecommendListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 推荐列表Request
 */
public class GetRecommendListRequest extends InterfaceRequest<GetRecommendListResponse> {


    private static final String TAG = GetRecommendListRequest.class.getSimpleName();

    public GetRecommendListRequest(int start, int rows, int type, ResponseListener<GetRecommendListResponse> listener) {
        super(Constant.URL.GET_RECOMMENDS,"", RequestUtil.getUserCommentParams(start, rows, type),listener);
    }

    @Override
    protected Response<GetRecommendListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetRecommendListResponse getRecommendListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getRecommendListResponse = new JsonUtil<GetRecommendListResponse>().json2Bean(
                        jsonString, GetRecommendListResponse.class.getName());
            }

            if (getRecommendListResponse != null) {
                CustomError customError = processResponse(getRecommendListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getRecommendListResponse,
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
