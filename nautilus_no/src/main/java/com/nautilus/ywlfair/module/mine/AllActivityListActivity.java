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
 * 全部活动
 * Created by Administrator on 2015/10/17.
 */
public class AllActivityListActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager mViewPager;

    private ViewPagerTabAdapter viewPagerTabAdapter;

    private String[] titles = new String[]{"近期热门", "往期活动"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_active_list_activity);

        findViewById(R.id.tv_title).setOnClickListener(this);

        TextView channelTitle = (TextView) findViewById(R.id.tv_title);
        channelTitle.setText("全部活动");
        View back = findViewById(R.id.img_back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(this);
        viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(),
                initFragments(), titles);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_title:
                rollToTop();
                break;
        }

    }

    private void rollToTop(){
        Fragment fragment = viewPagerTabAdapter.getItem(mViewPager.getCurrentItem());

        if(fragment instanceof NowActivityListFragment){
            ((NowActivityListFragment)fragment).scrollTop();
        }
    }

    private List<Fragment> initFragments() {
        Bundle bundle = new Bundle();
        Bundle bundles = new Bundle();
        List<Fragment> list = new ArrayList<>();
        bundle.putInt(Constant.KEY.TYPE, 1);
        list.add(NowActivityListFragment.getInstance(bundle));
        bundles.putInt(Constant.KEY.TYPE, 0);
        list.add(NowActivityListFragment.getInstance(bundles));
        return list;
    }
}
