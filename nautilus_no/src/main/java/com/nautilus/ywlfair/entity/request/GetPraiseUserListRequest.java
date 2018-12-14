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
import com.nautilus.ywlfair.entity.response.GetPraiseUserListResponse;
import com.nautilus.ywlfair.entity.response.GetUserArticlesResponse;

import java.io.UnsupportedEncodingException;

/**
 * 点赞用户Request
 */
public class GetPraiseUserListRequest extends InterfaceRequest<GetPraiseUserListResponse> {


    private static final String TAG = GetPraiseUserListRequest.class.getSimpleName();

    public GetPraiseUserListRequest(String itemId, String itemType, int start, int rows,
                                    ResponseListener<GetPraiseUserListResponse> listener) {
        super(Method.GET,Constant.URL.GET_PRIASE_USER_LIST,"",
                RequestUtil.getPraiseUserParams(itemId, itemType, start, rows), listener);
    }

    @Override
    protected Response<GetPraiseUserListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetPraiseUserListResponse getUserArticlesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getUserArticlesResponse = new JsonUtil<GetPraiseUserListResponse>().json2Bean(
                        jsonString, GetPraiseUserListResponse.class.getName());
            }

            if (getUserArticlesResponse != null) {
                CustomError customError = processResponse(getUserArticlesResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getUserArticlesResponse,
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
