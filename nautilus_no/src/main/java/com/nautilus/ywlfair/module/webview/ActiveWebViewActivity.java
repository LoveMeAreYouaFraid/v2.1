package com.nautilus.ywlfair.module.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ShowGetStallMenu;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.ShowTicketOrderMenu;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.ActiveStatusInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerActivityInfo;
import com.nautilus.ywlfair.entity.bean.PicInfoFromJs;
import com.nautilus.ywlfair.entity.bean.ScanCodeInfo;
import com.nautilus.ywlfair.entity.bean.TicketListItem;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.bean.event.EventActiveLike;
import com.nautilus.ywlfair.entity.bean.event.EventActiveStatus;
import com.nautilus.ywlfair.entity.request.BoothSubscribeNoticeRequest;
import com.nautilus.ywlfair.entity.request.GetActiveStatusRequest;
import com.nautilus.ywlfair.entity.request.GetShareInfoRequest;
import com.nautilus.ywlfair.entity.request.GetTicketListRequest;
import com.nautilus.ywlfair.entity.request.PostCommentByCommentIdRequest;
import com.nautilus.ywlfair.entity.request.PostWantJoinRequest;
import com.nautilus.ywlfair.entity.response.GetActiveStatusResponse;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;
import com.nautilus.ywlfair.entity.response.GetTicketListResponse;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaiduMapActivity;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.active.EditPagerActivity;
import com.nautilus.ywlfair.module.active.LotteryWebView;
import com.nautilus.ywlfair.module.booth.MyBoothDetailActivity;
import com.nautilus.ywlfair.module.market.OtherReasonsActivity;
import com.nautilus.ywlfair.module.mine.MySignActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.ACT_ID;
import static com.nautilus.ywlfair.common.Constant.REQUEST.KEY.TYPE;
import static com.nautilus.ywlfair.common.Constant.REQUEST_CODE.UP_LOAD_IDENTITY;

public class ActiveWebViewActivity extends BaseActivity implements OnClickListener {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private Context mContext;

    private View lottery;

    private LinearLayout replyView, controlBar;

    private ImageView imgLike, imgComment;

    private TextView takeTicket;

    private EditText replyInputView;

    private List<TicketListItem> ticketListItemList;

    private HomePagerActivityInfo homePagerActivityInfo;

    private JsonUtil<ScanCodeInfo> jsonUtil = new JsonUtil<>();

    private ActiveStatusInfo activeStatusInfo;

    private String actId;

    private MyWebViewClient webViewClient;

    private TextView takeStall;

    public ActiveShareInfo activeShareInfo;

