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
import com.nautilus.ywlfair.entity.response.GetSignListResponse;
import com.nautilus.ywlfair.entity.response.GetUserActivityResponse;
import com.nautilus.ywlfair.entity.response.MyTicketsListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 普通用户活动列表
 * Created by Administrator on 2016/4/22.
 */
public class GetUserActivityListRequest extends InterfaceRequest<GetUserActivityResponse> {
    private static final String TAG = GetSignListResponse.class.getSimpleName();

    public GetUserActivityListRequest(String type, String start, String rows, ResponseListener<GetUserActivityResponse> listener) {
        super(Method.GET, Constant.URL.GET_ACTIVITYS_USER, null, RequestUtil.getMyTicketsListParams(type, start, rows), listener);
    }

    @Override
    protected Response<GetUserActivityResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserActivityResponse getActivitiesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivitiesResponse = new JsonUtil<GetUserActivityResponse>().json2Bean(
                        jsonString, GetUserActivityResponse.class.getName());
            }

            if (getActivitiesResponse != null) {
                CustomError customError = processResponse(getActivitiesResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivitiesResponse,
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