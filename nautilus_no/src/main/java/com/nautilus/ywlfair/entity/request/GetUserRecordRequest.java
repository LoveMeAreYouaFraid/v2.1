package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserRecordResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动分享信息Request
 */
public class GetUserRecordRequest extends InterfaceRequest<GetUserRecordResponse> {


    private static final String TAG = GetUserRecordRequest.class.getSimpleName();

    public GetUserRecordRequest(String userId,ResponseListener<GetUserRecordResponse> listener) {
        super(Method.GET,Constant.URL.GET_USER_RECORDS,userId,null, listener);
    }

    @Override
    protected Response<GetUserRecordResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserRecordResponse getUserRecordResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserRecordResponse = new JsonUtil<GetUserRecordResponse>().json2Bean(
                        jsonString, GetUserRecordResponse.class.getName());
            }

            if (getUserRecordResponse != null) {
                CustomError customError = processResponse(getUserRecordResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserRecordResponse,
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
