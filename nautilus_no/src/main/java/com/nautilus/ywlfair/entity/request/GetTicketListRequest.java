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
import com.nautilus.ywlfair.entity.response.GetBoothListResponse;
import com.nautilus.ywlfair.entity.response.GetTicketListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 活动门票列表Request
 */
public class GetTicketListRequest extends InterfaceRequest<GetTicketListResponse> {

    private static final String TAG = GetTicketListRequest.class.getSimpleName();

    public GetTicketListRequest(String actId, ResponseListener<GetTicketListResponse> listener) {
        super(Method.GET,Constant.URL.ACTIVE_TICKETS,actId, null, listener);
    }

    @Override
    protected Response<GetTicketListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetTicketListResponse getTicketListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getTicketListResponse = new JsonUtil<GetTicketListResponse>().json2Bean(
                        jsonString, GetTicketListResponse.class.getName());
            }

            if (getTicketListResponse != null) {
                CustomError customError = processResponse(getTicketListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getTicketListResponse,
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
