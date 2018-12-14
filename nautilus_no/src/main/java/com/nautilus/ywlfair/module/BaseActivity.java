package com.nautilus.ywlfair.module;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.nautilus.ywlfair.BuildConfig;
import com.nautilus.ywlfair.common.utils.KeyBoardUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Copyright (©) 2014 Shanghai Russula Network Technology Co., Ltd
 * <p/>
 * 基本界面，用于加入友盟统计代码
 *
 * @author dingying
 * @version 1.0, 2015/3/6 17:22
 * @since 2015/3/6
 */
public class BaseActivity extends AppCompatActivity {

    protected void onResume() {

        KeyBoardUtil.hideSoftKeyboard(this);

        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

}
