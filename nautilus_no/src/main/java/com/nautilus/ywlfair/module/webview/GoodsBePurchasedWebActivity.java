package com.nautilus.ywlfair.module.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;

import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2016/6/2.
 */
public class GoodsBePurchasedWebActivity extends Activity {
    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private View webViewCover;

    private Context mContext;

    private String actId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_webview_activity);

        mContext = this;

        ProgressDialog.getInstance().show(mContext, "加载中...");

        actId = getIntent().getStringExtra(Constant.KEY.URL);


        initViews();

        initWebViewSettings();
        String ua = webView.getSettings().getUserAgentString();
        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        webView.loadUrl(actId);


    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.msw_webview);

        findViewById(R.id.iv_share).setVisibility(View.INVISIBLE);

        webViewCover = findViewById(R.id.webview_cover);

        webViewCover.setVisibility(View.GONE);

        findViewById(R.id.tv_top_bar_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.tv_title);


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

//        webView.getSettings().setLayoutAlgorithm(
//                WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

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

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 70) {
                    ProgressDialog.getInstance().cancel();
                }
            }
        });
    }


    public static void startWebActivity(Context context, String url) {

        Intent intent = new Intent(context, GoodsBePurchasedWebActivity.class);

        intent.putExtra(Constant.KEY.URL, url);

        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }


}