    private String roundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.active_webview_activity);

        mContext = this;

        initViews();

        actId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        EventBus.getDefault().register(this);

        registerBroadCastReceiver();

        initWebViewSettings();

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        webView.loadUrl(getUrl());

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        if (MyApplication.getInstance().getUserType() == 0) {

            getTicketList(actId);

            getActiveStatus(1, "");
        }

        getShareInfoStart();

    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.msw_webview);

        webView.setOnTouchListener(onTouchListener);

        View topBarBack = findViewById(R.id.tv_top_bar_back);
        topBarBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View signBtn = findViewById(R.id.bt_sign);
        signBtn.setOnClickListener(this);

        View shareBtn = findViewById(R.id.iv_share);
        shareBtn.setOnClickListener(this);

        controlBar = (LinearLayout) findViewById(R.id.ll_control_bar);

        imgLike = (ImageView) findViewById(R.id.bt_like);
        imgLike.setOnClickListener(this);

        lottery = findViewById(R.id.iv_lotto);
        lottery.setOnClickListener(this);

        imgComment = (ImageView) findViewById(R.id.bt_comment);
        imgComment.setOnClickListener(this);

        takeTicket = (TextView) findViewById(R.id.bt_buy_ticket);
        takeTicket.setOnClickListener(this);

        replyView = (LinearLayout) findViewById(R.id.ll_reply);

        replyInputView = (EditText) findViewById(R.id.et_reply_input);

        takeStall = (TextView) findViewById(R.id.bt_buy_stall);
        takeStall.setOnClickListener(this);

        if (MyApplication.getInstance().getUserType() == 0) {
            controlBar.setVisibility(View.VISIBLE);

            takeStall.setVisibility(View.GONE);
        }

        View confirmReply = findViewById(R.id.tv_confirm_reply);
        confirmReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = replyInputView.getText().toString();

                if (TextUtils.isEmpty(replyContent)) {

                    ToastUtil.showShortToast("请输入回复内容");


                    return;
                }

                confirmReply(replyContent);
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

        webViewClient = new MyWebViewClient(webView);

        webViewClient.enableLogging();

        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 99) {
                    ProgressDialog.getInstance().cancel();
                }
            }
        });
    }

    private String getUrl() {
        String url = MyApplication.getInstance().getActMainUrl();

        if (TextUtils.isEmpty(url)) {
            url = PreferencesUtil.getString(Constant.PRE_KEY.ACT_MAIN_URL);
        }

        if (MyApplication.getInstance().isLogin()) {
            int userId = GetUserInfoUtil.getUserInfo().getUserId();

            if (!TextUtils.isEmpty(getIntent().getStringExtra(Constant.KEY.IS_MSG))) {

                url = url + "vendor/" + actId + "?userId=" + userId;

                controlBar.setVisibility(View.GONE);

            } else {
                if (MyApplication.getInstance().getUserType() == 1) {

                    url = url + "vendor/" + actId + "?userId=" + userId;
                } else {
                    url = url + "user/" + actId + "?userId=" + userId;
                }
            }

        } else {
            url = url + "user/" + actId;
        }

        return url;
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(mBroadcastReceiver);

        EventBus.getDefault().unregister(this);

        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }

    class MyWebViewClient extends WVJBWebViewClient {

        public MyWebViewClient(WebView webView) {

            // support js send
            super(webView, new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
//                    callback.callback("Response for message from ObjC!");
                }
            });

			/*
            // not support js send
			super(webView);
			*/

            enableLogging();

            registerHandler("bridgeH5RegisterHandlerObj1", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    LogUtil.e("bridgeH5RegisterHandlerObj1", data.toString());

                    handleCallBack(data);

                    callback.callback("ywl");
                }
            });

            registerHandler("bridgeGetUserInfo", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    clickPraise(callback);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj5", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    replyComment(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj3", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    showJsPicture(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerLoaded", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    ProgressDialog.getInstance().cancel();
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
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

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
     * 回复评论
     */
    private void replyComment(Object data) {
        String jsonString = data.toString();

        String type = "";

        commentId = "";

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

                if (type.equals("reply")) {

                    showCommentLayout();

                } else {
                    Intent intent = new Intent();

                    intent.setClass(mContext, CommentDetailActivity.class);

                    intent.putExtra(Constant.KEY.COMMENT_ID, commentId);

                    startActivity(intent);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 选择场次
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

            if (type.equals("choiceRound")) {
                if (jsonObject.has("roundId")) {
                    roundId = jsonObject.getString("roundId");

                    if (roundId.equals("0")) {
                        return;
                    }

                    takeStall.setVisibility(View.GONE);

                    getActiveStatus(2, roundId);
                }
            } else if (type.equals("buyBooth")) {

            } else if (type.equals("openMap")) {

                if (jsonObject.has("addrMap")) {
                    String addressMap = jsonObject.getString("addrMap");

                    Intent mapIntent = new Intent(mContext,
                            BaiduMapActivity.class);

                    mapIntent.putExtra(Constant.KEY.ADDRESS, addressMap);

                    startActivity(mapIntent);
                }
            } else if (type.equals("vendorQuestion")) {
                if (jsonObject.has("actId")) {
                    String actId = jsonObject.getString("actId");
                    startActivityForResult(new Intent(mContext, OtherReasonsActivity.class).putExtra(ACT_ID, actId).putExtra(TYPE, 1), UP_LOAD_IDENTITY);
                }else {
                    ToastUtil.showShortToast("请稍后再试");
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_like:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {
                    return;
                }

                if (activeStatusInfo == null)
                    return;

                praise();

                break;

            case R.id.iv_lotto:

                isScanCode();

                break;

            case R.id.bt_comment:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                Intent commentIntent = new Intent(mContext, EditPagerActivity.class);

                commentIntent.putExtra(Constant.KEY.ITEM_ID, actId);

                commentIntent.putExtra(Constant.KEY.TYPE, "1");

                startActivityForResult(commentIntent, Constant.REQUEST_CODE.EDIT_COMMENT);

                break;

            case R.id.bt_buy_stall:

                buyStall();

                break;

            case R.id.bt_buy_ticket:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                if (activeStatusInfo != null && homePagerActivityInfo != null) {
                    if (activeStatusInfo.getTicketStatus() == 1) {
                        buyTicket();
                    } else if (activeStatusInfo.getTicketStatus() == 0) {
                        int method = activeStatusInfo.getHasSellOutSubscribe() == 0 ? Request.Method.POST : Request.Method.PUT;

                        changeNoticeStatus(method, 1);
                    }
                }

                break;

            case R.id.bt_sign:

                if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    Intent intent = new Intent(mContext, MySignActivity.class);

                    intent.putExtra(Constant.KEY.ITEM_ID, actId);

                    intent.putExtra(Constant.KEY.SHARE_INFO, activeShareInfo);

                    startActivity(intent);

                }

                break;


            case R.id.iv_share:

                if (activeShareInfo == null) {
                    ToastUtil.showShortToast("正在获取分享信息");
                    return;
                }
                if (activeShareInfo.getContentUrl() == null) {
                    ToastUtil.showShortToast("该活动没有分享信息");
                    return;
                }

                if (homePagerActivityInfo != null) {
                    activeShareInfo.setTitle(homePagerActivityInfo.getName());
                } else {
                    activeShareInfo.setTitle("鹦鹉螺市集活动");
                }

                ShowShareMenuUtil.getInstance().initShareMenuDialog(this, activeShareInfo);

                break;
        }
    }


    @Override
    protected void onPause() {
        ToastUtil.cancelToast();
        super.onPause();
    }

    private void buyStall() {
        if (activeStatusInfo == null) {

            ToastUtil.showLongToast("活动状态信息不存在");
            return;
        }

        if (activeStatusInfo.getIsBoothBuy() == 1) {

            Intent intent = new Intent(mContext, MyBoothDetailActivity.class);

            intent.putExtra(Constant.KEY.ORDER_ID, activeStatusInfo.getBoothOrderId());

            startActivity(intent);
            return;
        }

        if (activeStatusInfo.getIsDeposit() == 1 && activeStatusInfo.getIsBoothApply() == 2) {
            if (activeStatusInfo.getTicketStatus() == 2) {//已售罄  设置提醒
                int method = activeStatusInfo.getHasSellOutSubscribe() == 0 ? Request.Method.POST : Request.Method.PUT;

                changeNoticeStatus(method, 2);
            } else {
                postBuyStallIntent();
            }

        } else {
            ShowGetStallMenu.getInstance().initMenuDialog(mContext, activeStatusInfo, roundId);
        }

    }

    /**
     * 获取摊位选择页面url
     *
     * @return
     */
    private void postBuyStallIntent() {

        webViewClient.callHandler("bridgeSetCallbackResultWithParm", "", new WVJBWebViewClient.WVJBResponseCallback() {

            @Override
            public void callback(Object data) {

                String jsonString = data.toString();

                LogUtil.e("bridgeSetCallbackResultWithParm", jsonString);

                if (TextUtils.isEmpty(jsonString)) {
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(jsonString);

                    if (jsonObject.has("url")) {

                        String url = jsonObject.getString("url");

                        Intent intent = new Intent();

                        intent.setClass(mContext, BuyStallWebViewActivity.class);

                        intent.putExtra(Constant.KEY.URL, url);

                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void buyTicket() {
        if (ticketListItemList != null && ticketListItemList.size() > 0) {

            ShowTicketOrderMenu.getInstance().initMenuDialog(mContext, ticketListItemList, homePagerActivityInfo);

        } else {
            ToastUtil.showLongToast("没有可购票");
        }
    }

    private void setBuyStallBtnStatus() {

        takeStall.setVisibility(View.VISIBLE);

        if (activeStatusInfo.getTicketStatus() == 0) {

            takeStall.setEnabled(false);

            takeStall.setText("即将开售");

        } else if (activeStatusInfo.getTicketStatus() == 1 ||
                activeStatusInfo.getTicketStatus() == 2 ||
                activeStatusInfo.getTicketStatus() == 3) {

            if (activeStatusInfo.getIsDeposit() == 1 && activeStatusInfo.getIsBoothApply() == 2) {
                takeStall.setEnabled(true);

                takeStall.setText("购买摊位");

                if (activeStatusInfo.getTicketStatus() == 2) {
                    takeStall.setText("设置购摊提醒");
                }
            } else {
                takeStall.setEnabled(true);

                takeStall.setText("我要摆摊");
            }

        } else {
            takeStall.setText("停止售摊");

            takeStall.setEnabled(false);

        }

        if (activeStatusInfo.getHasSellOutSubscribe() == 1 && activeStatusInfo.getTicketStatus() == 2) {
            takeStall.setText("已设置购摊提醒");
        }

        if (activeStatusInfo.getIsBoothBuy() == 1) {
            takeStall.setEnabled(true);

            takeStall.setText("查看摊位");
        }

    }

    private void hideCommentLayout() {

        replyInputView.setText("");

        KeyBoardUtil.hideSoftKeyboard(replyView);

        replyView.setVisibility(View.GONE);

        controlBar.setVisibility(View.VISIBLE);

    }

    private void showCommentLayout() {
        replyView.setVisibility(View.VISIBLE);

        controlBar.setVisibility(View.GONE);

        replyInputView.requestFocus();

        KeyBoardUtil.showSoftKeyboard(replyInputView);

    }

    private void getTicketList(String actId) {
        GetTicketListRequest getTicketListRequest = new GetTicketListRequest(actId, new ResponseListener<GetTicketListResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetTicketListResponse response) {

            }

            @Override
            public void onResponse(GetTicketListResponse response) {
                if (response != null && response.getResult().getTicketInfo() != null) {

                    ticketListItemList = response.getResult().getTicketInfo();

                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();
                    // ToastUtil.showShortToast(response.getMessage());


                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");

                }
            }

            @Override
            public void onFinish() {

            }
        });

        getTicketListRequest.setShouldCache(false);

        VolleyUtil.addToRequestQueue(getTicketListRequest);
    }

    private void getActiveStatus(final int type, String roundId) {

        String userId = "-1";

        if (MyApplication.getInstance().isLogin()) {
            UserInfo userInfo = GetUserInfoUtil.getUserInfo();

            userId = userInfo.getUserId() + "";
        }

        GetActiveStatusRequest request = new GetActiveStatusRequest(actId,
                type + "", userId, roundId, new ResponseListener<GetActiveStatusResponse>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetActiveStatusResponse response) {

            }

            @Override
            public void onResponse(GetActiveStatusResponse response) {
                if (response != null && response.getResult().getStatusInfo() != null) {

                    setStallBtnStatus(true);

                    activeStatusInfo = response.getResult().getStatusInfo();

                    activeStatusInfo.setActId(actId);

                    homePagerActivityInfo = response.getResult().getActivityInfo();

                    if (activeStatusInfo.getIsDrawOpen() == 1 && MyApplication.getInstance().getUserType() == 0) {
                        lottery.setVisibility(View.VISIBLE);
                    } else {
                        lottery.setVisibility(View.GONE);
                    }

                    if (type == 1) {
                        setBtnStatus();
                    } else {
                        setBuyStallBtnStatus();
                    }

                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                setStallBtnStatus(false);

                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();
                    ToastUtil.showShortToast(response.getMessage());

                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                }
            }

            @Override
            public void onFinish() {

            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    /**
     * 控制购买摊位按钮的 显隐
     */
    private void setStallBtnStatus(boolean isShow) {

        if (isShow && MyApplication.getInstance().getUserType() == 1) {

            takeStall.setVisibility(View.VISIBLE);

        } else {
            takeStall.setVisibility(View.GONE);
        }

    }

    /**
     * 设置按钮状态
     * 登录状态下 获取了活动信息之后再调用
     */
    private void setBtnStatus() {

        takeTicket.setEnabled(false);

        if (activeStatusInfo.getHasLike() == 0) {
            imgLike.setImageResource(R.drawable.ic_want_join_normal);
        } else {
            imgLike.setImageResource(R.drawable.ic_want_join_pressed);
        }

        switch (activeStatusInfo.getTicketStatus()) {
            case 0:
                takeTicket.setText("设置开售提醒");
                takeTicket.setEnabled(true);
                break;

            case 1:
                takeTicket.setText("我要买票");
                takeTicket.setEnabled(true);
                break;

            case 2:
                takeTicket.setText("门票售罄");
                takeTicket.setEnabled(false);
                break;

            case 3:
                takeTicket.setText("门票免费");
                takeTicket.setEnabled(false);
                break;

            case 4:
                takeTicket.setText("停止售票");
                takeTicket.setEnabled(false);
                break;
        }

        if (activeStatusInfo.getHasSellOutSubscribe() == 1 && activeStatusInfo.getTicketStatus() == 0) {
            takeTicket.setText("已设置开售提醒");
        }
    }

    private void isScanCode() {

        if (LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

            String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

            String data = TimeUtil.getYearMonthAndDay(System.currentTimeMillis());

            String key = Constant.KEY.TWO_CODE_INFO + userId + actId;

            if (!TextUtils.isEmpty(PreferencesUtil.getString(key))) {

                ScanCodeInfo info = jsonUtil.json2Bean(PreferencesUtil.getString(key), ScanCodeInfo.class.getName());

                if (TimeUtil.getYearMonthAndDay(info.getData()).equals(data)) {

                    LotteryWebView.startLotteryWebView(mContext, "1", info.getUrl() + "&userId=" + userId + "", actId +
                            "&deviceId=" + Common.getInstance().getIMSI());

                } else {
                    LotteryWebView.startLotteryWebView(mContext, "1", MyApplication.getInstance().getActDrawUrl() + "actId=" + actId +
                            "&userId=" + userId + "", actId);
                }
            } else {
                LotteryWebView.startLotteryWebView(mContext, "1", MyApplication.getInstance().getActDrawUrl() + "actId=" + actId +
                        "&userId=" + userId + "", actId);
            }

        }
    }

    /**
     * 想参加
     */
    private void praise() {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        int isLike = activeStatusInfo.getHasLike() == 0 ? 1 : 2;

        PostWantJoinRequest request = new PostWantJoinRequest(actId, isLike, userId,
                new ResponseListener<PostLikeAndJoinResponse>() {
                    @Override
                    public void onStart() {
                        imgLike
                                .setBackgroundResource(R.drawable.bg_green_rounded_gray_solid_rectangle);

                        imgLike.setEnabled(false);
                    }

                    @Override
                    public void onCacheResponse(PostLikeAndJoinResponse response) {

                    }

                    @Override
                    public void onResponse(PostLikeAndJoinResponse response) {
                        if (response != null) {

                            int hasLike = activeStatusInfo.getHasLike() == 0 ? 1 : 0;

                            if (hasLike == 1) {
                                imgLike.setImageResource(R.drawable.ic_want_join_pressed);

                                ToastUtil.showShortToast("设置想去的市集");
                            }

                            if (hasLike == 0) {
                                imgLike.setImageResource(R.drawable.ic_want_join_normal);

                                ToastUtil.showShortToast("取消想去的市集");
                            }

                            activeStatusInfo.setHasLike(hasLike);

                            EventBus.getDefault().post(new EventActiveLike(hasLike));
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();
                            ToastUtil.showShortToast(response.getMessage());


                        } else {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                        }
                    }

                    @Override
                    public void onFinish() {
                        imgLike.setEnabled(true);

                        imgLike.setBackgroundResource(R.drawable.bg_green_rounded_rectangle);
                    }
                });
        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private String commentId;

    private void confirmReply(String content) {

        String userId = String.valueOf(GetUserInfoUtil.getUserInfo().getUserId());

        PostCommentByCommentIdRequest request = new PostCommentByCommentIdRequest(commentId, userId,
                content, new ResponseListener<PostCommentResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "提交中...");
            }

            @Override
            public void onCacheResponse(PostCommentResponse response) {
            }

            @Override
            public void onResponse(PostCommentResponse response) {
                if (response == null || response.getResult().getCommentInfo() == null) {
                    ToastUtil.showShortToast("操作失败");
                    return;
                }
                ToastUtil.showShortToast("回复成功");

                hideCommentLayout();
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();


                    ToastUtil.showShortToast(response.getMessage());

                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                }
            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }


    private void changeNoticeStatus(final int method, int type) {

        String userId = GetUserInfoUtil.getUserInfo().getUserId() + "";

        BoothSubscribeNoticeRequest request = new BoothSubscribeNoticeRequest(method, userId, actId,
                type, new ResponseListener<InterfaceResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(InterfaceResponse response) {

            }

            @Override
            public void onResponse(InterfaceResponse response) {
                if (response != null) {
                    if (method == Request.Method.POST) {
                        if (MyApplication.getInstance().getUserType() == 0) {

                            ToastUtil.showShortToast("已设置开售提醒");

                            takeTicket.setText("已设置开售提醒");
                        } else {
                            ToastUtil.showShortToast("已设置开售提醒");

                            takeStall.setText("已设置提醒");
                        }

                        activeStatusInfo.setHasSellOutSubscribe(1);
                    } else {
                        if (MyApplication.getInstance().getUserType() == 0) {

                            ToastUtil.showShortToast("已取消开售提醒");

                            takeTicket.setText("设置开售提醒");

                        } else {
                            ToastUtil.showShortToast("已取消开售提醒");

                            takeStall.setText("购摊提醒");
                        }

                        activeStatusInfo.setHasSellOutSubscribe(0);
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof CustomError) {
                    InterfaceResponse response = ((CustomError) error).getResponse();
                    ToastUtil.showShortToast(response.getMessage());

                } else {
                    ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                }
            }

            @Override
            public void onFinish() {

            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (replyView.getVisibility() == View.VISIBLE) {

                        hideCommentLayout();

                    }
                    break;
            }
            return false;
        }
    };

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

    public void getShareInfoStart() {

        GetShareInfoRequest request = new GetShareInfoRequest(Constant.URL.GET_ACTIVITY_SHARE_INFO, actId,
                new ResponseListener<GetShareInfoResponse>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCacheResponse(GetShareInfoResponse response) {
                        if (response != null && response.getResult().getActiveShareInfo() != null) {
                            activeShareInfo = response.getResult().getActiveShareInfo();
                        }
                    }

                    @Override
                    public void onResponse(GetShareInfoResponse response) {
                        if (response != null && response.getResult().getActiveShareInfo() != null) {
                            activeShareInfo = response.getResult().getActiveShareInfo();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof CustomError) {
                            InterfaceResponse response = ((CustomError) error).getResponse();


                            ToastUtil.showShortToast(response.getMessage());

                        } else {
                            ToastUtil.showShortToast("获取数据失败，请您稍后重试");
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
    }

    @Subscribe
    public void onEventMainThread(EventActiveStatus eventActiveStatus) {

        switch (eventActiveStatus.getType()) {

            case 1:

                activeStatusInfo.setIsDeposit(eventActiveStatus.getStatus());

                break;

            case 2:

                activeStatusInfo.setIsBoothApply(eventActiveStatus.getStatus());

                break;

        }

    }

    /**
     * 刷新点赞用户列表
     */
    private void refreshCommentList() {
        try {

            webViewClient.callHandler("bridgeSetCallbackResult", new JSONObject("{type:loadCommentFirst}"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 刷新提问
     */
    private void loadMyQuestionList() {
        try {

            webViewClient.callHandler("bridgeSetCallbackResult", new JSONObject("{type:loadMyQuestionList}"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case Constant.REQUEST_CODE.EDIT_COMMENT:
                    refreshCommentList();
                    break;
                case UP_LOAD_IDENTITY:
                    loadMyQuestionList();
                    break;
            }
        }

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.BROADCAST.LOGIN_BROAD)) {
                getActiveStatus(1, "");
            }
        }

    };

    public void registerBroadCastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constant.BROADCAST.LOGIN_BROAD);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


}
