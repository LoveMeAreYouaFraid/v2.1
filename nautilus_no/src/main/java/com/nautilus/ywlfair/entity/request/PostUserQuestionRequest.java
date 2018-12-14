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

import java.io.UnsupportedEncodingException;

/**
 * api 2.10用户提问
 */
public class PostUserQuestionRequest extends InterfaceRequest<InterfaceResponse> {


    private static final String TAG = PostUserQuestionRequest.class.getSimpleName();

    public PostUserQuestionRequest(String userId, String actId, String content, ResponseListener<InterfaceResponse> listener) {
        super(Method.POST, Constant.URL.POST_USER_QUESTION, userId,
                RequestUtil.PostUserQuestionParams(actId, content), listener);
    }

    @Override
    protected Response<InterfaceResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "PostUserQuestionRequest: jsonString: " + jsonString);

            InterfaceResponse postUserOrderResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                postUserOrderResponse = new JsonUtil<InterfaceResponse>().json2Bean(
                        jsonString, InterfaceResponse.class.getName());
            }

            if (postUserOrderResponse != null) {
                CustomError customError = processResponse(postUserOrderResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            postUserOrderResponse,
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
