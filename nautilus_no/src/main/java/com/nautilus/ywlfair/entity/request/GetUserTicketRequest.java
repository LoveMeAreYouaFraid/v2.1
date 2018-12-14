package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetUserTicketResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 用户门票Request
 */
public class GetUserTicketRequest extends InterfaceRequest<GetUserTicketResponse> {


    private static final String TAG = GetUserTicketRequest.class.getSimpleName();

    public GetUserTicketRequest(String userId,String activeId, ResponseListener<GetUserTicketResponse> listener) {
        super(Method.GET,Constant.URL.GET_USER_TICKETS,userId, RequestUtil.getUserBoothsParams(activeId), listener);
    }

    @Override
    protected Response<GetUserTicketResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetUserTicketResponse getUserTicketResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserTicketResponse = new JsonUtil<GetUserTicketResponse>().json2Bean(
                        jsonString, GetUserTicketResponse.class.getName());
            }

            if (getUserTicketResponse != null) {
                CustomError customError = processResponse(getUserTicketResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserTicketResponse,
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
