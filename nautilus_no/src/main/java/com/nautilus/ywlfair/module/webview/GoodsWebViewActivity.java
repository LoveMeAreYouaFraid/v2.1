package com.nautilus.ywlfair.module.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ShowConfirmOrderMenu;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.GoodsInfo;
import com.nautilus.ywlfair.entity.bean.PicInfoFromJs;
import com.nautilus.ywlfair.entity.bean.event.EventGoodsLike;
import com.nautilus.ywlfair.entity.request.GetGoodsDetailRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetGoodsDetailResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.active.CommentListActivity;
import com.nautilus.ywlfair.module.active.EditPagerActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.RippleView;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoodsWebViewActivity extends BaseActivity implements OnClickListener, RippleView.OnEndClickListener {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private View webViewCover;

    private Context mContext;

    private ImageView likeImage;

    private GoodsInfo goodsInfo;

    private int isLike;

    private int type;//0 商品界面  1商品详情界面

    private RippleView buyBtn;

    private ImageView btnLike;

    private int checkType;//0 从商品首页  1第一层分类 2 第二层分类 3第三层分类

    public static void startWebViewActivity(Context context, GoodsInfo goodsInfo, int type, int checkType) {

        Intent intent = new Intent(context, GoodsWebViewActivity.class);

        intent.putExtra(Constant.KEY.GOODS_INFO, goodsInfo);

        intent.putExtra(Constant.KEY.TYPE, type);

        intent.putExtra(Constant.KEY.CHECK_TYPE, checkType);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.goods_webview_activity);

        mContext = this;

        goodsInfo = (GoodsInfo) getIntent().getSerializableExtra(Constant.KEY.GOODS_INFO);

        type = getIntent().getIntExtra(Constant.KEY.TYPE, 0);

        if (type == 0)
            goodsInfo.setShareUrl(goodsInfo.getGoodsUrl());

        checkType = getIntent().getIntExtra(Constant.KEY.CHECK_TYPE, 0);

        isLike = goodsInfo.getHasLike();

        initViews();

        initWebViewSettings();

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        webView.loadUrl(goodsInfo.getGoodsUrl());

    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.msw_webview);

        if (Build.VERSION.SDK_INT >= 19) // KITKAT
        {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webViewCover = findViewById(R.id.webview_cover);

        likeImage = (ImageView) findViewById(R.id.iv_goods_like);
        likeImage.setOnClickListener(this);


        View back = findViewById(R.id.iv_goods_back);
        back.setOnClickListener(this);

        View share = findViewById(R.id.iv_goods_share);
        share.setOnClickListener(this);

        View recommend = findViewById(R.id.iv_goods_recommend);
        recommend.setOnClickListener(this);

        buyBtn = (RippleView) findViewById(R.id.tv_goods_buy);
        buyBtn.setOnEndClickListener(buyBtn.getId(), this);

        if (type == 1) {
            View topBar = findViewById(R.id.rl_top_bar);
            topBar.setVisibility(View.VISIBLE);

            View controlBar = findViewById(R.id.rl_control_bar);
            controlBar.setVisibility(View.GONE);

            View bottomBar = findViewById(R.id.ll_bottom_bar);
            bottomBar.setVisibility(View.VISIBLE);

            View confirmBtn = findViewById(R.id.bt_confirm);
            confirmBtn.setOnClickListener(this);

            btnLike = (ImageView) findViewById(R.id.bt_like);
            btnLike.setOnClickListener(this);

            View shareBtn = findViewById(R.id.bt_share);
            shareBtn.setOnClickListener(this);

            View backBtn = findViewById(R.id.iv_top_bar_back);
            backBtn.setOnClickListener(this);
        }

        setIsLikeView();

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
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

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_goods_like:
            case R.id.bt_like:
                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {
                    return;
                }

                praise();

                break;

            case R.id.iv_goods_recommend:
            case R.id.bt_confirm:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                Intent commentIntent = new Intent(mContext, EditPagerActivity.class);

                commentIntent.putExtra(Constant.KEY.TYPE, "5");

                commentIntent.putExtra(Constant.KEY.ITEM_ID, goodsInfo.getGoodsId() + "");

                startActivityForResult(commentIntent, Constant.REQUEST_CODE.EDIT_COMMENT);

                break;

            case R.id.iv_goods_share:
            case R.id.bt_share:

                ActiveShareInfo shareInfo = new ActiveShareInfo();

                shareInfo.setTitle("鹦鹉螺市集商品分享");

                shareInfo.setActDesc(goodsInfo.getGoodsName() + "");

                shareInfo.setActImgUrl(goodsInfo.getImageUrl() + "");

                shareInfo.setContentUrl(goodsInfo.getShareUrl() + "");

                ShowShareMenuUtil.getInstance().initShareMenuDialog((Activity) mContext, shareInfo);

                break;

            case R.id.iv_goods_back:
            case R.id.iv_top_bar_back:
                finish();
                break;
        }
    }

    /**
     * 感兴趣
     */
    private void praise() {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        int like = isLike + 1;

        PostLikeRequest request = new PostLikeRequest(goodsInfo.getGoodsId() + "", 4, like, userId,
                new ResponseListener<PostLikeAndJoinResponse>() {
                    @Override
                    public void onStart() {

                        likeImage.setEnabled(false);
                    }

                    @Override
                    public void onCacheResponse(PostLikeAndJoinResponse response) {

                    }

                    @Override
                    public void onResponse(PostLikeAndJoinResponse response) {
                        if (response != null) {

                            if (type == 0) {
                                if (isLike == 1) {
                                    isLike = 0;
                                    likeImage.setImageResource(R.drawable.goods_like);

                                } else if (isLike == 0) {
                                    isLike = 1;
                                    likeImage.setImageResource(R.drawable.goods_liked);
                                }

                                EventBus.getDefault().post(new EventGoodsLike(isLike, checkType));
                            } else {
                                if (isLike == 0) {
                                    isLike = 1;
                                    btnLike.setImageResource(R.drawable.bt_islike);
                                } else {
                                    isLike = 0;
                                    btnLike.setImageResource(R.drawable.bt_like);
                                }

                                setResult(RESULT_OK, new Intent().putExtra(Constant.KEY.HAS_LIKE, isLike));
                            }

                            goodsInfo.setHasLike(isLike);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();

                            Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        likeImage.setEnabled(true);

                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    @Override
    public void mViewClick(int id) {

        if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

            return;
        }

        getGoodsDetail();

    }

    private void getGoodsDetail() {
        GetGoodsDetailRequest request = new GetGoodsDetailRequest(goodsInfo.getGoodsId() + "",
                new ResponseListener<GetGoodsDetailResponse>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCacheResponse(GetGoodsDetailResponse response) {

                    }

                    @Override
                    public void onResponse(GetGoodsDetailResponse response) {

                        if (response != null && response.getResult() != null && response.getResult().getGoodsInfo() != null) {
                            goodsInfo = response.getResult().getGoodsInfo();
                        }

                        ShowConfirmOrderMenu.getInstance().initMenuDialog(mContext, goodsInfo);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
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


            registerHandler("bridgeH5RegisterHandlerObj5", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    replyComment(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj1", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    handleCallBack(data);

                    callback.callback("ywl");
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj7", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    checkMoreComment(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj3", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    showJsPicture(data);
                }
            });

            registerHandler("bridgeGetUserInfo", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    clickPraise(callback);
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

    /**
     * 点击文章评论的赞
     */
    private void clickPraise(WVJBWebViewClient.WVJBResponseCallback callback) {

        if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

            return;
        }

        try {
            String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

            callback.callback(new JSONObject("{\"userId\": " + userId + "}"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看更多评论
     *
     * @param data
     */
    private void checkMoreComment(Object data) {
        String type = "";

        try {
            JSONObject jsonObject = new JSONObject(data.toString());

            if (jsonObject.has("type")) {
                type = jsonObject.getString("type");
            }

            if (type.equals("commentList")) {
                onMoreComments();
            }

        } catch (JSONException e) {
            e.printStackTrace();
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

    /**
     * 查看图文详情
     *
     * @param data
     */
    private void handleCallBack(Object data) {
        String type = "";

        try {
            JSONObject jsonObject = new JSONObject(data.toString());

            if (jsonObject.has("type")) {
                type = jsonObject.getString("type");
            }

            if (type.equals("openGoodsDetailMore")) {
                if (jsonObject.has("url")) {
                    String url = jsonObject.getString("url");

                    jumpGoodsDetailMore(url);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 回复评论
     */
    private void replyComment(Object data) {
        String jsonString = data.toString();

        String type = "";

        String commentId = "";

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("type")) {
                type = jsonObject.getString("type");

            }

            if (jsonObject.has("id")) {
                commentId = jsonObject.getString("id");
            }

            if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(commentId)) {

                if (type.equals("comment")) {

                    onCommentClick(commentId);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void jumpGoodsDetailMore(String url) {
        GoodsInfo tempGoods = goodsInfo;

        tempGoods.setGoodsUrl(url);

        Intent intent = new Intent(mContext, GoodsWebViewActivity.class);

        intent.putExtra(Constant.KEY.GOODS_INFO, tempGoods);

        intent.putExtra(Constant.KEY.TYPE, 1);

        startActivityForResult(intent, Constant.REQUEST_CODE.GOODS_DETAIL);
    }

    private void onCommentClick(String commendId) {
        Intent intent = new Intent(mContext,
                CommentDetailActivity.class);

        intent.putExtra(Constant.KEY.COMMENT_ID, commendId);

        startActivity(intent);
    }

    private void onMoreComments() {
        Intent commendListIntent = new Intent(mContext, CommentListActivity.class);

        commendListIntent.putExtra(Constant.KEY.ITEM_ID, goodsInfo.getGoodsId() + "");

        commendListIntent.putExtra(Constant.KEY.ITEM_TYPE, "4");

        startActivity(commendListIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.GOODS_DETAIL) {
                goodsInfo.setHasLike(data.getIntExtra(Constant.KEY.HAS_LIKE, 0));

                setIsLikeView();

                EventBus.getDefault().post(new EventGoodsLike(isLike, checkType));

            } else if (requestCode == Constant.REQUEST_CODE.EDIT_COMMENT) {
                webView.reload();
            }


        }
    }

    private void setIsLikeView() {
        isLike = goodsInfo.getHasLike();

        if (type == 1) {
            if (isLike == 1) {
                btnLike.setImageResource(R.drawable.bt_islike);
            } else {
                btnLike.setImageResource(R.drawable.bt_like);
            }
        } else {
            if (goodsInfo.getHasLike() == 0) {
                likeImage.setImageResource(R.drawable.goods_like);
            } else {
                likeImage.setImageResource(R.drawable.goods_liked);
            }
        }
    }

    @Override
    protected void onResume() {

        setIsLikeView();

        super.onResume();
    }


}
