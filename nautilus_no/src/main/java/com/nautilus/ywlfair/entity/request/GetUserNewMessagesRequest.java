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
import com.nautilus.ywlfair.entity.response.GetUserMessagesResponse;
import com.nautilus.ywlfair.entity.response.GetUserOrdersResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2016/4/29.
 */
public class GetUserNewMessagesRequest extends InterfaceRequest<GetUserMessagesResponse> {


    private static final String TAG = GetUserMessagesResponse.class.getSimpleName();

    public GetUserNewMessagesRequest(String userId, String type, String readStatus, String start, String rows,
                                     ResponseListener<GetUserMessagesResponse> listener) {
        super(Method.GET, Constant.URL.GET_USER_NEW_MESSAGES, userId, RequestUtil.getUserNewMessagesParams(type, readStatus, start, rows), listener);
    }

    @Override
    protected Response<GetUserMessagesResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            Log.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserMessagesResponse getUserOrdersResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserOrdersResponse = new JsonUtil<GetUserMessagesResponse>().json2Bean(
                        jsonString, GetUserMessagesResponse.class.getName());
            }

            if (getUserOrdersResponse != null) {
                CustomError customError = processResponse(getUserOrdersResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserOrdersResponse,
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
