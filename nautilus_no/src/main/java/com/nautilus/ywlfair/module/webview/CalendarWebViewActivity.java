package com.nautilus.ywlfair.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.StringUtils;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

public class CalendarWebViewActivity extends BaseActivity {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private View webViewCover;

    private TextView titleView;

    private Context mContext;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.calendar_webview_activity);

        mContext = this;

        initViews();

        if (getIntent().hasExtra(Constant.KEY.URL)) {

            url = getIntent().getStringExtra(Constant.KEY.URL);

            String date = StringUtils.getUrlParameter(url, "date");

            if (TextUtils.isEmpty(date)) {
                titleView.setText("");
            } else {
                titleView.setText(date);
            }

        } else {
            url = MyApplication.calendarUrl;

            int userId = -1;

            if (MyApplication.getInstance().isLogin()) {

                userId = GetUserInfoUtil.getUserInfo().getUserId();
            }

            url = url + "?userId=" + userId + "&isVendor=" + MyApplication.getInstance().getUserType();
        }

        initWebViewSettings();

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        LogUtil.e("url", url);

        webView.loadUrl(url);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

    }

    private void initViews() {
        titleView = (TextView) findViewById(R.id.tv_title);

        webView = (WebView) findViewById(R.id.msw_webview);

        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webViewCover = findViewById(R.id.webview_cover);

        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 初始化webview设置
     */
    public void initWebViewSettings() {

        // 支持缩放
        webView.getSettings().setBuiltInZoomControls(false);
        // 支持保存数据
        webView.getSettings().setSaveFormData(false);

        webView.getSettings().setDefaultTextEncodingName("UTF-8");// 设置默认为utf-8

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()
                + APP_CACHE_DIRNAME;
        // String cacheDirPath =
        // getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        // 设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);

        webView.setWebViewClient(new MyWebViewClient(webView));

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 70) {
                    ProgressDialog.getInstance().cancel();
                }
            }
        });
    }

    class MyWebViewClient extends WVJBWebViewClient {

        public MyWebViewClient(WebView webView) {

            // support js send
            super(webView, new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                }
            });


            enableLogging();

            registerHandler("bridgeH5RegisterHandlerObj1", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    processResult(data);

                    callback.callback("ywl");
                }
            });

            registerHandler("bridgeGetUserId", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    if (MyApplication.getInstance().isLogin()) {
                        try {
                            String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

                            callback.callback(new JSONObject("{\"userId\": " + userId + "}"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            ProgressDialog.getInstance().show(mContext, "加载中...");

            webViewCover.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            webViewCover.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

    private void processResult(Object data) {

        String jsonString = data.toString();

        String type = "";

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(data.toString());

            if (jsonObject.has("type")) {
                type = jsonObject.getString("type");
            }

            if (type.equals("jumpCalendarDetail")) {
                if (jsonObject.has("url")) {
                    String url = jsonObject.getString("url");

                    jumpCalendarDetail(url);
                }
            } else if (type.equals("jumpActMainDateTime")) {

                String date = jsonObject.getString("date");

                jumpActMainDateTime(date);

            } else if (type.equals("act")) {

                if (jsonObject.has("id")) {
                    String actId = jsonObject.getString("id");

                    jumpActMainDetail(actId);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void jumpCalendarDetail(String url) {

        Intent intent = new Intent(mContext,
                CalendarWebViewActivity.class);

        intent.putExtra(Constant.KEY.URL, url);

        startActivity(intent);
    }

    private void jumpActMainDetail(String actId) {

        Intent intent = new Intent(mContext,
                ActiveWebViewActivity.class);

        intent.putExtra(Constant.KEY.ITEM_ID, actId);

        mContext.startActivity(intent);
    }

    private void jumpActMainDateTime(String date) {

        titleView.setText(date);
    }

}
