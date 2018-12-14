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
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetAccountStatusResponse;
import com.nautilus.ywlfair.entity.response.GetQrCodeResponse;

import java.io.UnsupportedEncodingException;

/**
 * 支付账户状态Request
 */
public class GetAccountStatusRequest extends InterfaceRequest<GetAccountStatusResponse> {

    private static final String TAG = GetAccountStatusRequest.class.getSimpleName();

    public GetAccountStatusRequest(String userId, ResponseListener<GetAccountStatusResponse> listener) {
        super(Method.GET, Constant.URL.GET_ACCOUNT_STATUS,userId,null, listener);
    }

    @Override
    protected Response<GetAccountStatusResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetAccountStatusResponse getAccountStatusResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAccountStatusResponse = new JsonUtil<GetAccountStatusResponse>().json2Bean(
                        jsonString, GetAccountStatusResponse.class.getName());
            }

            if (getAccountStatusResponse != null) {
                CustomError customError = processResponse(getAccountStatusResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAccountStatusResponse,
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
