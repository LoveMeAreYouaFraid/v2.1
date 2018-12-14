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
import com.nautilus.ywlfair.entity.response.GetOriginalCreateListResponse;
import com.nautilus.ywlfair.entity.response.GetRecommendListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 推荐列表Request
 */
public class GetOriginalCreateRequest extends InterfaceRequest<GetOriginalCreateListResponse> {


    private static final String TAG = GetOriginalCreateRequest.class.getSimpleName();

    public GetOriginalCreateRequest(int start, int rows, int type, ResponseListener<GetOriginalCreateListResponse> listener) {
        super(Constant.URL.GET_RECOMMEND_ARTICLES, "", RequestUtil.getUserCommentParams(start, rows, type) , listener);
    }

    @Override
    protected Response<GetOriginalCreateListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetOriginalCreateListResponse getOriginalCreateListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getOriginalCreateListResponse = new JsonUtil<GetOriginalCreateListResponse>().json2Bean(
                        jsonString, GetOriginalCreateListResponse.class.getName());
            }

            if (getOriginalCreateListResponse != null) {
                CustomError customError = processResponse(getOriginalCreateListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getOriginalCreateListResponse,
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
