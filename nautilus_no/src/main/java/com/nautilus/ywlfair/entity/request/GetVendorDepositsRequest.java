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
import com.nautilus.ywlfair.entity.response.GetUserStatisticResponse;
import com.nautilus.ywlfair.entity.response.GetVendorDepositsResponse;

import java.io.UnsupportedEncodingException;

/**
 * 押金记录Request
 */
public class GetVendorDepositsRequest extends InterfaceRequest<GetVendorDepositsResponse> {


    private static final String TAG = GetVendorDepositsRequest.class.getSimpleName();

    public GetVendorDepositsRequest(String userId, ResponseListener<GetVendorDepositsResponse> listener) {
        super(Method.GET, Constant.URL.GET_VENDOR_DEPOSITS, userId, null, listener);
    }

    @Override
    protected Response<GetVendorDepositsResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetVendorDepositsResponse getVendorDepositsResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorDepositsResponse = new JsonUtil<GetVendorDepositsResponse>().json2Bean(
                        jsonString, GetVendorDepositsResponse.class.getName());
            }

            if (getVendorDepositsResponse != null) {
                CustomError customError = processResponse(getVendorDepositsResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getVendorDepositsResponse,
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
