package com.nautilus.ywlfair.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nautilus.ywlfair.common.Common;
import com.nautilus.ywlfair.entity.bean.PictureInfo;

/**
 * Created by dingying on 2015/4/13.
 */
public class PictureUtils {

    /**
     * 处理图片文件，转换大小并进行压缩
     *
     * @param pictureUri
     * @return
     */
    public static PictureInfo processPictureFile(final String cacheDirName, final Object pictureUri) {

        int dw = calculateSize(Common.getInstance().getScreenWidth());
        int dh = calculateSize(Common.getInstance().getScreenHeight());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        String uriPath;

        if(pictureUri instanceof Uri){
            uriPath = FileUtils.uri2Path((Uri)pictureUri);
        } else {
            uriPath = (String)pictureUri;
        }

        if(TextUtils.isEmpty(uriPath)) {
            return null;
        }

        Bitmap pic = ImageLoadUtils.loadExifBitmap(uriPath, options, true);

        int wRatio = (int) Math.ceil(options.outWidth / (float) dw);
        int hRatio = (int) Math.ceil(options.outHeight / (float) dh);

        options.inSampleSize = 1;

        if (wRatio > 1 || hRatio > 1) {
            if (wRatio > hRatio) {
                options.inSampleSize = wRatio;
            } else {
                options.inSampleSize = hRatio;
            }
        }
        options.inJustDecodeBounds = false;

        PictureInfo pictureInfo = new PictureInfo();
        pictureInfo.setWidth(options.outWidth / options.inSampleSize);
        pictureInfo.setHeight(options.outHeight / options.inSampleSize);

        pic = ImageLoadUtils.loadExifBitmap(uriPath, options, true);
        // Log.i("file.length()",
        // "此次上传的图片的宽高分别是："+pic.getWidth()+","+pic.getHeight());

        String pictureName = UUID.randomUUID().toString() + ".jpg";

        File file = FileUtils.getDiskCacheFile(cacheDirName, pictureName);

        try {
            saveBitmapToFile(pic, file.getPath(), 0.8f);

            if(file.exists()) {
                pictureInfo.setFile(file);
            } else {
                return null;
            }

        } catch (IOException e) {
            return null;
        } finally {
            if (pic != null && !pic.isRecycled()) {
                pic.recycle();
            }
        }

        return pictureInfo;

    }

    /**
     * 计算图片目标压缩大小
     *
     * @param originalSize
     * @return
     */
    private static int calculateSize(int originalSize) {
        final int MIN_SIZE = 800;
        final int MAX_SIZE = 1200;

        if (originalSize < MIN_SIZE) {
            return MIN_SIZE;
        } else if (originalSize > MAX_SIZE) {
            return MAX_SIZE;
        } else {
            return originalSize;
        }
    }

    public static void saveBitmapToFile(Bitmap bitmap, String fileBgName, float rote) throws IOException {

            if (bitmap != null) {
                File f = new File(fileBgName);
                f.createNewFile();
                FileOutputStream fOut = null;
                fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (rote * 100), fOut);
                fOut.flush();
                fOut.close();
            }

    }


}
