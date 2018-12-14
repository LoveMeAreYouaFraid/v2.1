package com.nautilus.ywlfair.module.active;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import zxing.activity.CaptureActivity;

/**
 * Created by lipeng on 2016/4/8.
 */
public class LotteryWebView extends BaseActivity implements View.OnClickListener {
    private static final String APP_CACHE_DIRNAME = "/webcache";
    private TextView appTitle, appTitleRight;
    private WebView webView;
    private Context mContext;
    private String url, Type, actid;
    private int requestcode = 8888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lottery_web_view);

        mContext = this;

        View back = findViewById(R.id.img_back);
        back.setOnClickListener(this);

        webView = (WebView) findViewById(R.id.lottery_web);

        appTitle = (TextView) findViewById(R.id.tv_title);

        appTitleRight = (TextView) findViewById(R.id.tv_right_btn);

        appTitleRight.setVisibility(View.VISIBLE);

        appTitleRight.setOnClickListener(this);

        Type = getIntent().getStringExtra(Constant.KEY.COMMENT_TYPE);

        url = getIntent().getStringExtra(Constant.KEY.URL);

        actid = getIntent().getStringExtra(Constant.REQUEST.KEY.ACT_ID);

        initialization();

        initWebViewSettings();

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        LogUtil.e("webView.loadUrl=", url);

        webView.loadUrl(url);

    }

    private void initialization() {
        if (Type.equals("1")) {
            appTitle.setText("抽奖页");
            appTitleRight.setText("中奖记录");
        }
        if (Type.equals("2")) {
            appTitle.setText("中奖记录");
            appTitleRight.setOnClickListener(null);
        }
    }

    public static void startLotteryWebView(Context context, String commentType, String url, String actid) {

        Intent intent = new Intent(context, LotteryWebView.class);

        intent.putExtra(Constant.KEY.COMMENT_TYPE, commentType);

        intent.putExtra(Constant.KEY.URL, url);

        intent.putExtra(Constant.REQUEST.KEY.ACT_ID, actid);

        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right_btn:
                String uri = MyApplication.getInstance().getActDrawLogUrl() + "actId=" + actid + "&userId=" +
                        GetUserInfoUtil.getUserInfo().getUserId() + "";
                LotteryWebView.startLotteryWebView(mContext, "2", uri, actid);
                LogUtil.e("中奖记录url", uri);
                break;
        }

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
                if (newProgress > 50) {
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

                    cameraDialog(mContext);
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

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }

    }

    public void cameraDialog(final Context context) {

        final Dialog loginDialog = new Dialog(context, R.style.dialog);
        loginDialog.show();
        Window window = loginDialog.getWindow();

        window.setContentView(R.layout.dlg_unlogin);
        TextView title = (TextView) window.findViewById(R.id.tv_title);
        title.setText("请扫描现场抽奖二维码");
        TextView loginTextView = (TextView) window.findViewById(R.id.tv_login);
        loginTextView.setText("我懂了");
        loginTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(mContext,
                        CaptureActivity.class);

                intent.putExtra(Constant.REQUEST.KEY.ACT_ID, actid);

                startActivityForResult(intent, requestcode);

                loginDialog.cancel();
            }
        });

        TextView cancelSchoolTextView = (TextView) window
                .findViewById(R.id.tv_cancel);
        cancelSchoolTextView.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == requestcode) {

                String ua = webView.getSettings().getUserAgentString();

                webView.getSettings().setUserAgentString(ua + "; NautilusFair");

                url = data.getStringExtra(Constant.KEY.URL);

                LogUtil.e("onActivityResult—返回的URI", url);

                webView.loadUrl(url);

            }
        }

    }
}
