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
import com.nautilus.ywlfair.entity.response.GetActivityInfoResponse;
import com.nautilus.ywlfair.entity.response.GetWalletInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 钱包信息Request
 */
public class GetWalletInfoRequest extends InterfaceRequest<GetWalletInfoResponse> {


    private static final String TAG = GetWalletInfoRequest.class.getSimpleName();

    public GetWalletInfoRequest(String userId, ResponseListener<GetWalletInfoResponse> listener) {
        super(Method.GET,Constant.URL.GET_WALLET_INFO,userId,null, listener);
    }

    @Override
    protected Response<GetWalletInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetWalletInfoResponse getWalletInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getWalletInfoResponse = new JsonUtil<GetWalletInfoResponse>().json2Bean(
                        jsonString, GetWalletInfoResponse.class.getName());
            }

            if (getWalletInfoResponse != null) {
                CustomError customError = processResponse(getWalletInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getWalletInfoResponse,
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
