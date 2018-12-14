package com.nautilus.ywlfair.module;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.widget.ProgressDialog;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoPlayerActivity extends BaseActivity implements OnClickListener {

    private MediaController mediaController;
    private TimeCount time;
    private ImageView tv_top_bar_back;
    private long mExitTime;
    private Intent intent;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();

        url = intent.getStringExtra(Constant.KEY.URL);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

        setContentView(R.layout.activity_video_player);

        Display mDisplay = getWindowManager().getDefaultDisplay();

        int W = mDisplay.getWidth();//屏幕宽高

        int H = mDisplay.getHeight();

        tv_top_bar_back = (ImageView) findViewById(R.id.img_back);
        tv_top_bar_back.setOnClickListener(this);

        VideoView videoView = (VideoView) findViewById(R.id.vv_video);

        time = new TimeCount(3000, 1000);
        time.start();
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        if (!TextUtils.isEmpty(url)) {
            videoView.setVideoURI(Uri.parse(url));
        } else {
            ToastUtil.showShortToast("视频不存在!");

            finish();
        }

        ProgressDialog.getInstance().show(this, "视频载入中...");

         videoView.start();

        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                ProgressDialog.getInstance().cancel();
            }
        });


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(layoutParams);
        videoView.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    class TimeCount extends CountDownTimer {// 计时器

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            tv_top_bar_back.setVisibility(View.INVISIBLE);

        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            tv_top_bar_back.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                mExitTime = System.currentTimeMillis();
                time.start();
            } else {
                if (tv_top_bar_back.getVisibility() == View.VISIBLE) {
                    tv_top_bar_back.setVisibility(View.INVISIBLE);
                } else {
                    tv_top_bar_back.setVisibility(View.VISIBLE);
                }
            }
            Log.i("123", "down");


        }

        return super.onTouchEvent(event);
    }

}
