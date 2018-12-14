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
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.PutUserInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 提交验证码Request
 */
public class PostVerifyCodeRequest extends InterfaceRequest<PutUserInfoResponse> {


    private static final String TAG = PostVerifyCodeRequest.class.getSimpleName();

    public PostVerifyCodeRequest(String phone, String code,int autoRegisterFlag, ResponseListener<PutUserInfoResponse> listener) {
        super(Method.POST,Constant.URL.POST_VERIFY_CODE, "",
                RequestUtil.getVerifyCodeParams(phone, code, autoRegisterFlag), listener);
    }

    @Override
    protected Response<PutUserInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PutUserInfoResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<PutUserInfoResponse>().json2Bean(
                        jsonString, PutUserInfoResponse.class.getName());
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
