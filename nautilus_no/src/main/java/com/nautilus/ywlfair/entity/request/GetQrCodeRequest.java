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
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetAppConfigResponse;
import com.nautilus.ywlfair.entity.response.GetQrCodeResponse;

import java.io.UnsupportedEncodingException;

/**
 * 收款二维码Request
 */
public class GetQrCodeRequest extends InterfaceRequest<GetQrCodeResponse> {

    private static final String TAG = GetQrCodeRequest.class.getSimpleName();

    public GetQrCodeRequest(String userId,ResponseListener<GetQrCodeResponse> listener) {
        super(Method.GET, Constant.URL.GET_QR_CODE,userId,null, listener);
    }

    @Override
    protected Response<GetQrCodeResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetQrCodeResponse getAppConfigResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAppConfigResponse = new JsonUtil<GetQrCodeResponse>().json2Bean(
                        jsonString, GetQrCodeResponse.class.getName());
            }

            if (getAppConfigResponse != null) {
                CustomError customError = processResponse(getAppConfigResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getAppConfigResponse,
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
