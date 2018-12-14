package com.nautilus.ywlfair.module.booth;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ViewPagerTabAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public class MyBoothActivity extends BaseActivity {

    private String[] titles = new String[]{"已购买", "待付款"};

    private ViewPager mViewPager;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_booth_activity);

        position = getIntent().getIntExtra(Constant.KEY.POSITION, 0);

        View back = findViewById(R.id.tv_top_bar_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.pager);

        initPagers();
    }

    private void initPagers() {

        ViewPagerTabAdapter viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(),
                initFragments(),titles);

        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(position);

    }

    private List<Fragment> initFragments() {
        Bundle bundle = new Bundle();

        List<Fragment> list = new ArrayList<>();

        bundle.putInt(Constant.KEY.TYPE, 0);

        list.add(MyBoothFragment.getInstance(bundle));

        Bundle bundle1 = new Bundle();

        bundle1.putInt(Constant.KEY.TYPE, 1);

        list.add(MyBoothFragment.getInstance(bundle1));

        return list;
    }
}
