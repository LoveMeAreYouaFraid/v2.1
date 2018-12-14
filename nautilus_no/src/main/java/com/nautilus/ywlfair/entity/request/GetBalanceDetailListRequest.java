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
import com.nautilus.ywlfair.entity.response.GetActivitiesResponse;
import com.nautilus.ywlfair.entity.response.GetBalanceDetailListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 收支明细列表Request
 */
public class GetBalanceDetailListRequest extends InterfaceRequest<GetBalanceDetailListResponse> {


    private static final String TAG = GetBalanceDetailListRequest.class.getSimpleName();

    public GetBalanceDetailListRequest(int userId, String startTime, String endTime,
                                       int queryType,int start, int rows, ResponseListener<GetBalanceDetailListResponse> listener) {
        super(Method.GET, Constant.URL.BALANCE_DETAIL, userId + "",
                RequestUtil.getBalanceDetailListParams(startTime, endTime, queryType,start, rows), listener);
    }

    @Override
    protected Response<GetBalanceDetailListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetBalanceDetailListResponse getBalanceDetailListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getBalanceDetailListResponse = new JsonUtil<GetBalanceDetailListResponse>().json2Bean(
                        jsonString, GetBalanceDetailListResponse.class.getName());
            }

            if (getBalanceDetailListResponse != null) {
                CustomError customError = processResponse(getBalanceDetailListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getBalanceDetailListResponse,
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
