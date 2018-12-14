package com.nautilus.ywlfair.module.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.entity.bean.BoothInfo;
import com.nautilus.ywlfair.entity.bean.PicInfoFromJs;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.TicketOrderActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuyStallWebViewActivity extends BaseActivity {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private View webViewCover;

    private Context mContext;

    private String url;

    private MyWebViewClient webViewClient;

    private JsonUtil<BoothInfo> jsonUtil;

    private BoothInfo boothInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.buy_stall_webview_activity);

        mContext = this;

        initViews();

        url = getIntent().getStringExtra(Constant.KEY.URL);

        initWebViewSettings();

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        url = url + "&userId=" + GetUserInfoUtil.getUserInfo().getUserId();

        webView.loadUrl(url);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        jsonUtil = new JsonUtil<>();
    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.msw_webview);

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient() {
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

        });

        webViewClient = new MyWebViewClient(webView);

        webViewClient.enableLogging();

        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 70) {
                    ProgressDialog.getInstance().cancel();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

    class MyWebViewClient extends WVJBWebViewClient {

        public MyWebViewClient(WebView webView) {

            super(webView, new WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                }
            });

            enableLogging();

            registerHandler("bridgeH5RegisterHandlerObj1", new WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    boothInfo = null;

                    processStallOrder(data);

                    callback.callback("ywl");
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj3", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    showJsPicture(data);
                }
            });

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

    private void processStallOrder(Object data) {

        String jsonString = data.toString();

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        boothInfo = jsonUtil.json2Bean(jsonString, BoothInfo.class.getName());

        if (boothInfo != null) {
            Intent intent = new Intent(mContext, TicketOrderActivity.class);

            intent.putExtra(Constant.KEY.BOOTH, boothInfo);

            intent.putExtra(Constant.KEY.MODE, TicketOrderActivity.Mode.STALL);

            startActivity(intent);
        }

    }

    /**
     * 点击文章图片  查看大图
     *
     * @param data
     */
    private void showJsPicture(Object data) {
        String jsonString = data.toString();

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        JsonUtil<PicInfoFromJs> jsJsonUtil = new JsonUtil<>();

        PicInfoFromJs picInfoFromJs = jsJsonUtil.json2Bean(jsonString, PicInfoFromJs.class.getName());

        Intent intent = new Intent(mContext,
                ShowPicturesPagerActivity.class);

        ArrayList<Uri> uris = new ArrayList<>();

        for (String string : picInfoFromJs.getPhotos()) {
            uris.add(Uri.parse(string));
        }

        intent.putExtra(Constant.KEY.URIS, uris);

        intent.putExtra(Constant.KEY.POSITION, picInfoFromJs.getIndex());

        intent.putExtra(Constant.KEY.CAN_DELETE, false);

        mContext.startActivity(intent);
    }

}
