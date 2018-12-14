package com.nautilus.ywlfair.widget;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nautilus.ywlfair.entity.bean.PicInfo;
import com.nautilus.ywlfair.module.BaseActivity;
import com.nautilus.ywlfair.R;
import com.nautilus.ywlfair.common.Constant;
import com.nautilus.ywlfair.common.MyApplication;
import com.nautilus.ywlfair.widget.city.FileUtil;
import com.nautilus.ywlfair.widget.photoview.PhotoView;
import com.nautilus.ywlfair.widget.photoview.PhotoViewAttacher;
import com.nautilus.ywlfair.common.utils.BaseInfoUtil;
import com.nautilus.ywlfair.common.utils.TimeUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

/**
 * 图片滑动浏览界面，支持滑动翻页、手势缩放、双击放大缩小、单击返回、长按保存图片
 *
 * @author dingying
 */
public class ShowImagesPagerActivity extends BaseActivity {

    private static final String TAG = ShowImagesPagerActivity.class.getSimpleName();

    // 显示图片的ViewPager
    private HackyViewPager mViewPager;

    private PhotoPagerAdapter mAdapter;

    // 图片数据
    private ArrayList<PicInfo> mAttachmentList;

    // 当前图片页数
    protected int mCurrentIndex;

    private int totalCount;

    private TextView pagerNum;

    public enum Mode{ PICTURE, TICKET};

    private Mode mode = Mode.PICTURE;

    // 正在后台下载中的图片的Map
//    private HashMap<Integer, String> mBackgroundLoadingMap = new HashMap<>();
    private HashSet<Integer> mBackgroundLoadingSet = new HashSet<Integer>();

