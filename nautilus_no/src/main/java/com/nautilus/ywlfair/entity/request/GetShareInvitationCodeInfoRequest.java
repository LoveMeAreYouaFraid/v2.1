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
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetInvitationInfoResponse;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;

import java.io.UnsupportedEncodingException;

/**
 * 27.3取得邀请码分享信息
 */
public class GetShareInvitationCodeInfoRequest extends InterfaceRequest<GetShareInfoResponse> {


    private static final String TAG = GetShareInvitationCodeInfoRequest.class.getSimpleName();

    public GetShareInvitationCodeInfoRequest(String userId, ResponseListener<GetShareInfoResponse> listener) {
        super(Method.GET, Constant.URL.GET_USER_INVITATION_CODE_INFO, userId, null, listener);
    }

    @Override
    protected Response<GetShareInfoResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetShareInfoResponse getVendorDepositsResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorDepositsResponse = new JsonUtil<GetShareInfoResponse>().json2Bean(
                        jsonString, GetShareInfoResponse.class.getName());
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
