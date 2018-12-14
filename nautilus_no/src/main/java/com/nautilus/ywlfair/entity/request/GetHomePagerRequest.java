package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetHomePagerResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 首页信息Request
 */
public class GetHomePagerRequest extends InterfaceRequest<GetHomePagerResponse> {


    private static final String TAG = GetHomePagerRequest.class.getSimpleName();

    public GetHomePagerRequest(ResponseListener<GetHomePagerResponse> listener) {
        super(Constant.URL.GET_HOME_PAGER, "", null , listener);
    }

    @Override
    protected Response<GetHomePagerResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetHomePagerResponse getHomePagerResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getHomePagerResponse = new JsonUtil<GetHomePagerResponse>().json2Bean(
                        jsonString, GetHomePagerResponse.class.getName());
            }

            if (getHomePagerResponse != null) {
                CustomError customError = processResponse(getHomePagerResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getHomePagerResponse,
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
