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
import com.nautilus.ywlfair.entity.response.GetBalanceDetailResponse;
import com.nautilus.ywlfair.entity.response.GetWalletInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 钱包信息Request
 */
public class GetBalanceDetailRequest extends InterfaceRequest<GetBalanceDetailResponse> {


    private static final String TAG = GetBalanceDetailRequest.class.getSimpleName();

    public GetBalanceDetailRequest(String balanceId, ResponseListener<GetBalanceDetailResponse> listener) {
        super(Method.GET,Constant.URL.GET_BALANCE_DETAIL,balanceId,null, listener);
    }

    @Override
    protected Response<GetBalanceDetailResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetBalanceDetailResponse getBalanceDetailResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getBalanceDetailResponse = new JsonUtil<GetBalanceDetailResponse>().json2Bean(
                        jsonString, GetBalanceDetailResponse.class.getName());
            }

            if (getBalanceDetailResponse != null) {
                CustomError customError = processResponse(getBalanceDetailResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getBalanceDetailResponse,
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
