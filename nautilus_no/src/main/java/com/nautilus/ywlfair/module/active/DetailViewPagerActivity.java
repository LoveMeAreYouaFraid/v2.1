package com.nautilus.ywlfair.module.active;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ViewPagerTabAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.common.utils.ShareUtil;
import com.nautilus.ywlfair.common.utils.ShowShareMenuUtil;
import com.nautilus.ywlfair.common.utils.voley.CustomError;
import com.nautilus.ywlfair.common.utils.voley.InterfaceResponse;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.ActiveShareInfo;
import com.nautilus.ywlfair.entity.request.GetShareInfoRequest;
import com.nautilus.ywlfair.entity.response.GetShareInfoResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.active.ActiveFragment.GetShareInfoListener;
import com.umeng.socialize.sso.UMSsoHandler;

import java.util.ArrayList;
import java.util.List;

public class DetailViewPagerActivity extends BaseActivity implements
        OnClickListener, GetShareInfoListener {

    private String[] titles = new String[]{"活动", "详情", "记录"};

    private Activity mContext;

    private ViewPager mViewPager;

    private ImageView mImageViewBack, shareBtn;

    private String itemId;

    public static ActiveShareInfo activeShareInfo;

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.detail_tabs);

        mContext = this;

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            itemId = arguments.getString(Constant.KEY.ITEM_ID);

            currentIndex = arguments.getInt(Constant.KEY.CURRENT_INDEX, 0);
        }

        mViewPager = (ViewPager) findViewById(R.id.pager);

        mImageViewBack = (ImageView) findViewById(R.id.back_button);
        mImageViewBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareBtn = (ImageView) findViewById(R.id.iv_share);
        shareBtn.setOnClickListener(this);

        initPagers();
    }


    private void initPagers() {

        ViewPagerTabAdapter viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(),
                initFragments(),titles);

        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(currentIndex);
    }


    private List<Fragment> initFragments() {
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constant.KEY.ITEM_ID, itemId);

        List<Fragment> list = new ArrayList<>();

        ActiveFragment activeFragment = ActiveFragment.getInstance(bundle);
        activeFragment.setGetShareInfoListener(this);

        list.add(activeFragment);

        list.add(ActiveDetailFragment.getInstance(bundle));

        list.add(LiveFragment.getInstance(bundle));

        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_share:

                if (activeShareInfo == null) {
                    Toast.makeText(MyApplication.getInstance(), "正在获取分享信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activeShareInfo.getContentUrl() == null) {
                    Toast.makeText(MyApplication.getInstance(), "该活动没有分享信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                ShowShareMenuUtil.getInstance().initShareMenuDialog(mContext,activeShareInfo);
                break;

        }

    }


    @Override
    public void getShareInfoStart(String itemId) {

        GetShareInfoRequest request = new GetShareInfoRequest(Constant.URL.GET_ACTIVITY_SHARE_INFO,itemId,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = ShareUtil.mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


}
