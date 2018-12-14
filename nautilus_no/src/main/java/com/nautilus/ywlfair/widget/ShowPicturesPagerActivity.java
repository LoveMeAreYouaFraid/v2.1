package com.nautilus.ywlfair.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.widget.photoview.PhotoView;
import com.nautilus.ywlfair.widget.photoview.PhotoViewAttacher.OnViewTapListener;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 图片滑动浏览界面，支持滑动翻页、手势缩放、双击放大缩小、单击返回、长按保存图片
 *
 * @author dingying
 */
public class ShowPicturesPagerActivity extends Activity implements View.OnClickListener {

    private static final String TAG = ShowPicturesPagerActivity.class.getSimpleName();

    // 显示图片的ViewPager
    private HackyViewPager mViewPager;

    private PhotoPagerAdapter mAdapter;

    // 图片数据
    private ArrayList<Uri> mList;

    // 当前图片页数
    protected int mCurrentIndex;

    private View mTopBarView;

    private TextView mTopBarTitleTextView;

    private ImageView mTopBarRightImageView;

    private boolean canDelete = true;//是否可以删除  编辑成长档案情况下 不能删除图片

    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_pictures_pager);

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        
        mTopBarView = findViewById(R.id.rl_top_bar);

        View topBarBackImageView = findViewById(R.id.tv_top_bar_back);
        topBarBackImageView.setOnClickListener(this);

        mTopBarRightImageView = (ImageView) findViewById(R.id.iv_top_bar_right);
        mTopBarRightImageView.setOnClickListener(this);

        mTopBarTitleTextView = (TextView) findViewById(R.id.tv_top_bar_title);

        // 从Intent中获取图片数据及当前图片页数
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.KEY.URIS)) {
            mList = (ArrayList<Uri>) intent.getSerializableExtra(Constant.KEY.URIS);
            
            mCurrentIndex = intent.getIntExtra(Constant.KEY.POSITION, 0);

            canDelete = intent.getBooleanExtra(Constant.KEY.CAN_DELETE, true);
        }

        if (mList == null || mList.size() == 0) {
            finish();
            return;
        }

        if(!canDelete){
            mTopBarRightImageView.setVisibility(View.GONE);
        }

        mTopBarTitleTextView.setText(mCurrentIndex + 1 + "/" + mList.size());

        mAdapter = new PhotoPagerAdapter();

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOffscreenPageLimit(0);

        mViewPager.setCurrentItem(mCurrentIndex);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

//                LogUtil.e(ShowImagesPagerActivity.class.getSimpleName(), "mCurrentIndex: " + mCurrentIndex + ", " +
//                        "position: " + position);

//				((PhotoView)(mViewPager.getChildAt(currentIndex))).setScale(1);
//				currentIndex = arg0;
                if(mCurrentIndex != position) {

//                    LogUtil.e(ShowImagesPagerActivity.class.getSimpleName(), "mViewPager.getChildAt(mCurrentIndex) !=" +
//                            " null" + String.valueOf(mViewPager.getChildAt(mCurrentIndex) != null));

                    View view = mViewPager.findViewWithTag(position);
                    if(view != null) {
                        ((PhotoView)view).setScale(1);
                    }

//                    if(mViewPager.getChildAt(mCurrentIndex) != null) {
//                        ((PhotoView)(mViewPager.getChildAt(mCurrentIndex))).setScale(1);
//                    }

                    mCurrentIndex = position;

                    mTopBarTitleTextView.setText(mCurrentIndex + 1 + "/" + mList.size());
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // 图片ViewPager的Adapter
    class PhotoPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            final PhotoView photoView = new PhotoView(container.getContext());

            final Uri uri = mList.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.color.black)
                    .considerExifParams(true)
                    .cacheInMemory(true) //缓存至内存
                    .build();

            ImageLoader.getInstance().displayImage(uri.toString(), photoView, options);
