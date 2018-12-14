package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 想参加Request
 */
public class PostWantJoinRequest extends InterfaceRequest<PostLikeAndJoinResponse> {


    private static final String TAG = PostWantJoinRequest.class.getSimpleName();

    public PostWantJoinRequest(String activeId, int isWantParticipate,String userId, ResponseListener<PostLikeAndJoinResponse> listener) {
        super(Method.POST,Constant.URL.POST_WANT_JOIN, activeId,
                RequestUtil.getWantJoinParams(isWantParticipate, userId), listener);
    }

    @Override
    protected Response<PostLikeAndJoinResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostLikeAndJoinResponse postLikeAndJoinResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                postLikeAndJoinResponse = new JsonUtil<PostLikeAndJoinResponse>().json2Bean(
                        jsonString, PostLikeAndJoinResponse.class.getName());
            }

            if (postLikeAndJoinResponse != null) {
                CustomError customError = processResponse(postLikeAndJoinResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            postLikeAndJoinResponse,
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
