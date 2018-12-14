package com.nautilus.ywlfair.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.PreferencesUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.module.BaseActivity;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.json.JSONException;
import org.json.JSONObject;

import cn.beecloud.BeeCloud;

/**
 * Created by DY on 2015/11/13.
 */
public class MainActivity extends BaseActivity {

    public MyLocationListener mMyLocationListener;

    private LocationClient mLocationClient;

    //TabHost，用于存放主界面各Tab的Fragment
    public static FragmentTabHost mTabHost;

    //Fragment数组
    private Class<?> fragmentArray[] = new Class<?>[]{HomePagerFragment.class, RecommendFragment.class,
            GoodsFragment.class, UserCenterFragment.class};

    //Tab标签标题数组
    private int mTabNameArray[] = new int[]{R.string.tab_name_1, R.string.tab_name_2
            , R.string.tab_name_3, R.string.tab_name_4};

    private int[] mIconArray = new int[]{R.drawable.home_pager_tab_selector, R.drawable.recommend_tab_selector,
            R.drawable.original_tab_selector, R.drawable.mine_tab_selector, R.drawable.pitch_tab_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        setUpdateValue();

//        // 推荐在主Activity里的onCreate函数中初始化BeeCloud.
//        if (AppConfig.getInstance().getAccessConfig() != null) {
//            BeeCloud.setAppIdAndSecret(AppConfig.getInstance().getAccessConfig().getkBeeCloudAppID(),
//                    AppConfig.getInstance().getAccessConfig().getkBeeCloudAppSecret());
//        }

        initTCAgent();

        mLocationClient = new LocationClient(this.getApplicationContext());

        mMyLocationListener = new MyLocationListener();

        mLocationClient.registerLocationListener(mMyLocationListener);

        registerBroadCastReceiver();

        initLocation();

        initViews();

        SDKInitializer.initialize(getApplicationContext());

    }

    private void initTCAgent(){
        TalkingDataAppCpa.init(this.getApplicationContext(),Constant.TALKING_ID, Common.getAppMetaData(this,"UMENG_CHANNEL"));
    }


    @Override
    protected void onResume() {

        mTabHost.setCurrentTab(MyApplication.mainTabActivityIndex);

        super.onResume();
    }

    private void initViews() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.removeAllViews();

        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_content);

        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(String.valueOf(i)).setIndicator(getTabItemViews(i));

            mTabHost.addTab(tabSpec, fragmentArray[i], null);

        }

        mTabHost.getTabWidget().setPadding(0, 0, 0, 0);

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        mTabHost.setOnTabChangedListener(onTabChangeListener);

        setTabShow();
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemViews(int index) {

        View view = LayoutInflater.from(this).inflate(R.layout.layout_main_tab_item_child, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_main_tab_item);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_main_tab_item);
        textView.setText(mTabNameArray[index]);

        int resourceId = mIconArray[index];

        imageView.setImageResource(resourceId);

        view.setTag(index + "");

        return view;
    }

    TabHost.OnTabChangeListener onTabChangeListener = new TabHost.OnTabChangeListener() {

        @Override
        public void onTabChanged(String tabId) {

            int currentIndex = Integer.parseInt(tabId);

            MyApplication.mainTabActivityIndex = currentIndex;

            if (mTabHost.getCurrentTab() != currentIndex) {
                setCurrentPage(currentIndex);

            }
        }


    };

    public static void setCurrentPage(int index) {
        mTabHost.setCurrentTab(index);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll");

        option.setOpenGps(true);

        option.setAddrType("all");

        option.setScanSpan(3000);

        option.setLocationNotify(true);

        option.setIgnoreKillProcess(false);

        option.setEnableSimulateGps(false);

        option.setIsNeedLocationDescribe(true);

        option.setIsNeedLocationPoiList(true);

        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {// Positioning

        @Override
        public void onReceiveLocation(BDLocation location) {

            MyApplication.getInstance().setLatitude(location.getLatitude());

            MyApplication.getInstance().setLongitude(location.getLongitude());

            MyApplication.getInstance().setLocationDescription(
                    location.getAddrStr());

            if (MyApplication.getInstance().getLatitude() != 4.9E-324) {
                mLocationClient.stop();
            } else {

            }
        }

    }

    private long mExitTime = 0;

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                ToastUtil.showShortToast("再按一次退出鹦鹉螺");

                mExitTime = System.currentTimeMillis();

            } else {
                ToastUtil.cancelToast();

                finish();

                System.exit(0);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(mBroadcastReceiver);

        mLocationClient.stop();

        mLocationClient.unRegisterLocationListener(mMyLocationListener);

        mMyLocationListener = null;

        super.onDestroy();
    }

    String value = "0";

    private void setUpdateValue() {

        UmengUpdateAgent.update(this);

        OnlineConfigAgent.getInstance().updateOnlineConfig(this);

        UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);

        OnlineConfigAgent.getInstance().setOnlineConfigListener(configureListener);

        value = PreferencesUtil.getString(Constant.PRE_KEY.FORCE_UPDATE);

        if (value != null && value.equals("1")) {
            UmengUpdateAgent.forceUpdate(MainActivity.this);
        }

        UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
            @Override
            public void onClick(int i) {
                switch (i) {
                    case UpdateStatus.NotNow:
                    case UpdateStatus.Ignore:

                        value = PreferencesUtil.getString(Constant.PRE_KEY.FORCE_UPDATE);

                        if (value != null && value.equals("1")) {
                            ToastUtil.showLongToast("非常抱歉，您需要更新应用才能继续使用");

                            MainActivity.this.finish();
                        }
                        break;

                }
            }
        });
    }

    UmengOnlineConfigureListener configureListener = new UmengOnlineConfigureListener() {
        @Override
        public void onDataReceived(JSONObject json) {
            // TODO Auto-generated method stub
            if (json != null && json.has("force_update")) {
                try {
                    String updateString = json.getString(Constant.PRE_KEY.FORCE_UPDATE);

                    PreferencesUtil.putString(Constant.PRE_KEY.FORCE_UPDATE, updateString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();

            PreferencesUtil.putInt(Constant.PRE_KEY.USER_TYPE, MyApplication.getInstance().getUserType());

            setTabShow();
        }

    };

    private void setTabShow() {

        LogUtil.e("broadcase", MyApplication.getInstance().getUserType() + "------");
        if (MyApplication.getInstance().getUserType() == 1) {

            TextView textView = (TextView) mTabHost.findViewWithTag("0").findViewById(R.id.tv_main_tab_item);

            ImageView imageView = (ImageView) mTabHost.findViewWithTag("0").findViewById(R.id.iv_main_tab_item);

            int resourceId = mIconArray[mIconArray.length - 1];

            imageView.setImageResource(resourceId);

            textView.setText("摆摊");

            mTabHost.findViewWithTag("1").setVisibility(View.GONE);

            mTabHost.findViewWithTag("2").setVisibility(View.GONE);


        } else {

            TextView textView = (TextView) mTabHost.findViewWithTag("0").findViewById(R.id.tv_main_tab_item);

            textView.setText("市集");

            ImageView imageView = (ImageView) mTabHost.findViewWithTag("0").findViewById(R.id.iv_main_tab_item);

            int resourceId = mIconArray[0];

            imageView.setImageResource(resourceId);

            mTabHost.findViewWithTag("1").setVisibility(View.VISIBLE);

            mTabHost.findViewWithTag("2").setVisibility(View.VISIBLE);
        }
    }

    public void registerBroadCastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constant.BROADCAST.EXCHANGE_USER);
        myIntentFilter.addAction(Constant.BROADCAST.LOGIN_BROAD);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

}
