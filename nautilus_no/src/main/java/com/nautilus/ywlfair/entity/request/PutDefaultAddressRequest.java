package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.GetShippingAddressList;
import com.nautilus.ywlfair.entity.response.PutDefaultAddressResponse;

import java.io.UnsupportedEncodingException;

/**
 * 设置默认地址
 * Created by lp on 2016/1/2.
 */
public class PutDefaultAddressRequest extends InterfaceRequest<PutDefaultAddressResponse> {

    private static final String TAG = PutDefaultAddressRequest.class.getName();

    public PutDefaultAddressRequest(String userId, String id, ResponseListener<PutDefaultAddressResponse> listener) {
        super(Method.PUT, Constant.URL.USER_ID, userId + "/defaultAddress/" + id, null, listener);
    }


    @Override
    protected Response<PutDefaultAddressResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            Log.e("123", "parseNetworkResponse: jsonString: " + jsonString);

            PutDefaultAddressResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<PutDefaultAddressResponse>().json2Bean(
                        jsonString, PutDefaultAddressResponse.class.getName());
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