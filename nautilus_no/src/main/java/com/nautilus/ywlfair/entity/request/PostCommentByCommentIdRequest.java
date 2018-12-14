package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;

import java.io.UnsupportedEncodingException;

/**
 * 活动评论Request
 */
public class PostCommentByCommentIdRequest extends InterfaceRequest<PostCommentResponse> {


    private static final String TAG = PostCommentByCommentIdRequest.class.getSimpleName();

    public PostCommentByCommentIdRequest(String commentId,String userId,String content, ResponseListener<PostCommentResponse> listener) {
        super(Method.POST,Constant.URL.POST_COMMENTS_BY_COMMENT_ID,commentId,
                RequestUtil.getPostCommentParams(userId, content), listener);
    }

    @Override
    protected Response<PostCommentResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostCommentResponse postCommentResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                postCommentResponse = new JsonUtil<PostCommentResponse>().json2Bean(
                        jsonString, PostCommentResponse.class.getName());
            }

            if (postCommentResponse != null) {
                CustomError customError = processResponse(postCommentResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            postCommentResponse,
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
