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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.GetUserInfoUtil;
import com.nautilus.ywlfair.common.utils.JsonUtil;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.LoginWarmUtil;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.component.WVJBWebViewClient;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.bean.PicInfoFromJs;
import com.nautilus.ywlfair.entity.bean.event.EventArticleType;
import com.nautilus.ywlfair.entity.bean.event.EventExperience;
import com.nautilus.ywlfair.entity.bean.event.EventHomePager;
import com.nautilus.ywlfair.entity.request.GetShareInfoRequest;
import com.nautilus.ywlfair.entity.request.PostLikeRequest;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;
import com.nautilus.ywlfair.entity.response.PostLikeAndJoinResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.CommentDetailActivity;
import com.nautilus.ywlfair.module.active.CommentListActivity;
import com.nautilus.ywlfair.module.active.EditPagerActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;
import com.nautilus.ywlfair.widget.ShowPicturesPagerActivity;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WebViewActivity extends BaseActivity implements OnClickListener {

    private static final String APP_CACHE_DIRNAME = "/webcache";

    public static final int ALL_ARTICLE = 0;

    public static final int EXPERIENCE_ARTICLE = 1;

    public static final int HOMEPAGER_ARTICLE = 2;

    private WebView webView;

    private TextView titleView;

    private Context mContext;

    private String url;

    private String itemId;

    private ImageView likeImage;

    private String commentType;

    private ActiveShareInfo activeShareInfo;

    private String shareInfoUrl;

    private int isLike;//0 未赞  1  已赞   1 点赞 2 取消赞

    private int articleType = ALL_ARTICLE;//0 全部  1攻略 经验

    private View controllerView;

    private View errorView;

    public static void startWebViewActivity(Context context, String commentType, String url, String itemId, int isLike, int articleType) {

        Intent intent = new Intent(context, WebViewActivity.class);

        intent.putExtra(Constant.KEY.COMMENT_TYPE, commentType);

        intent.putExtra(Constant.KEY.URL, url);

        intent.putExtra(Constant.KEY.ITEM_ID, itemId);

        intent.putExtra(Constant.KEY.IS_LIKE, isLike);

        intent.putExtra(Constant.KEY.TYPE, articleType);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.webview_activity);

        mContext = this;

        commentType = getIntent().getStringExtra(Constant.KEY.COMMENT_TYPE);

        url = getIntent().getStringExtra(Constant.KEY.URL);

        itemId = getIntent().getStringExtra(Constant.KEY.ITEM_ID);

        isLike = getIntent().getIntExtra(Constant.KEY.IS_LIKE, 0);

        articleType = getIntent().getIntExtra(Constant.KEY.TYPE, ALL_ARTICLE);

        initViews();

        initWebViewSettings();

        initData();

    }

    private void initViews() {
        titleView = (TextView) findViewById(R.id.tv_title);

        webView = (WebView) findViewById(R.id.msw_webview);

        View topBarBack = findViewById(R.id.iv_top_bar_back);
        topBarBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likeImage = (ImageView) findViewById(R.id.bt_like);
        likeImage.setOnClickListener(this);

        View commentView = findViewById(R.id.bt_confirm);
        commentView.setOnClickListener(this);

        View shareView = findViewById(R.id.bt_share);
        shareView.setOnClickListener(this);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        errorView = findViewById(R.id.error);

        errorView.setOnClickListener(this);

    }

    private void initData() {

        if (commentType.equals("0")) {

            titleView.setText("协议详情");

        } else if (commentType.equals("-1")) {

        } else {
            controllerView = findViewById(R.id.ll_control_bar);

            controllerView.setVisibility(View.VISIBLE);

            webView.setOnTouchListener(onTouchListener);

            if (commentType.equals("2")) {
                shareInfoUrl = Constant.URL.GET_RECOMMEND_SHARE_INFO;
            } else if (commentType.equals("3")) {
                shareInfoUrl = Constant.URL.GET_ORIGINAL_SHARE_INFO;
            }

            getShareInfo();

            if (isLike == 1) {
                likeImage.setImageResource(R.drawable.h5bt_islike);
            }
            if (isLike == 0) {
                likeImage.setImageResource(R.drawable.h5bt_like);
            }
        }

        String ua = webView.getSettings().getUserAgentString();

        webView.getSettings().setUserAgentString(ua + "; NautilusFair");

        webView.loadUrl(url);
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

                    errorView.setVisibility(View.GONE);
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


            registerHandler("bridgeH5RegisterHandlerObj5", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    replyComment(data);
                }
            });

            registerHandler("bridgeH5RegisterHandlerObj7", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    checkMoreComment(data);
                    LogUtil.e("response", data.toString());
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

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);

        }

        @Override
        public void onReceivedError(WebView webView, int errorCode, String s, String s1) {
            super.onReceivedError(webView, errorCode, s, s1);
//            webView.loadUrl(Constant.URL.ERROR_URL);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_like:
                if (LoginWarmUtil.getInstance().checkLoginStatus(mContext))
                    praise();
                break;

            case R.id.bt_confirm:

                if (!LoginWarmUtil.getInstance().checkLoginStatus(mContext)) {

                    return;
                }

                Intent commentIntent = new Intent(mContext, EditPagerActivity.class);

                commentIntent.putExtra(Constant.KEY.ITEM_ID, itemId);

                commentIntent.putExtra(Constant.KEY.TYPE, commentType);

                startActivityForResult(commentIntent, Constant.REQUEST_CODE.EDIT_COMMENT);

                break;

            case R.id.bt_share:

                if (activeShareInfo == null) {
                    Toast.makeText(MyApplication.getInstance(), "正在获取分享信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activeShareInfo.getContentUrl() == null) {
                    Toast.makeText(MyApplication.getInstance(), "该活动没有分享信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                activeShareInfo.setTitle("鹦鹉螺市集原创文章");

                ShowShareMenuUtil.getInstance().initShareMenuDialog((Activity) mContext, activeShareInfo);

                break;

            case R.id.error:

                webView.reload();

                break;
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

    private void getShareInfo() {
        GetShareInfoRequest request = new GetShareInfoRequest(shareInfoUrl, itemId,
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

                            Toast.makeText(MyApplication.getInstance(), response.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MyApplication.getInstance(), "获取数据失败，请您稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                    }
                });
        request.setShouldCache(true);

        VolleyUtil.addToRequestQueue(request);
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

        PostLikeRequest request = new PostLikeRequest(itemId, Integer.valueOf(commentType), like, userId,
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

                            if (isLike == 1) {
                                likeImage.setImageResource(R.drawable.h5bt_like);

                                isLike = 0;
                            } else if (isLike == 0) {
                                likeImage.setImageResource(R.drawable.h5bt_islike);

                                isLike = 1;
                            }

                            switch (articleType) {
                                case ALL_ARTICLE:
                                    EventBus.getDefault().post(new EventArticleType(Integer.valueOf(commentType), isLike));
                                    break;

                                case EXPERIENCE_ARTICLE:
                                    EventBus.getDefault().post(new EventExperience(Integer.valueOf(commentType), isLike));
                                    break;

                                case HOMEPAGER_ARTICLE:
                                    EventBus.getDefault().post(new EventHomePager(Integer.valueOf(commentType), isLike));
                                    break;
                            }

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

    private void onCommentClick(String commendId) {
        Intent intent = new Intent(mContext,
                CommentDetailActivity.class);

        intent.putExtra(Constant.KEY.COMMENT_ID, commendId);

        mContext.startActivity(intent);
    }

    private void onMoreComments() {
        Intent commendListIntent = new Intent(mContext, CommentListActivity.class);

        commendListIntent.putExtra(Constant.KEY.ITEM_ID, itemId);

        commendListIntent.putExtra(Constant.KEY.ITEM_TYPE, commentType);

        startActivity(commendListIntent);
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
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(controllerView,
                    "translationY", controllerView.getTranslationY(),
                    controllerView.getHeight());// 将Button隐藏到下面
            ArrayList<Animator> animators = new ArrayList<Animator>();
            // animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideAnimatorSet.setDuration(300);
            hideAnimatorSet.playTogether(animators);
            hideAnimatorSet.start();
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
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(controllerView,
                    "translationY", controllerView.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<Animator>();
            // animators.add(headerAnimator);
            animators.add(footerAnimator);
            backAnimatorSet.setDuration(300);
            backAnimatorSet.playTogether(animators);
            backAnimatorSet.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE.EDIT_COMMENT) {
                webView.reload();
            }
        }
    }
}
