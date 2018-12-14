package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetVendorInfoResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 摊主信息Request
 */
public class GetVendorInfoRequest extends InterfaceRequest<GetVendorInfoResponse> {


    private static final String TAG = GetVendorInfoRequest.class.getSimpleName();

    public GetVendorInfoRequest(String userId, ResponseListener<GetVendorInfoResponse> listener) {
        super(Method.GET,Constant.URL.GET_VENDOR_INFO,userId,null, listener);
    }

    @Override
    protected Response<GetVendorInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetVendorInfoResponse getVendorInfoResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorInfoResponse = new JsonUtil<GetVendorInfoResponse>().json2Bean(
                        jsonString, GetVendorInfoResponse.class.getName());
            }

            if (getVendorInfoResponse != null) {
                CustomError customError = processResponse(getVendorInfoResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getVendorInfoResponse,
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
