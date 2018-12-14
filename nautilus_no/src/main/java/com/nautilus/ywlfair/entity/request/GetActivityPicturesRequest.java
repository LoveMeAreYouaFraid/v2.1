package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetActivityPicturesResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动图片列表Request
 */
public class GetActivityPicturesRequest extends InterfaceRequest<GetActivityPicturesResponse> {


    private static final String TAG = GetActivityPicturesRequest.class.getSimpleName();

    public GetActivityPicturesRequest(String activeId,int start, int rows, ResponseListener<GetActivityPicturesResponse> listener) {
        super(Method.GET,Constant.URL.GET_ACTIVITY_PIC,activeId, RequestUtil.getActivePictureParams(start,rows), listener);
    }

    @Override
    protected Response<GetActivityPicturesResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivityPicturesResponse getActivityPicturesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivityPicturesResponse = new JsonUtil<GetActivityPicturesResponse>().json2Bean(
                        jsonString, GetActivityPicturesResponse.class.getName());
            }

            if (getActivityPicturesResponse != null) {
                CustomError customError = processResponse(getActivityPicturesResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivityPicturesResponse,
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
