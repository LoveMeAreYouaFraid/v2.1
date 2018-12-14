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
import com.nautilus.ywlfair.entity.response.GetArticleAndRecommendResponse;
import com.nautilus.ywlfair.entity.response.GetRecommendListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 推荐、原创列表Request
 */
public class GetArticleAndRecommendRequest extends InterfaceRequest<GetArticleAndRecommendResponse> {


    private static final String TAG = GetArticleAndRecommendRequest.class.getSimpleName();

    public GetArticleAndRecommendRequest(int start, int rows, int type, ResponseListener<GetArticleAndRecommendResponse> listener) {
        super(Constant.URL.ARTICLE_AND_RECOMMEND,"", RequestUtil.getUserCommentParams(start, rows, type),listener);
    }

    @Override
    protected Response<GetArticleAndRecommendResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetArticleAndRecommendResponse getArticleAndRecommendResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getArticleAndRecommendResponse = new JsonUtil<GetArticleAndRecommendResponse>().json2Bean(
                        jsonString, GetArticleAndRecommendResponse.class.getName());
            }

            if (getArticleAndRecommendResponse != null) {
                CustomError customError = processResponse(getArticleAndRecommendResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getArticleAndRecommendResponse,
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
