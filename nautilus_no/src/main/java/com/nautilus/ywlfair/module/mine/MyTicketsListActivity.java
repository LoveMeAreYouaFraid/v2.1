package com.nautilus.ywlfair.module.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ViewPagerTabAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 门票列表
 * Created by Administrator on 2016/4/25.
 */
public class MyTicketsListActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;

    private TextView channelTitle;

    private String[] titles = new String[]{"已购买", "待付款"};

    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recommend_fragment);

        position = getIntent().getIntExtra(Constant.KEY.POSITION, 0);

        channelTitle = (TextView) findViewById(R.id.tv_title);

        channelTitle.setText("我的门票");

        mViewPager = (ViewPager) findViewById(R.id.pager);

        View back = findViewById(R.id.img_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);

        ViewPagerTabAdapter viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(),
                initFragments(), titles);

        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(position);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }

    }

    private List<Fragment> initFragments() {
        Bundle bundle = new Bundle();
        Bundle bundles = new Bundle();
        List<Fragment> list = new ArrayList<>();
        bundle.putInt(Constant.KEY.TYPE, 0);
        bundles.putInt(Constant.KEY.TYPE, 1);
        list.add(MyTicketsListNoPayFragment.getInstance(bundle));
        list.add(MyTicketsListNoPayFragment.getInstance(bundles));
        return list;
    }
}
