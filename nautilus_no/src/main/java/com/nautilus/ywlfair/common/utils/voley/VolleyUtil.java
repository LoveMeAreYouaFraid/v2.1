package com.nautilus.ywlfair.common.utils.voley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

public class VolleyUtil {
    
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyPatterns";
    
    /**
     * Global request queue for Volley
     */
    private static RequestQueue mRequestQueue;

    private static ImageLoader mImageLoader;
    
    private VolleyUtil() {
        // no instances
    }

    public static void init(Context context) {
        
        if(mRequestQueue != null) {
            throw new IllegalStateException("Volley has initialized");
        }
        
//        mRequestQueue = Volley.newRequestQueue(context, new OkHttp3Stack(new OkHttpClient()));

        mRequestQueue = Volley.newRequestQueue(context, new OnStartHurlStack());

    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

//        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public static <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Returns instance of ImageLoader initialized with {@see FakeImageCache} which effectively means
     * that no memory caching is used. This is useful for images that you know that will be show
     * only once.
     * TODO:重写ImageRequest的HttpHeaderParser
     *
     * @return
     */
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }


    /**
     * 获取某个请求的缓存，如果为null，则无缓存
     * @param key
     * @return
     */
    public static byte[] getDataInDiskCache(String key){
        Cache.Entry entry = getRequestQueue().getCache().get(key);
        return entry == null ? null : entry.data;
    }

}