//            holder.pictureImageView.setImageURI(item);

            // 为PhotoView设置单击事件Listener，用于单击关闭界面
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    if(mTopBarView.getVisibility() != View.VISIBLE) {
                        mTopBarView.setVisibility(View.VISIBLE);
                    } else {
                        mTopBarView.setVisibility(View.GONE);
                    }
                }
            });

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            photoView.setTag(position);

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_top_bar_back:
                finishActivity();
                break;
            case R.id.iv_top_bar_right:
                if(canDelete){
                    removePicture();
                }
                break;
        }
    }

    private void removePicture() {

        final Dialog dialog = new Dialog(this, R.style.dialog);

        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_confirm, null);

        dialog.setContentView(view);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        int paddingPx = BaseInfoUtil.dip2px(MyApplication.getInstance(), 20);
        window.getDecorView().setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title);
        titleTextView.setText("提示");

        View dividerView = view.findViewById(R.id.view_divider);
        dividerView.setVisibility(View.VISIBLE);

        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        contentTextView.setVisibility(View.VISIBLE);
        contentTextView.setText("确定删除这张照片吗？");

        TextView cancelTextView = (TextView) view.findViewById(R.id.tv_left);
        cancelTextView.setText("取消");

        TextView okTextView = (TextView) view.findViewById(R.id.tv_right);
        okTextView.setText("确定");

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

                mList.remove(mCurrentIndex);

                if(mList.size() > 0) {

//                    View pageView = mViewPager.findViewWithTag(mCurrentIndex);
//
//                    LogUtil.e(TAG, "mCurrentIndex: " + mCurrentIndex + ", pageView != null: " + (pageView != null));
//
//                    if(pageView != null) {
//                        mViewPager.removeView(view);
//                    }

                    mCurrentIndex = mCurrentIndex == 0 ? 0 : mCurrentIndex - 1;

                    mTopBarTitleTextView.setText(mCurrentIndex + 1 + "/" + mList.size());

                    mAdapter = new PhotoPagerAdapter();

                    mViewPager.setAdapter(mAdapter);

                    mViewPager.setCurrentItem(mCurrentIndex);

//                    mAdapter.notifyDataSetChanged();
//                    mTopBarTitleTextView.setText(mCurrentIndex + 1 + "/" + mList.size());
                } else {
                    finishActivity();
                }
            }
        });


    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra(Constant.KEY.URIS, mList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishActivity();
    }

    class PicturesPagerAdapter extends PagerAdapter
    {
        // This holds all the currently displayable views, in order from left to right.
        private ArrayList<View> views = new ArrayList<View>();

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
        // page should be displayed, from left-to-right.  If the page no longer exists,
        // return POSITION_NONE.
        @Override
        public int getItemPosition (Object object)
        {
            int index = views.indexOf (object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
        // to add the page to the container, which is normally the ViewPager itself.  Since
        // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
        @Override
        public Object instantiateItem (ViewGroup container, int position)
        {
            View v = views.get (position);
            container.addView (v);
            return v;
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
        // is our job to remove the page from the container, which is normally the
        // ViewPager itself.  Since all our pages are persistent, we do nothing to the
        // contents of our "views" ArrayList.
        @Override
        public void destroyItem (ViewGroup container, int position, Object object)
        {
            container.removeView (views.get (position));
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager; can be used by app as well.
        // Returns the total number of pages that the ViewPage can display.  This must
        // never be 0.
        @Override
        public int getCount ()
        {
            return views.size();
        }

        //-----------------------------------------------------------------------------
        // Used by ViewPager.
        @Override
        public boolean isViewFromObject (View view, Object object)
        {
            return view == object;
        }

        //-----------------------------------------------------------------------------
        // Add "view" to right end of "views".
        // Returns the position of the new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView (View v)
        {
            return addView (v, views.size());
        }

        //-----------------------------------------------------------------------------
        // Add "view" at "position" to "views".
        // Returns position of new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView (View v, int position)
        {
            views.add (position, v);
            return position;
        }

        //-----------------------------------------------------------------------------
        // Removes "view" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView (ViewPager pager, View v)
        {
            return removeView (pager, views.indexOf (v));
        }

        //-----------------------------------------------------------------------------
        // Removes the "view" at "position" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView (ViewPager pager, int position)
        {
            // ViewPager doesn't have a delete method; the closest is to set the adapter
            // again.  When doing so, it deletes all its views.  Then we can delete the view
            // from from the adapter and finally set the adapter to the pager again.  Note
            // that we set the adapter to null before removing the view from "views" - that's
            // because while ViewPager deletes all its views, it will call destroyItem which
            // will in turn cause a null pointer ref.
            pager.setAdapter (null);
            views.remove (position);
            pager.setAdapter (this);

            return position;
        }

        //-----------------------------------------------------------------------------
        // Returns the "view" at "position".
        // The app should call this to retrieve a view; not used by ViewPager.
        public View getView (int position)
        {
            return views.get (position);
        }

        // Other relevant methods:

        // finishUpdate - called by the ViewPager - we don't care about what pages the
        // pager is displaying so we don't use this method.
    }
}
