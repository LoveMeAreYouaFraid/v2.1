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
import com.nautilus.ywlfair.entity.response.GetScoreListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 积分明细列表Request
 */
public class GetScoreListRequest extends InterfaceRequest<GetScoreListResponse> {


    private static final String TAG = GetScoreListRequest.class.getSimpleName();

    public GetScoreListRequest(String userId, String startTime, String endTime, int queryType,int start,int rows,
                               ResponseListener<GetScoreListResponse> listener) {
        super(Method.GET,Constant.URL.GET_SCORE_LIST,userId,
                RequestUtil.getScoreListParams(startTime, endTime, queryType, start, rows), listener);
    }

    @Override
    protected Response<GetScoreListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetScoreListResponse getScoreListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getScoreListResponse = new JsonUtil<GetScoreListResponse>().json2Bean(
                        jsonString, GetScoreListResponse.class.getName());
            }

            if (getScoreListResponse != null) {
                CustomError customError = processResponse(getScoreListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getScoreListResponse,
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
