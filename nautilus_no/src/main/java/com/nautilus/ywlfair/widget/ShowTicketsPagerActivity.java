package com.nautilus.ywlfair.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.utils.LogUtil;
import com.nautilus.ywlfair.common.utils.ToastUtil;
import com.nautilus.ywlfair.common.utils.voley.ResponseListener;
import com.nautilus.ywlfair.common.utils.voley.VolleyUtil;
import com.nautilus.ywlfair.entity.bean.TicketInfoList;
import com.nautilus.ywlfair.entity.request.GetTicketCodeStatusRequest;
import com.nautilus.ywlfair.entity.response.GutTicketCodeStatusResponse;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.ShowImageFragment;

import java.util.ArrayList;
import java.util.List;


public class ShowTicketsPagerActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    private Context mContext;

    private TextView tvNum;

    private String type;

    private List<TicketInfoList> ticketInfoList;

    private int positions;

    private myAdapter myAdapte;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.ticke_dietil_activity);
        Intent intent = getIntent();
        type = intent.getStringExtra(Constant.KEY.TYPE);
        tvNum = (TextView) findViewById(R.id.tv_num);
        ticketInfoList = (List<TicketInfoList>) intent.getSerializableExtra(Constant.KEY.PICINFO_LIST);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        View back = findViewById(R.id.back_view);
        back.setOnClickListener(this);
        myAdapte = new myAdapter(getSupportFragmentManager(), initFragments());
        mViewPager.setAdapter(myAdapte);
        tvNum.setText("1" + "/" + ticketInfoList.size());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.e("onPageScrolled", position + "");
                tvNum.setText((position + 1) + "/" + ticketInfoList.size());
                if (ticketInfoList.get(position).getStatus().equals("0")) {
                    GetTickType(ticketInfoList.get(position).getTicketCode());
                    positions = position;
                    mViewPager.setOffscreenPageLimit(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private List<Fragment> initFragments() {

        List<Fragment> list = new ArrayList<>();

        for (int i = 0; i < ticketInfoList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY.URIS, ticketInfoList.get(i).getTicketImgUrl());
            bundle.putString(Constant.KEY.CODE, ticketInfoList.get(i).getTicketCode());
            bundle.putString(Constant.KEY.TYPE, ticketInfoList.get(i).getStatus());
            list.add(ShowImageFragment.getInstance(bundle));
        }


        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_view:
                finish();
                break;
        }
    }

    class myAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragmentList;

        public myAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.setTag(ticketInfoList.get(position).getStatus());
            return super.instantiateItem(container, position);

        }
    }

    private void GetTickType(String tickCode) {
        GetTicketCodeStatusRequest request = new GetTicketCodeStatusRequest(tickCode, new ResponseListener<GutTicketCodeStatusResponse>() {
            @Override
            public void onStart() {
                ProgressDialog.getInstance().show(mContext, "获取数据中..");
            }

            @Override
            public void onCacheResponse(GutTicketCodeStatusResponse response) {

            }

            @Override
            public void onResponse(GutTicketCodeStatusResponse response) {
                if (response == null || TextUtils.isEmpty(response.getResult().getCodeStatus() + "")) {
                    ToastUtil.showShortToast("获取数据失败,请检查网络");
                    return;
                }
                ticketInfoList.get(positions).setStatus(response.getResult().getCodeStatus() + "");
                ShowImageFragment f = (ShowImageFragment) myAdapte.getItem(positions);
                f.setType(response.getResult().getCodeStatus() + "");
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                ToastUtil.showShortToast("数据获取失败..");

            }

            @Override
            public void onFinish() {
                ProgressDialog.getInstance().cancel();
            }
        });
        request.setShouldCache(false);
        VolleyUtil.addToRequestQueue(request);

    }

}