package com.nautilus.ywlfair.module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.cache.CacheUserInfo;
import com.nautilus.ywlfair.common.utils.CacheUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.PushManager;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.dialog.AppConfigDialog;
import com.nautilus.ywlfair.entity.bean.UserInfo;
import com.nautilus.ywlfair.entity.request.GetAppConfigRequest;
import com.nautilus.ywlfair.entity.request.PostAppInitializationRequest;
import com.nautilus.ywlfair.entity.response.GetAppConfigResponse;
import com.nautilus.ywlfair.entity.response.GetAppInitializationResponse;
import com.nautilus.ywlfair.module.main.MainActivity;
import com.nautilus.ywlfair.widget.CirclePageIndicator;
import com.tendcloud.appcpa.TalkingDataAppCpa;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity implements AppConfigDialog.LoginInputListener {

    private ImageView imageView;

    private int[] images = {R.drawable.splash1, R.drawable.splash2, R.drawable.splash3, R.drawable.splash4, R.drawable.splash5};

    private List<ImageView> views;

    private ViewPager viewPager;

    private View enterView;

    private View jumpView;

    private AlphaAnimation animation;

    private boolean isConfig = false;

    private boolean isAnimationEnd = false;

    private int errorType = 0;

    private int configRetryCount = 0;

    private int initRetryCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // 隐藏android系统的状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 隐藏应用程序的标题栏，即当前activity的标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_activity);

        if (!isTaskRoot()) {
            finish();
            return;
        }

        boolean isFirst = PreferencesUtil.getBoolean(Constant.PRE_KEY.FIRST_BOOT, false);

        MyApplication.getInstance().setUserType(PreferencesUtil.getInt(Constant.PRE_KEY.USER_TYPE, 0));

        if (!isFirst) {

            PreferencesUtil.putBoolean(Constant.PRE_KEY.FIRST_BOOT, true);

            showFirstView();
        } else {
            showNormalView();
        }

        startActivity(new Intent(this,MainActivity.class));
//        getAppConfig();

    }

    private void showFirstView() {
        views = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            imageView.setImageResource(images[i]);

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            views.add(imageView);
        }

        viewPager = (ViewPager) findViewById(R.id.splash_view_pager);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return images.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));

                return views.get(position);
            }
        });

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);

        indicator.setVisibility(View.VISIBLE);

        viewPager.setVisibility(View.VISIBLE);

        indicator.setViewPager(viewPager);

        enterView = findViewById(R.id.tv_enter);

        jumpView = findViewById(R.id.tv_jump);
        jumpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);

                startActivity(intent);

                finish();
            }
        });


        enterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);

                startActivity(intent);

                finish();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    enterView.setVisibility(View.VISIBLE);
                } else {
                    enterView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showNormalView() {

        imageView = (ImageView) findViewById(R.id.iv_splash);

        imageView.setVisibility(View.VISIBLE);

        animation = new AlphaAnimation(1.0f, 1.0f);

        animation.setDuration(3000);// 设置动画显示时间

        imageView.startAnimation(animation);

        animation.setAnimationListener(new AnimationImpl());

        CacheUserInfo cacheUserInfo = new CacheUserInfo();

        UserInfo userInfo = cacheUserInfo.getUserInfo();

        if (userInfo != null) {

            TalkingDataAppCpa.init(this.getApplicationContext(), Constant.TALKING_ID, Common.getAppMetaData(this, "UMENG_CHANNEL"));

            TalkingDataAppCpa.onLogin(userInfo.getUserId() + "");

            MyApplication.getInstance().setCurrentUser(userInfo);

            String accessToken = PreferencesUtil.getString(Constant.PRE_KEY.ACCESSTOKEN);

            MyApplication.getInstance().setAccessToken(accessToken);

            MyApplication.getInstance().setLogin(true);
        }
    }

    private class AnimationImpl implements AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            isAnimationEnd = true;

            if (!isConfig) {
                return;
            }
            processIntent();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void processIntent() {
        // 动画结束后跳转到别的页面
        Intent intent = new Intent(SplashActivity.this,
                MainActivity.class);

        startActivity(intent);

        finish();
    }

//    private void getAppConfig() {
//        GetAppConfigRequest getAppConfigRequest = new GetAppConfigRequest(new ResponseListener<GetAppConfigResponse>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onCacheResponse(GetAppConfigResponse response) {
//
//            }
//
//            @Override
//            public void onResponse(GetAppConfigResponse response) {
//
//                if (response != null && response.getAppConfig() != null) {
//
//
//                    CacheUtil.putCache("app_config", response.getAppConfig());
//
//                } else {
//
//                    errorType = 0;
//
//                    getInitializationData();
//
//                }
//
//            }
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (configRetryCount >= 3) {
//                    AppConfigDialog.getInstance().show(getFragmentManager(), null);
//                } else {
//                    errorType = 0;
//
//                    handler.sendEmptyMessageDelayed(errorType, 1000);
//                }
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//
//        getAppConfigRequest.setShouldCache(false);
//
//        VolleyUtil.addToRequestQueue(getAppConfigRequest);
//    }

    private void getInitializationData() {

        String deviceId = MyApplication.getInstance().getDeviceId() + "";

        PostAppInitializationRequest request = new PostAppInitializationRequest(deviceId, new ResponseListener<GetAppInitializationResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCacheResponse(GetAppInitializationResponse response) {

            }

            @Override
            public void onResponse(GetAppInitializationResponse response) {

                if (response != null && response.getResult() != null) {

                    MyApplication.calendarUrl = response.getResult().getCalendarUrl();

                    MyApplication.getInstance().setMessageCount(response.getResult().getUserMsg());

                    MyApplication.getInstance().setDeviceId(response.getResult().getDeviceId());

                    MyApplication.getInstance().setActDrawUrl(response.getResult().getActDrawUrl());

                    MyApplication.getInstance().setActDrawLogUrl(response.getResult().getActDrawLogUrl());

                    PreferencesUtil.putString(Constant.PRE_KEY.ACT_MAIN_URL, response.getResult().getActMainUrl());

                    MyApplication.getInstance().setActMainUrl(response.getResult().getActMainUrl());

                    if (response.getResult().getUserLoginFlag() == 1) {

                        MyApplication.getInstance().setCurrentUser(response.getResult().getUserInfo());

                        MyApplication.getInstance().setLogin(true);

                        PushManager.bindPushAccount();

                    } else {

                        MyApplication.getInstance().setLogin(false);

                        MyApplication.getInstance().setCurrentUser(null);

                    }

                    isConfig = true;

                    if (isAnimationEnd) {
                        processIntent();
                    }

                } else {
                    AppConfigDialog.getInstance().show(getFragmentManager(), null);

                    errorType = 1;
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

                if (initRetryCount >= 3) {
                    AppConfigDialog.getInstance().show(getFragmentManager(), null);
                } else {
                    errorType = 1;

                    handler.sendEmptyMessageDelayed(errorType, 1000);
                }
            }

            @Override
            public void onFinish() {

            }
        });

        request.setShouldCache(false);

        VolleyUtil.addToRequestQueue(request);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

//                    getAppConfig();

                    configRetryCount = configRetryCount + 1;

                    break;

                case 1:

                    getInitializationData();

                    initRetryCount = initRetryCount + 1;

                    break;
            }
        }
    };


    @Override
    public void onLoginInputComplete(int type) {

        if (errorType == 0) {
//            getAppConfig();
        } else {
            getInitializationData();
        }

    }

}
