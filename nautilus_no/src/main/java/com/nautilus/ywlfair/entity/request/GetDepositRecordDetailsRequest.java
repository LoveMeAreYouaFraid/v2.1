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
import com.nautilus.ywlfair.entity.response.GetDebitRecordsResponse;

import java.io.UnsupportedEncodingException;

/**
 * 6 .5.1 摊主交押金记录明细 请求
 * Created by lipeng on 2016/3/29.
 */
public class GetDepositRecordDetailsRequest extends InterfaceRequest<GetDebitRecordsResponse> {


    private static final String TAG = GetDepositRecordDetailsRequest.class.getSimpleName();

    public GetDepositRecordDetailsRequest(String userId, String depositId, ResponseListener<GetDebitRecordsResponse> listener) {
        super(Method.GET, Constant.URL.GET_VENDOR_DEPOSITS_LOG, userId, RequestUtil.getDepositRecordDetailsParams(depositId), listener);
    }

    @Override
    protected Response<GetDebitRecordsResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.e(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetDebitRecordsResponse getVendorDepositsResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getVendorDepositsResponse = new JsonUtil<GetDebitRecordsResponse>().json2Bean(
                        jsonString, GetDebitRecordsResponse.class.getName());
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
