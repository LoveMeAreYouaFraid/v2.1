package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetActivitiesResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动列表Request
 */
public class GetActivitiesRequest extends InterfaceRequest<GetActivitiesResponse> {


    private static final String TAG = GetActivitiesRequest.class.getSimpleName();

    public GetActivitiesRequest(int start, int rows, ResponseListener<GetActivitiesResponse> listener) {
        super(Method.GET,Constant.URL.GET_ACTIVIES,"", RequestUtil.getActivePictureParams(start,rows), listener);
    }

    @Override
    protected Response<GetActivitiesResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivitiesResponse getActivitiesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivitiesResponse = new JsonUtil<GetActivitiesResponse>().json2Bean(
                        jsonString, GetActivitiesResponse.class.getName());
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
