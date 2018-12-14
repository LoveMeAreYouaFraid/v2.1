package com.nautilus.ywlfair.module.webview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.entity.bean.ActFunInitInfo;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.HomePagerArticleInfo;
import com.nautilus.ywlfair.entity.bean.PicInfoFromJs;
import com.nautilus.ywlfair.entity.request.PostCommentByCommentIdRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.PostCommentResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.VideoPlayerActivity;
import com.nautilus.ywlfair.module.active.EditPagerActivity;
import com.nautilus.ywlfair.module.mine.AllActivityListActivity;
import com.nautilus.ywlfair.module.mine.ClickPraiseUserListActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ArticleWebViewActivity extends BaseActivity implements OnClickListener {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    private WebView webView;

    private Context mContext;

    private HomePagerArticleInfo homePagerArticleInfo;

    private ImageView likeImage;

    private MyWebViewClient webViewClient;

    private LinearLayout replyView, controlBar;

    private EditText replyInputView;

    private ActFunInitInfo actFunInitInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_webview_activity);

        mContext = this;

        initViews();

        initWebViewSettings();

        homePagerArticleInfo = (HomePagerArticleInfo) getIntent().getSerializableExtra(Constant.KEY.ARTICLE);

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        webView.loadUrl(homePagerArticleInfo.getArticleUrl());

    }

    private void initViews() {

        webView = (WebView) findViewById(R.id.msw_webview);
        webView.setOnTouchListener(onTouchListener);

        View back = findViewById(R.id.iv_top_bar_back);
        back.setOnClickListener(this);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        likeImage = (ImageView) findViewById(R.id.bt_like);
        likeImage.setOnClickListener(this);

        View commentView = findViewById(R.id.bt_confirm);
        commentView.setOnClickListener(this);

        View shareView = findViewById(R.id.bt_share);
        shareView.setOnClickListener(this);

        replyView = (LinearLayout) findViewById(R.id.ll_reply);

        controlBar = (LinearLayout) findViewById(R.id.ll_control_bar);

        replyInputView = (EditText) findViewById(R.id.et_reply_input);

        View confirmReply = findViewById(R.id.tv_confirm_reply);
        confirmReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyContent = replyInputView.getText().toString();

                if (TextUtils.isEmpty(replyContent)) {
                    Toast.makeText(MyApplication.getInstance(), "请输入回复内容",
                            Toast.LENGTH_SHORT).show();
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
        webView.getSettings().setGeolocationEnabled(true);
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

            registerHandler("bridgeH5RegisterHandlerObj1", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    processDetailIntent(data);

                    callback.callback("ywl");
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj2", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    processPraiseListIntent(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj4", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    processVideoIntent(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj5", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    processCommentIntent(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj6", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    LogUtil.e("bridgeH5RegisterHandlerObj6", data.toString());

                    processInitInfo(data);
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
     * 获取页面初始化信息
     */
    private void processInitInfo(Object data) {
        String jsonString = data.toString();

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        JsonUtil<ActFunInitInfo> jsonUtil = new JsonUtil<>();

        actFunInitInfo = jsonUtil.json2Bean(jsonString, ActFunInitInfo.class.getName());

        if (actFunInitInfo != null) {
            if (actFunInitInfo.getHasLike() == 1) {
                likeImage.setImageResource(R.drawable.h5bt_islike);
            }
            if (actFunInitInfo.getHasLike() == 0) {
                likeImage.setImageResource(R.drawable.h5bt_like);
            }
        }
    }

    /**
     * 评论回复 评论详情
     */
    private void processCommentIntent(Object data) {
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

    private void processVideoIntent(Object data) {

        String jsonString = data.toString();

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }


        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("type")) {
                String type = jsonObject.getString("type");

                if (type.equals("video")) {
                    if (jsonObject.has("url")) {
                        String url = jsonObject.getString("url");

                        Intent intent = new Intent(mContext, VideoPlayerActivity.class);

                        intent.putExtra(Constant.KEY.URL, url);

                        startActivity(intent);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /**
     * 点赞列表
     */
    private void processPraiseListIntent(Object data) {
        String jsonString = data.toString();

        String itemId = "";

        String itemType = "";

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("itemId")) {
                itemId = jsonObject.getString("itemId");

            }

            if (jsonObject.has("itemType")) {
                itemType = jsonObject.getString("itemType");

            }

            if (!TextUtils.isEmpty(itemId) && !TextUtils.isEmpty(itemType)) {
                Intent intent = new Intent(mContext, ClickPraiseUserListActivity.class);

                intent.putExtra(Constant.KEY.ITEM_ID, itemId);

                intent.putExtra(Constant.KEY.ITEM_TYPE, itemType);

                startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到活动详情或者 活动列表
     */
    private void processDetailIntent(Object data) {
        String jsonString = data.toString();

        if (TextUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            if (jsonObject.has("id")) {
                String actId = jsonObject.getString("id");

                if (TextUtils.isEmpty(actId)) {
                    return;
                }

                Intent intent = new Intent();

                if (actId.equals("-1")) {
                    intent.setClass(mContext, AllActivityListActivity.class);
                } else {
                    intent.putExtra(Constant.KEY.ITEM_ID, actId);

                    intent.setClass(mContext, ActiveWebViewActivity.class);
                }

                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

            case R.id.iv_top_bar_back:
                finish();
                break;

            case R.id.bt_like:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                if (actFunInitInfo == null) {
                    return;
                }

                praise();

                break;

            case R.id.bt_confirm:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                if (actFunInitInfo == null) {
                    return;
                }

                Intent commentIntent = new Intent(mContext, EditPagerActivity.class);

                commentIntent.putExtra(Constant.KEY.ITEM_ID, actFunInitInfo.getId());

                commentIntent.putExtra(Constant.KEY.TYPE, actFunInitInfo.getItemType() + "");

                startActivityForResult(commentIntent, Constant.REQUEST_CODE.EDIT_COMMENT);

                break;

            case R.id.bt_share:

                ActiveShareInfo activeShareInfo = new ActiveShareInfo();

                activeShareInfo.setTitle(homePagerArticleInfo.getTitle());

                activeShareInfo.setActDesc(homePagerArticleInfo.getActName());

                activeShareInfo.setContentUrl(homePagerArticleInfo.getArticleUrl());

                activeShareInfo.setActImgUrl(homePagerArticleInfo.getImageUrl());

                ShowShareMenuUtil.getInstance().initShareMenuDialog((Activity) mContext, activeShareInfo);

                break;
        }
    }


    private void hideCommentLayout() {

        replyInputView.setText("");
        replyInputView.setHint("");

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

    int touchSlop = 10;

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        float lastY = 0f;
        float currentY = 0f;
        // 下面两个表示滑动的方向，大于0表示向下滑动，小于0表示向上滑动，等于0表示未滑动
        int lastDirection = 0;
        int currentDirection = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    lastY = event.getY();
                    currentY = event.getY();
                    currentDirection = 0;
                    lastDirection = 0;

                    if (replyView.getVisibility() == View.VISIBLE) {

                        hideCommentLayout();

                    }
                    break;
                case MotionEvent.ACTION_MOVE:

                    // 只有在listView.getFirstVisiblePosition()>0的时候才判断是否进行显隐动画。因为listView.getFirstVisiblePosition()==0时，
                    // ToolBar——也就是头部元素必须是可见的，如果这时候隐藏了起来，那么占位置用了headerview就被用户发现了
                    // 但是当用户将列表向下拉露出列表的headerview的时候，应该要让头尾元素再次出现才对——这个判断写在了后面onScrollListener里面……
                    float tmpCurrentY = event.getY();
                    if (Math.abs(tmpCurrentY - lastY) > touchSlop) {// 滑动距离大于touchslop时才进行判断
                        currentY = tmpCurrentY;
                        currentDirection = (int) (currentY - lastY);
                        if (lastDirection != currentDirection) {
                            // 如果与上次方向不同，则执行显/隐动画
                            if (currentDirection < 0) {
                                animateHide();
                            } else {
                                animateBack();
                            }
                        }
                        lastY = currentY;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    // 手指抬起的时候要把currentDirection设置为0，这样下次不管向哪拉，都与当前的不同（其实在ACTION_DOWN里写了之后这里就用不着了……）
                    currentDirection = 0;
                    lastDirection = 0;
                    break;
            }
            return false;
        }
    };

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
                Toast.makeText(MyApplication.getInstance(), "回复成功",
                        Toast.LENGTH_SHORT).show();

                hideCommentLayout();
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
                ProgressDialog.getInstance().cancel();
            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private int isLike;

    /**
     * 感兴趣
     */
    private void praise() {

        int userId = 0;

        if (MyApplication.getInstance().isLogin()) {
            userId = GetUserInfoUtil.getUserInfo().getUserId();
        }

        isLike = actFunInitInfo.getHasLike();

        int like = actFunInitInfo.getHasLike() + 1;

        PostLikeRequest request = new PostLikeRequest(actFunInitInfo.getId(), Integer.valueOf(actFunInitInfo.getItemType()), like, userId,
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

                            refreshLikeUserList();

                            if (isLike == 1) {
                                likeImage.setImageResource(R.drawable.h5bt_like);

                                ToastUtil.showShortToast("取消点赞");

                                isLike = 0;

                            } else if (isLike == 0) {
                                likeImage.setImageResource(R.drawable.h5bt_islike);

                                ToastUtil.showShortToast("点赞成功");

                                isLike = 1;
                            }

                            actFunInitInfo.setHasLike(isLike);
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

    /**
     * 刷新点赞用户列表
     */
    private void refreshLikeUserList() {
        try {

            webViewClient.callHandler("bridgeSetCallbackResult", new JSONObject("{type:loadLikeUserList}"));

        } catch (JSONException e) {
            e.printStackTrace();
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

    AnimatorSet backAnimatorSet;// 这是显示头尾元素使用的动画

    private void animateBack() {
        // 先清除其他动画
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            hideAnimatorSet.cancel();
        }
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            // 如果这个动画已经在运行了，就不管它
        } else {
            backAnimatorSet = new AnimatorSet();
            // 下面两句是将头尾元素放回初始位置。
            // ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(rl_bottom,
            // "translationY", rl_bottom.getTranslationY(), 0f);
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(controlBar,
                    "translationY", controlBar.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<Animator>();
            // animators.add(headerAnimator);
            animators.add(footerAnimator);
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }

    AnimatorSet hideAnimatorSet;// 这是隐藏头尾元素使用的动画

    private void animateHide() {
        // 先清除其他动画
        if (backAnimatorSet != null && backAnimatorSet.isRunning()) {
            backAnimatorSet.cancel();
        }
        if (hideAnimatorSet != null && hideAnimatorSet.isRunning()) {
            // 如果这个动画已经在运行了，就不管它
        } else {
            hideAnimatorSet = new AnimatorSet();
            // ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(rl_bottom,
            // "translationY", rl_bottom.getTranslationY(),
            // -rl_bottom.getHeight());//将ToolBar隐藏到上面
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(controlBar,
                    "translationY", controlBar.getTranslationY(),
                    controlBar.getHeight());// 将Button隐藏到下面
            ArrayList<Animator> animators = new ArrayList<Animator>();
            // animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constant.REQUEST_CODE.EDIT_COMMENT:
                refreshCommentList();
                break;
        }
    }

}
