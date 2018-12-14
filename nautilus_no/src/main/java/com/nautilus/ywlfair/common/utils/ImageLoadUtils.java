package com.nautilus.ywlfair.common.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.widget.ImageView;

import com.nautilus.ywlfair.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;

public class ImageLoadUtils {
    private static final int THREAD_POOL_NUM = 5;

    private ImageLoadUtils() {

    }

    public static DisplayImageOptions createNoRoundedOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_image)
                .showImageForEmptyUri(R.drawable.default_image)
                .showImageOnFail(R.drawable.default_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
        return options;
    }

    public static DisplayImageOptions createDisplayOptions(int rounded) {
        return createDisplayOptions(rounded, R.color.content_color, R.drawable.default_image, R.drawable.default_image, true, true);
    }

    public static DisplayImageOptions createDisplayOptions(int rounded,boolean isCacheDisk,boolean isCacheMemory) {
        return createDisplayOptions(rounded, R.color.light_light_gray, R.drawable.default_image, R.drawable.default_image, isCacheDisk, isCacheMemory);
    }


    public static DisplayImageOptions createDisplayOptions(int rounded, int loadingImageId, int emptyUriImageId, int failImageId, boolean isCacheDisk, boolean isCacheMemory) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(loadingImageId) // 设置图片下载期间显示的图�?
                .showImageForEmptyUri(emptyUriImageId) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(failImageId) // 设置图片加载或解码过程中发生错误显示的图�?
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复�?
                        //.delayBeforeLoading(1000)  // 下载前的延迟时间
                .cacheInMemory(isCacheMemory) // default  设置下载的图片是否缓存在内存�?
                .cacheOnDisk(isCacheDisk) // default  设置下载的图片是否缓存在SD卡中
                        //.considerExifParams(false) // default
                .imageScaleType(ImageScaleType.EXACTLY) // default 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类�?
                .displayer(new RoundedBitmapDisplayer(rounded))
                        //.decodingOptions(...)  // 图片的解码设�?
                        //.displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                        //.handler(new Handler()) // default
                .build();

        return options;
    }

    public static void setItemImageView(ImageView imageView, String imageUrl, int resId, ImageScaleType type,
                                        BitmapProcessor processor,boolean isResetView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) // image在加载过程中，显示的图片
                .showImageForEmptyUri(resId) // empty URI时显示的图片
                .showImageOnFail(resId) // 不是图片文件 显示图片
                .resetViewBeforeLoading(isResetView) // default
                .cacheInMemory(false) // default 不缓存至内存
                .imageScaleType(type)// default
                .cacheOnDisk(true) // default 不缓存至手机SDCard
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
//				.displayer(new SimpleBitmapDisplayer()) // default
//														// 可以设置动画，比如圆角或者渐�?
//				.handler(new Handler()) // default
                .preProcessor(processor).build();

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);

    }

    public static void setItemImageView(ImageView imageView, String imageUrl, int resId, ImageScaleType ImageScaleType, boolean isResetView) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) // image在加载过程中，显示的图片
                .showImageForEmptyUri(resId) // empty URI时显示的图片
                .showImageOnFail(resId) // 不是图片文件 显示图片
                .resetViewBeforeLoading(isResetView)
                .cacheInMemory(true) // 缓存至内�?
                .imageScaleType(ImageScaleType)	//
                .cacheOnDisk(true) // 缓存至手机SDCard
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }
    
    public static void setRoundHeadView(ImageView imageView, String imageUrl, int resId,int rounded) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) // image在加载过程中，显示的图片
                .showImageForEmptyUri(resId) // empty URI时显示的图片
                .showImageOnFail(resId) // 不是图片文件 显示图片
                .resetViewBeforeLoading(true)
                .cacheInMemory(true) // 缓存至内�?
                .imageScaleType(ImageScaleType.EXACTLY)	//
                .cacheOnDisk(true) // 缓存至手机SDCard
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new RoundedBitmapDisplayer(rounded))
                .build();

        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
    }

    /** 从给定路径加载图�? */
    public static Bitmap loadBitmap(String imgpath, Options options) {
        return BitmapFactory.decodeFile(imgpath, options);
    }

    /** 从给定的路径加载图片，并指定是否自动旋转方向 */
    public static Bitmap loadExifBitmap(String imgpath, Options options, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath, options);
        } else {
            Bitmap bm = loadBitmap(imgpath, options);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信�?
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                if (bm != null) {
                    bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
                }
            }
            return bm;
        }
    }
}