    @SuppressWarnings("unchecked")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showimages_pager);

        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

        pagerNum = (TextView) findViewById(R.id.tv_pager_num);

        // 从Intent中获取图片数据及当前图片页数
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.KEY.PICINFO_LIST)) {
            mAttachmentList = (ArrayList<PicInfo>) intent
                    .getSerializableExtra(Constant.KEY.PICINFO_LIST);
            mCurrentIndex = intent.getIntExtra(Constant.KEY.POSITION, 0);

            totalCount = mAttachmentList.size();

            pagerNum.setText((mCurrentIndex+1) + "/" + totalCount);
        }

        if(intent != null && intent.hasExtra(Constant.KEY.MODE)){
           mode = (Mode) intent.getSerializableExtra(Constant.KEY.MODE);
        }

        if (mAttachmentList == null || mAttachmentList.size() == 0) {
            return;
        }

        mAdapter = new PhotoPagerAdapter();

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOffscreenPageLimit(1);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @SuppressWarnings("deprecation")
			@Override
            public void onPageSelected(int position) {

                pagerNum.setText((position+1) + "/" + totalCount);

                final PicInfo attachment = mAttachmentList.get(position);

                final String bigImagePath = attachment.getImgUrl();

                if (mCurrentIndex != position
                        && !mBackgroundLoadingSet.contains(position)
                        && (ImageLoader.getInstance().getDiskCache().get(bigImagePath) == null
                                || !ImageLoader.getInstance().getDiskCache().get(bigImagePath).exists())) {

//                    LogUtil.e(ShowImagesPagerActivity.class.getSimpleName(), "mViewPager.getChildAt(mCurrentIndex) !=" +
//                            " null" + String.valueOf(mViewPager.getChildAt(mCurrentIndex) != null));

                    View pageView = mViewPager.findViewWithTag(position);

                    if (pageView != null) {

                        PhotoView photoView = (PhotoView) pageView.findViewById(R.id.iv_large);
                        final ImageView smallPhoto = (ImageView) pageView.findViewById(R.id.iv_small);
                        final ProgressBar progressBar = (ProgressBar) pageView.findViewById(R.id.pb_loading);

                        final DisplayImageOptions options = new DisplayImageOptions.Builder()

                                .cacheOnDisk(true)
                                .cacheInMemory(true)
                                .imageScaleType(ImageScaleType.EXACTLY)
                                .resetViewBeforeLoading(false)
                                .showImageOnLoading(0)
                                .build();

                        photoView.setScale(1);
//                        ((PhotoView) view).setScale(1);

                        //加载大图
                        ImageLoader.getInstance().displayImage(bigImagePath, photoView, options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                progressBar.setVisibility(View.GONE);
                                smallPhoto.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

                mCurrentIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        int position = mCurrentIndex;

        mViewPager.setCurrentItem(position);
    }


    // 图片ViewPager的Adapter
    class PhotoPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mAttachmentList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {

            View pageView = LayoutInflater.from(container.getContext()).inflate(
                    R.layout.view_vp_photo_view, null);

//            final PhotoView photoView = new PhotoView(container.getContext());
            final PhotoView photoView = (PhotoView) pageView.findViewById(R.id.iv_large);
            final ImageView smallPhoto = (ImageView) pageView.findViewById(R.id.iv_small);
            final ProgressBar progressBar = (ProgressBar) pageView.findViewById(R.id.pb_loading);

            final PicInfo attachment = mAttachmentList.get(position);

            final String bigImagePath = attachment.getImgUrl();

            String smallImagePath = attachment.getThumbnailUrl();

            ImageLoader imageLoader = ImageLoader.getInstance();

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();

            final DisplayImageOptions options2 = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .resetViewBeforeLoading(false)
                    .showImageOnLoading(0)
                    .build();


            progressBar.setVisibility(View.VISIBLE);

            //如果有小图缓存，并且大图无缓存，则先显示小图
            if ((imageLoader.getDiskCache().get(smallImagePath) != null
                    || MemoryCacheUtils.findCachedBitmapsForImageUri(smallImagePath, ImageLoader.getInstance().getMemoryCache()) != null)
//                    && (imageLoader.getDiskCache().get(bigImagePath) == null
//                        || !imageLoader.getDiskCache().get(bigImagePath).exists())
                    ) {

                smallPhoto.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(smallImagePath, smallPhoto, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

            }

            if (mCurrentIndex == position) {

                //加载大图
                ImageLoader.getInstance().displayImage(bigImagePath, photoView, options2, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                        smallPhoto.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                //加载大图
                ImageLoader.getInstance().displayImage(bigImagePath, photoView, options2, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        mBackgroundLoadingSet.add(position);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        mBackgroundLoadingSet.remove(position);
                        progressBar.setVisibility(View.GONE);
                        if (mViewPager.getCurrentItem() == position) {
                            Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mBackgroundLoadingSet.remove(position);
                        progressBar.setVisibility(View.GONE);
                        smallPhoto.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        mBackgroundLoadingSet.remove(position);
                        progressBar.setVisibility(View.GONE);
                        if (mViewPager.getCurrentItem() == position) {
                            Toast.makeText(MyApplication.getInstance(), "下载原图失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            smallPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            // 为PhotoView设置单击事件Listener，用于单击关闭界面
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    finish();
                }
            });

            // 为PhotoView设置长按事件Listener，用于长按弹出保存图片对话框
            photoView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {

                    @SuppressWarnings("deprecation")
					File file = ImageLoader.getInstance().getDiskCache().get(bigImagePath);

                    showSaveConfirmDialog(file.toString());

                    return false;
                }
            });

            // Now just add PhotoView to ViewPager and return it
            container.addView(pageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            pageView.setTag(position);

            return pageView;
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

    /**
     * 保存图片到手机相册
     *
     * @param filePath 图片文件路径
     */
    public void savePic(String filePath) {

        String fileName = TimeUtil.convertDateTime2DateStr(new Date(System.currentTimeMillis()));
        String uri = null;
        try {
            uri = MediaStore.Images.Media.insertImage(MyApplication.getInstance()
                    .getContentResolver(), filePath, fileName, "");
        } catch (FileNotFoundException e) {
            Toast.makeText(MyApplication.getInstance(), "图片保存失败，请您稍后重试", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (uri != null) {
            String picPath = FileUtil.uri2Path(Uri.parse(uri), ShowImagesPagerActivity.this);
            MediaScannerConnection.scanFile(MyApplication.getInstance(), new String[]{Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                    + "/" + picPath.split("/")[picPath.split("/").length - 1]}, null, null);
            Toast.makeText(MyApplication.getInstance(), "图片保存成功，请您到手机相册中查看", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(MyApplication.getInstance(), "图片保存失败，请您稍后重试", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 弹出保存图片确认对话框
     *
     * @param filePath
     */
    private void showSaveConfirmDialog(final String filePath) {

//        final Dialog dialog = new AlertDialog.Builder(this).create();
//
//        dialog.show();
//
//        Window window = dialog.getWindow();
//
//        window.setContentView(R.layout.xingxing_dialog);
//
//        TextView tip = (TextView) window.findViewById(R.id.tipContentTextView);
//
//        tip.setText("您要保存图片到手机上吗？");
//        Button ok = (Button) window.findViewById(R.id.btn_ok);
//        ok.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                new Handler().post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        savePic(filePath);
//                    }
//                });
//                dialog.cancel();
//            }
//        });
//        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });


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
        contentTextView.setText("要保存图片到手机相册吗？");

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
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        savePic(filePath);
                    }
                });
            }
        });

    }

}