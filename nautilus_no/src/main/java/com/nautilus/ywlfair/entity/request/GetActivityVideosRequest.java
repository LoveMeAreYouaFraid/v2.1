package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetActivityVideosResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动图片视频列表Request
 */
public class GetActivityVideosRequest extends InterfaceRequest<GetActivityVideosResponse> {


    private static final String TAG = GetActivityVideosRequest.class.getSimpleName();

    public GetActivityVideosRequest(String activeId, ResponseListener<GetActivityVideosResponse> listener) {
        super(Method.GET,Constant.URL.GET_ACTIVITY_VIDEOS,activeId, null, listener);
    }

    @Override
    protected Response<GetActivityVideosResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivityVideosResponse getActivityVideosResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivityVideosResponse = new JsonUtil<GetActivityVideosResponse>().json2Bean(
                        jsonString, GetActivityVideosResponse.class.getName());
            }

            if (getActivityVideosResponse != null) {
                CustomError customError = processResponse(getActivityVideosResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivityVideosResponse,
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
