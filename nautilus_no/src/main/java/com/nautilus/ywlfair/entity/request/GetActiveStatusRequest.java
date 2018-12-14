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
import com.nautilus.ywlfair.entity.response.GetActiveStatusResponse;
import com.nautilus.ywlfair.entity.response.GetGoodsDetailResponse;

import java.io.UnsupportedEncodingException;

/**
 * 活动状态Request
 */
public class GetActiveStatusRequest extends InterfaceRequest<GetActiveStatusResponse> {


    private static final String TAG = GetActiveStatusRequest.class.getSimpleName();

    public GetActiveStatusRequest(String actId,String type,String userId,String roundId, ResponseListener<GetActiveStatusResponse> listener) {
        super(Method.GET, Constant.URL.ACTIVE_STATUS,actId, RequestUtil.getActiveStatusParams(type, userId, roundId), listener);
    }

    @Override
    protected Response<GetActiveStatusResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActiveStatusResponse getAgctiveStatusResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAgctiveStatusResponse = new JsonUtil<GetActiveStatusResponse>().json2Bean(
                        jsonString, GetActiveStatusResponse.class.getName());
            }

            if (getAgctiveStatusResponse != null) {
                CustomError customError = processResponse(getAgctiveStatusResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAgctiveStatusResponse,
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
