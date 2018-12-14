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
import com.nautilus.ywlfair.entity.response.GetArticleFunListResponse;

import java.io.UnsupportedEncodingException;

/**
 * 玩转市集列表Request
 */
public class GetArticleFunListRequest extends InterfaceRequest<GetArticleFunListResponse> {


    private static final String TAG = GetArticleFunListRequest.class.getSimpleName();

    public GetArticleFunListRequest(int start, int rows, ResponseListener<GetArticleFunListResponse> listener) {
        super(Method.GET,Constant.URL.GET_ARTICLE_FUN_LIST,"", RequestUtil.getActivePictureParams(start,rows), listener);
    }

    @Override
    protected Response<GetArticleFunListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetArticleFunListResponse getArticleFunListResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getArticleFunListResponse = new JsonUtil<GetArticleFunListResponse>().json2Bean(
                        jsonString, GetArticleFunListResponse.class.getName());
            }

            if (getArticleFunListResponse != null) {
                CustomError customError = processResponse(getArticleFunListResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getArticleFunListResponse,
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
