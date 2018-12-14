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
import com.nautilus.ywlfair.entity.response.MyTicketsListResponse;

import java.io.UnsupportedEncodingException;

/**
 * type	Y	摊位状态 0:已付款，1待付款
 * start	Y	页码
 * rows	Y	每页获取数量
 * 门票列表
 * Created by Administrator on 2016/4/22.
 */
public class GetMyTicketsListRequest extends InterfaceRequest<MyTicketsListResponse> {

    private static final String TAG = GetMyTicketsListRequest.class.getName();

    public GetMyTicketsListRequest(String userId, String type, String start, String rows, ResponseListener<MyTicketsListResponse> listener) {
        super(Method.GET, Constant.URL.GET_ACTIVITY_TICKET_LIST, userId, RequestUtil.getMyTicketsListParams(type, start, rows), listener);
    }

    @Override
    protected Response<MyTicketsListResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            MyTicketsListResponse getActivitiesResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                getActivitiesResponse = new JsonUtil<MyTicketsListResponse>().json2Bean(
                        jsonString, MyTicketsListResponse.class.getName());
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