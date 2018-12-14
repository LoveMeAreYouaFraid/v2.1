package com.nautilus.ywlfair.common.utils.voley;

import android.os.Handler;
import android.os.Looper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Copyright (©) 2014 Shanghai Russula Network Technology Co., Ltd
 * <p/>
 * TODO:
 *
 * @author jiangdongxiang
 * @version 1.0, 2015/1/21 17:54
 * @since 2015/1/21
 */
public class OnStartHurlStack extends HurlStack {

    public OnStartHurlStack() {
        super(null);
    }

    @Override
    public HttpResponse performRequest(final Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {

        onPrepareRequest(request);

        return super.performRequest(request, additionalHeaders);
    }

    protected void onPrepareRequest(final Request request) throws IOException {

        if(request instanceof InterfaceRequest) {
//            final long threadId = Thread.currentThread().getId();
//            if (Looper.myLooper() != Looper.getMainLooper()) {
                // If we finish marking off of the main thread, we need to
                // actually do it on the main thread to ensure correct ordering.
                Handler mainThread = new Handler(Looper.getMainLooper());
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        ((InterfaceRequest) request).getResponseListener().onStart();
                        ((InterfaceRequest) request).getCache();
                    }
                });
//            }
        }
    }

}
