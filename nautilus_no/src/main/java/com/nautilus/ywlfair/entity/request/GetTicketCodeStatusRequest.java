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
import com.nautilus.ywlfair.entity.response.GutTicketCodeStatusResponse;

import java.io.UnsupportedEncodingException;

/**
 * 刷新门票
 * Created by Administrator on 2016/5/17.
 */
public class GetTicketCodeStatusRequest extends InterfaceRequest<GutTicketCodeStatusResponse> {


    private static final String TAG = PutPasswordRequest.class.getSimpleName();

    public GetTicketCodeStatusRequest(String oldPassword, ResponseListener<GutTicketCodeStatusResponse> listener) {
        super(Method.GET, Constant.URL.GET_TICKET_CODE_STATUS, null, RequestUtil.PutTicketCodeStatusParams(oldPassword), listener);
    }

    @Override
    protected Response<GutTicketCodeStatusResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GutTicketCodeStatusResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<GutTicketCodeStatusResponse>().json2Bean(
                        jsonString, GutTicketCodeStatusResponse.class.getName());
            }

            if (interfaceResponse != null) {
                CustomError customError = processResponse(interfaceResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            interfaceResponse,
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
