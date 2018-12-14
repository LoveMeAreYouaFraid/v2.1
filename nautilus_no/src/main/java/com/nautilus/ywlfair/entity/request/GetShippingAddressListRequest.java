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
import com.nautilus.ywlfair.entity.response.GetShippingAddressList;

import java.io.UnsupportedEncodingException;

/**
 * 获取收货地址list
 * Created by lp on 2016/1/2.
 */
public class GetShippingAddressListRequest extends InterfaceRequest<GetShippingAddressList> {

    private static final String TAG = GetShippingAddressListRequest.class.getName();

    public GetShippingAddressListRequest(String userId, ResponseListener<GetShippingAddressList> listener) {
        super(Method.GET, Constant.URL.GET_SHIPPING_ADDERS_LIST, userId, null, listener);
    }


    @Override
    protected Response<GetShippingAddressList> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            LogUtil.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            GetShippingAddressList interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<GetShippingAddressList>().json2Bean(
                        jsonString, GetShippingAddressList.class.getName());
            }

            if (interfaceResponse != null) {
                CustomError customError = processResponse(interfaceResponse);
                if (customError != null) {
                    return Response
                            .error(customError);
                } else {
                    return Response.success(interfaceResponse, ForceCacheHttpHeaderParser.parseCacheHeaders(response, 100));
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