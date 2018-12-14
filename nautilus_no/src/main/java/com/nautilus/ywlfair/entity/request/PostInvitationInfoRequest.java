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
import com.nautilus.ywlfair.entity.response.GetInvitationInfoResponse;
import com.nautilus.ywlfair.entity.response.PostInvitationInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 27.2提交邀请码信息
 */
public class PostInvitationInfoRequest extends InterfaceRequest<PostInvitationInfoResponse> {


    private static final String TAG = PostInvitationInfoRequest.class.getSimpleName();

    public PostInvitationInfoRequest(String userId, String invitationCode, ResponseListener<PostInvitationInfoResponse> listener) {
        super(Method.POST, Constant.URL.POST_USER_INVITATION_CODE, userId, RequestUtil.getInvitationInfoParams(invitationCode), listener);
    }

    @Override
    protected Response<PostInvitationInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostInvitationInfoResponse getVendorDepositsResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorDepositsResponse = new JsonUtil<PostInvitationInfoResponse>().json2Bean(
                        jsonString, PostInvitationInfoResponse.class.getName());
            }

            if (getVendorDepositsResponse != null) {
                CustomError customError = processResponse(getVendorDepositsResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getVendorDepositsResponse,
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
