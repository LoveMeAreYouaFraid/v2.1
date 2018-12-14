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
import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;
import com.nautilus.ywlfair.entity.response.GetVendorVipResponse;

import java.io.UnsupportedEncodingException;

/**
 * 摊主Vip Request
 */
public class GetVendorVipRequest extends InterfaceRequest<GetVendorVipResponse> {


    private static final String TAG = GetVendorVipRequest.class.getSimpleName();

    public GetVendorVipRequest(String userId, ResponseListener<GetVendorVipResponse> listener) {
        super(Method.GET,Constant.URL.GET_VENDOR_VIP,userId,null, listener);
    }

    @Override
    protected Response<GetVendorVipResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetVendorVipResponse getVendorVipResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorVipResponse = new JsonUtil<GetVendorVipResponse>().json2Bean(
                        jsonString, GetVendorVipResponse.class.getName());
            }

            if (getVendorVipResponse != null) {
                CustomError customError = processResponse(getVendorVipResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getVendorVipResponse,
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
