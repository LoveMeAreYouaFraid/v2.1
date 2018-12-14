package com.nautilus.ywlfair.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.widget.photoview.PhotoView;
import com.nautilus.ywlfair.common.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

//预览选择过的照片
public class YuLanActivity extends Activity implements OnClickListener {

	// private ArrayList<ImageView> views;
	private ArrayList<Uri> uris = new ArrayList<Uri>();
	private HackyViewPager viewPager;
	private boolean isSeResult;
	protected int currentIndex = 0;
	private int checkPostion = 0;// 查看第几张图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_yulan);

		// 是否是startActivityForResult()的方式开启的intent
		isSeResult = getIntent().getBooleanExtra(Constant.KEY.IS_SET_RESULT,
				false);
		// 获取传过来的Uri列表
		uris = getIntent().getParcelableArrayListExtra(Constant.KEY.URIS);

		checkPostion = getIntent().getIntExtra(Constant.KEY.POSITION, 0);

		initTopView();

		initUI();
	}

	// 初始化标题栏
	public void initTopView() {
		View topBarBack = findViewById(R.id.tv_top_bar_back);
		topBarBack.setOnClickListener(this);
	}

	/**
	 * 初始viewpager化控件
	 */
	public void initUI() {
		viewPager = (HackyViewPager) findViewById(R.id.pager);
		Button wancheng = (Button) findViewById(R.id.wancheng);
		wancheng.setOnClickListener(this);
		viewPager.setAdapter(new PhotoPagerAdapter());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				currentIndex = index;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setCurrentItem(checkPostion);
	}

	// 图片ViewPager的Adapter
	class PhotoPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return uris.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());

			ImageLoadUtils.setItemImageView(photoView, uris.get(position).toString(), R.drawable.default_image, ImageScaleType.EXACTLY, false);

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_top_bar_back:
			finish();
			break;
		case R.id.wancheng:
			Intent intent = new Intent(YuLanActivity.this,
					XiangCeActivity.class);
			intent.putParcelableArrayListExtra(Constant.KEY.URIS, uris);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}
}
