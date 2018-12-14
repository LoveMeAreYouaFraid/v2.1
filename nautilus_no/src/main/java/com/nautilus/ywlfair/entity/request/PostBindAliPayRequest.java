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
import com.nautilus.ywlfair.entity.response.PostBindAliPayResponse;

import java.io.UnsupportedEncodingException;

/**
 * 绑定推送Tag Request
 */
public class PostBindAliPayRequest extends InterfaceRequest<PostBindAliPayResponse> {


    private static final String TAG = PostBindAliPayRequest.class.getSimpleName();

    public PostBindAliPayRequest(int userId, int aliPayType, String account, String name,
                                 String certificateNo, String certificateImageUrl,
                                 ResponseListener<PostBindAliPayResponse> listener) {
        super(Method.POST, Constant.URL.BIND_ALI_PAY, userId + "",
                RequestUtil.getBindAliPayParams(aliPayType, account, name, certificateNo, certificateImageUrl), listener);
    }

    @Override
    protected Response<PostBindAliPayResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostBindAliPayResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<PostBindAliPayResponse>().json2Bean(
                        jsonString, PostBindAliPayResponse.class.getName());
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
