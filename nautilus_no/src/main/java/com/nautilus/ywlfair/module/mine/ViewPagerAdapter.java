package com.nautilus.ywlfair.module.mine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * package com.nautilus.ywlfair.adapter;
 * <p/>
 * import android.support.v4.app.Fragment;
 * import android.support.v4.app.FragmentManager;
 * import android.support.v4.app.FragmentPagerAdapter;
 * <p/>
 * import java.util.List;
 * <p/>
 * /**
 * Created by Administrator on 2015/11/19.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] titles) {
        super(fm);
        mFragmentList = fragmentList;

        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
