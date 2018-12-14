package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.PostShippingAddressResponse;

import java.io.UnsupportedEncodingException;

/**
 * Created by lp on 2016/1/2.
 */
public class PutEditAddressRequest extends InterfaceRequest<PostShippingAddressResponse> {

    private static final String TAG = PostShippingAddressRequest.class.getName();


    public PutEditAddressRequest(String userId, String id, String Name, String provinceCode, String cityCode,
                                 String telephone, String address, String postCode,
                                 ResponseListener<PostShippingAddressResponse> listener) {
//        String consignee, String provinceCode, String cityCode, String telephone, String address, String postCode

        super(Request.Method.PUT, Constant.URL.USER_ID, userId + "/address/" + id,
                RequestUtil.getShippingAddress(Name, provinceCode, cityCode, telephone, address, postCode), listener);
    }

    @Override
    protected Response<PostShippingAddressResponse> parseNetworkResponse(NetworkResponse response) {

        try {
            String jsonString = new String(response.data, "UTF-8");

            Log.i(TAG, "parseNetworkResponse: jsonString: " + jsonString);

            PostShippingAddressResponse interfaceResponse = null;

            if (!TextUtils.isEmpty(jsonString)) {
                interfaceResponse = new JsonUtil<PostShippingAddressResponse>().json2Bean(
                        jsonString, PostShippingAddressResponse.class.getName());
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