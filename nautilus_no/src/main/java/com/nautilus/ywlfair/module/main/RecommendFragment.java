package com.nautilus.ywlfair.module.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ViewPagerTabAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.recommend.AllRecommendFragment;
import com.nautilus.ywlfair.module.recommend.ExperienceFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/14.
 */
public class RecommendFragment extends Fragment {

    private String[] titles = new String[]{"全部", "经验", "攻略"};

    private ViewPager mViewPager;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.recommend_fragment, null);

        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        TextView title = (TextView) rootView.findViewById(R.id.tv_title);
        title.setText("推荐");
        View back = rootView.findViewById(R.id.img_back);
        back.setVisibility(View.INVISIBLE);

        initPagers();

        return rootView;
    }

    private void initPagers() {

        ViewPagerTabAdapter viewPagerTabAdapter = new ViewPagerTabAdapter(getChildFragmentManager(),
                initFragments(), titles);

        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private List<Fragment> initFragments() {
        Bundle bundle = new Bundle();

        List<Fragment> list = new ArrayList<>();

        list.add(AllRecommendFragment.getInstance(bundle));

        bundle.putInt(Constant.KEY.TYPE, 1);
        list.add(ExperienceFragment.getInstance(bundle));

        Bundle bundle1 = new Bundle();
        bundle1.putInt(Constant.KEY.TYPE, 2);
        list.add(ExperienceFragment.getInstance(bundle1));

        return list;
    }


}
