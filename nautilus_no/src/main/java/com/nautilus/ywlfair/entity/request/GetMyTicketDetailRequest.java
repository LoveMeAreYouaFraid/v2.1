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
import com.nautilus.ywlfair.entity.response.GetMyTicketDetailResponse;

import java.io.UnsupportedEncodingException;

/**
 * 门票详情
 * Created by Administrator on 2016/4/28.
 */
public class GetMyTicketDetailRequest extends InterfaceRequest<GetMyTicketDetailResponse> {
    private static final String TAG = GetMyTicketDetailRequest.class.getSimpleName();

    public GetMyTicketDetailRequest(String userId, String orderId, ResponseListener<GetMyTicketDetailResponse> listener) {
        super(Method.GET, Constant.URL.GET_ACTIVITY_MY_TICKETINFO , userId,  RequestUtil.getMyTicketsDetailParams(orderId), listener);
    }

    @Override
    protected Response<GetMyTicketDetailResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetMyTicketDetailResponse getActivitiesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivitiesResponse = new JsonUtil<GetMyTicketDetailResponse>().json2Bean(
                        jsonString, GetMyTicketDetailResponse.class.getName());
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