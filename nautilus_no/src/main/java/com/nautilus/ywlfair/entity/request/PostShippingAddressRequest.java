package com.nautilus.ywlfair.entity.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.ForceCacheHttpHeaderParser;
import com.nautilus.ywlfair.common.utils.voley.InterfaceRequest;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.RequestUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.entity.response.PostShippingAddressResponse;

import java.io.UnsupportedEncodingException;
import java.util.jar.Attributes;

/**
 * 增加守收货地址
 * Created by Administrator on 2015/12/31.
 */
public class PostShippingAddressRequest extends InterfaceRequest<PostShippingAddressResponse> {

    private static final String TAG = PostShippingAddressRequest.class.getName();


    public PostShippingAddressRequest(String userid, String Name, String provinceCode, String cityCode,
                                      String telephone, String postCode, String address,
                                      ResponseListener<PostShippingAddressResponse> listener) {


        super(Request.Method.POST, Constant.URL.POST_SHIPPING_ADDERS, userid,
                RequestUtil.getShippingAddress(Name, provinceCode, cityCode, telephone, postCode, address), listener);
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