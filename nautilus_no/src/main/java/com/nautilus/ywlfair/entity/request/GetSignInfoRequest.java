package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.PostUserSignResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动分享信息Request
 */
public class GetSignInfoRequest extends InterfaceRequest<PostUserSignResponse> {


    private static final String TAG = GetSignInfoRequest.class.getSimpleName();

    public GetSignInfoRequest(String userId, String activityId, ResponseListener<PostUserSignResponse> listener) {
        super(Method.GET, Constant.URL.POST_USER_SIGN, userId, RequestUtil.getGetMySiginParams(activityId), listener);
    }

    @Override
    protected Response<PostUserSignResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostUserSignResponse getShareInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getShareInfoResponse = new JsonUtil<PostUserSignResponse>().json2Bean(
                        jsonString, PostUserSignResponse.class.getName());
            }

            if (getShareInfoResponse != null) {
                CustomError customError = processResponse(getShareInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getShareInfoResponse,
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
