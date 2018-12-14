package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetActivityBoothApplicationConfigResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

/**
 * 活动报名初始化
 * Created by Administrator on 2016/3/3.
 */
public class GetActivityBoothApplicationConfigRequest extends InterfaceRequest<GetActivityBoothApplicationConfigResponse> {
    private static final String TAG = GetActivityCommentsRequest.class.getSimpleName();

    public GetActivityBoothApplicationConfigRequest(String activityId,String roundId, ResponseListener<GetActivityBoothApplicationConfigResponse> listener) {
        super(Request.Method.GET, Constant.URL.GET_ACTIVITY_BOOTH_APPLICATION_CONFIG,
                activityId, RequestUtil.getBoothApplyInit(roundId), listener);
    }

    @Override
    protected Response<GetActivityBoothApplicationConfigResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivityBoothApplicationConfigResponse getActivityInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                    LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);
                    getActivityInfoResponse = new JsonUtil<GetActivityBoothApplicationConfigResponse>().json2Bean(
                            jsonString, GetActivityBoothApplicationConfigResponse.class.getName());
            }

            if (getActivityInfoResponse != null) {
                CustomError customError = processResponse(getActivityInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivityInfoResponse,
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
