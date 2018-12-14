package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserMessagesResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 获取用户消息Request
 */
public class GetUserMessagesRequest extends InterfaceRequest<GetUserMessagesResponse> {


    private static final String TAG = GetUserMessagesRequest.class.getSimpleName();

    public GetUserMessagesRequest(String userId, int type, int readStatus, int start, int rows,
                                  ResponseListener<GetUserMessagesResponse> listener) {
        super(Method.GET,Constant.URL.GET_USER_MESSAGES,userId,
                RequestUtil.getUserMessagesParams(type, readStatus, start, rows), listener);
    }

    @Override
    protected Response<GetUserMessagesResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserMessagesResponse getUserMessagesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserMessagesResponse = new JsonUtil<GetUserMessagesResponse>().json2Bean(
                        jsonString, GetUserMessagesResponse.class.getName());
            }

            if (getUserMessagesResponse != null) {
                CustomError customError = processResponse(getUserMessagesResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserMessagesResponse,
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
