package com.nautilus.ywlfair.module.mine;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.adapter.ViewPagerTabAdapter;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.module.mine.comment.MyCommentFragment;

import java.util.ArrayList;
import java.util.List;

public class MyCommentsActivity extends BaseActivity implements OnClickListener {

    private String[] titles = new String[]{"收到的评论", "发表的评论"};

    private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.my_commenton);

		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        initPagers();

	}

    private void initPagers() {

        ViewPagerTabAdapter viewPagerTabAdapter = new ViewPagerTabAdapter(getSupportFragmentManager(),
                initFragments(), titles);

        mViewPager.setAdapter(viewPagerTabAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private List<Fragment> initFragments() {
        List<Fragment> list = new ArrayList<>();

        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY.TYPE, 2);
        list.add(MyCommentFragment.getInstance(bundle));

        Bundle bundle1 =new Bundle();
        bundle1.putInt(Constant.KEY.TYPE, 1);
        list.add(MyCommentFragment.getInstance(bundle1));

        return list;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_bar_back:
			finish();
			break;
		}

	}

}