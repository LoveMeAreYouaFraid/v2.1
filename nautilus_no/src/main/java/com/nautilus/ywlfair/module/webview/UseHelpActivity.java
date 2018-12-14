package com.nautilus.ywlfair.module.webview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.module.BaseActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/24.
 */
public class UseHelpActivity extends BaseActivity {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private int type;//0 帮助 1 权益 2  积分说明 3等级说明

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.use_help_activity);

        type = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        url = getIntent().getStringExtra(Constant.KEY.URL);

        setTitleBar();
    }

    private void setTitleBar() {

        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titleView = (TextView) findViewById(R.id.tv_title);

        if (type == 0) {
            titleView.setText("使用帮助");
        } else if (type == 1) {
            titleView.setText("摊主权益");
        } else if (type == 2) {
            titleView.setText("积分说明");
        } else if (type == 3) {
            titleView.setText("等级说明");
        }


        WebView webView = (WebView) findViewById(R.id.webView);

        setWebView(webView);

        webView.loadUrl(url);

    }

    private void setWebView(WebView webView) {
        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        MyWebViewClient webViewClient = new MyWebViewClient(webView);

        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
    }

    class MyWebViewClient extends WVJBWebViewClient {

        public MyWebViewClient(com.tencent.smtt.sdk.WebView webView) {

            super(webView, new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                }
            });

            enableLogging();

            registerHandler("bridgeGetUserId", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    int userId = -1;

                    try {
                        if (MyApplication.getInstance().isLogin()) {
                            userId = GetUserInfoUtil.getUserInfo().getUserId();
                        }

                        callback.callback(new JSONObject("{\"userId\": " + userId + "}"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }

    }

}
