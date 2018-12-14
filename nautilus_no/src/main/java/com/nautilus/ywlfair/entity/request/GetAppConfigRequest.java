package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetAppConfigResponse;

import java.io.UnsupportedEncodingException;

/**
 * app初始化Request
 */
public class GetAppConfigRequest extends InterfaceRequest<GetAppConfigResponse> {

    private static final String TAG = GetAppConfigRequest.class.getSimpleName();

    public GetAppConfigRequest(ResponseListener<GetAppConfigResponse> listener) {
        super("","",null, listener);
    }

    @Override
    protected Response<GetAppConfigResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetAppConfigResponse getAppConfigResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getAppConfigResponse = new JsonUtil<GetAppConfigResponse>().json2Bean(
                        jsonString, GetAppConfigResponse.class.getName());
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
