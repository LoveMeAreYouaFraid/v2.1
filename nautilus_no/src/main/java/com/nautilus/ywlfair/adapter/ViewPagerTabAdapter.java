package com.nautilus.ywlfair.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/11/19.
 */
public class ViewPagerTabAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList;

    private String[] titles;

    public ViewPagerTabAdapter(FragmentManager fm) {
        super(fm);
    }

    public ViewPagerTabAdapter(FragmentManager fm, List<Fragment> fragmentList,String[] titles) {
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

        return titles[position];
    }

}
