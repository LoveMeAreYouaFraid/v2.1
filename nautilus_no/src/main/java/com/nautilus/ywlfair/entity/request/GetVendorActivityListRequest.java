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
import com.nautilus.ywlfair.entity.response.GetSignListResponse;
import com.nautilus.ywlfair.entity.response.GetVendorActivityResponse;

import java.io.UnsupportedEncodingException;

/**
 * 摊主活动列表
 * Created by Administrator on 2016/4/22.
 */
public class GetVendorActivityListRequest extends InterfaceRequest<GetVendorActivityResponse> {
    private static final String TAG = GetSignListResponse.class.getSimpleName();

    public GetVendorActivityListRequest(int type, int start, int rows, ResponseListener<GetVendorActivityResponse> listener) {
        super(Method.GET, Constant.URL.GET_ACTIVITYS_VONDER, null, RequestUtil.getBoothListParams(type, start, rows), listener);
    }

    @Override
    protected Response<GetVendorActivityResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetVendorActivityResponse getActivitiesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivitiesResponse = new JsonUtil<GetVendorActivityResponse>().json2Bean(
                        jsonString, GetVendorActivityResponse.class.getName());
            }

            if (getActivitiesResponse != null) {
                CustomError customError = processResponse(getActivitiesResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(
                            getActivitiesResponse,
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