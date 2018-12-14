package com.nautilus.ywlfair.module.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;

public class MyOrderFormActivity extends BaseActivity implements OnClickListener {

	private Context mContext;

	private ViewPager mViewPager;

	private String[] titles = new String[] { "全部", "待付款", "待发货", "待收货", "待评论", "已评论" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_order_form);

		mContext = this;

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		init();
	}

	private void init() {
		mViewPager.setAdapter(new MyViewPagerAdapter(
				getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_bar_back:
			finish();
			break;
		}

	}

	class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Bundle bundle = new Bundle();

			int status = position - 1;

			if (position == 0) {
				status = 100;
			}

            bundle.putInt(Constant.KEY.TYPE, status);

			Fragment fragment = OrderListFragment.getInstance(bundle);

			return fragment;
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return titles[position];
		}

	}
	

}