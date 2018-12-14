package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.entity.response.GetActivityCommentsResponse;
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
public class GetActivityCommentsRequest extends InterfaceRequest<GetActivityCommentsResponse> {


    private static final String TAG = GetActivityCommentsRequest.class.getSimpleName();

    public GetActivityCommentsRequest(String itemId,String itemType, int start, int rows, ResponseListener<GetActivityCommentsResponse> listener) {
        super(Method.GET,Constant.URL.GET_COMMENTS_BY_ACTIVITY_ID,"", RequestUtil.getCommentListParams(itemId,itemType,start,rows), listener);
    }

    @Override
    protected Response<GetActivityCommentsResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetActivityCommentsResponse getActivityCommentsResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivityCommentsResponse = new JsonUtil<GetActivityCommentsResponse>().json2Bean(
                        jsonString, GetActivityCommentsResponse.class.getName());
            }

            if (getActivityCommentsResponse != null) {
                CustomError customError = processResponse(getActivityCommentsResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivityCommentsResponse,
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
