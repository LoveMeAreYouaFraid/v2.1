package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserStatisticResponse;
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
public class GetUserStatisticRequest extends InterfaceRequest<GetUserStatisticResponse> {


    private static final String TAG = GetUserStatisticRequest.class.getSimpleName();

    public GetUserStatisticRequest(String userId, ResponseListener<GetUserStatisticResponse> listener) {
        super(Method.GET,Constant.URL.GET_USER_STATISTICS,userId,null, listener);
    }

    @Override
    protected Response<GetUserStatisticResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserStatisticResponse getUserStatisticResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserStatisticResponse = new JsonUtil<GetUserStatisticResponse>().json2Bean(
                        jsonString, GetUserStatisticResponse.class.getName());
            }

            if (getUserStatisticResponse != null) {
                CustomError customError = processResponse(getUserStatisticResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserStatisticResponse,
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
