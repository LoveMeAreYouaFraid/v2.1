package com.nautilus.ywlfair.common.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.nautilus.ywlfair.common.MyApplication;

/**
 * Copyright (©) 2015 Shanghai Duxue Networking Technology Co., Ltd
 * <p/>
 * TODO:
 * 
 * @author jiangdongxiang
 * @version 1.0, 2015/3/23 12:06
 * @since 2015/3/23
 */
public class FileUtils {

	/**
	 * 返回应用的缓存目录
	 * 
	 * @return
	 */
	public static File getDiskCacheDir() {

		File cacheDir;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cacheDir = MyApplication.getInstance().getExternalCacheDir();
		} else {
			cacheDir = MyApplication.getInstance().getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}

		return cacheDir;
	}

	/**
	 * 返回应用的缓存目录中的指定名称子目录（如不存在则自动创建）
	 * 
	 * @param directoryName
	 * @return
	 */
	public static File getDiskCacheDir(String directoryName) {

		File cacheDir;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cacheDir = MyApplication.getInstance().getExternalCacheDir();
		} else {
			cacheDir = MyApplication.getInstance().getCacheDir();
		}

		File dirFile = new File(cacheDir + File.separator + directoryName);

		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		return dirFile;
	}

	/**
	 * 返回应用的缓存目录中的指定名称文件
	 * 
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskCacheFile(String uniqueName) {

		File cacheDir;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cacheDir = MyApplication.getInstance().getExternalCacheDir();
		} else {
			cacheDir = MyApplication.getInstance().getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}

		return new File(cacheDir.getPath() + File.separator + uniqueName);
	}

	/**
	 * 返回应用的缓存目录的子目录中的指定名称文件
	 * 
	 * @param directoryName
	 * @param uniqueName
	 * @return
	 */
	public static File getDiskCacheFile(String directoryName, String uniqueName) {

		File cacheDir;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cacheDir = MyApplication.getInstance().getExternalCacheDir();
		} else {
			cacheDir = MyApplication.getInstance().getCacheDir();
		}

		File dirFile = new File(cacheDir + File.separator + directoryName);

		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		return new File(dirFile.getPath() + File.separator + uniqueName);
	}

	public static File getDiskDownloadsDir(String uniqueName) {
		File dir;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			dir = MyApplication.getInstance().getExternalFilesDir(
					Environment.DIRECTORY_DOWNLOADS);
		} else {
			dir = MyApplication.getInstance().getExternalFilesDir(
					Environment.DIRECTORY_DOWNLOADS);
		}

		if (!dir.exists()) {
			dir.mkdir();
		}

		return new File(dir.getPath() + File.separator + uniqueName);
	}

	public static void saveBitmapToFile(Bitmap bitmap, String fileBgName,
			float rote) {
		try {
			if (bitmap != null) {
				File f = new File(fileBgName);
				f.createNewFile();
				FileOutputStream fOut = null;
				fOut = new FileOutputStream(f);
				bitmap.compress(Bitmap.CompressFormat.JPEG, (int) (rote * 100),
						fOut);
				fOut.flush();
				fOut.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String uri2Path(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = MyApplication.getInstance().getContentResolver()
				.query(uri, projection, null, null, null);
		if (cursor != null) {
			int actual_image_column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String img_path = cursor.getString(actual_image_column_index);
			cursor.close();
			return img_path;
		} else {
			File file = new File(uri.getPath());
			if (file.exists()) {
				return file.getAbsolutePath();
			}
		}

		return null;
	}

	/**
	 * 清空指定目录中的文件
	 */
	public static void clearDir(File directoryFile) {
		if (directoryFile.isDirectory()) {
			File[] files = directoryFile.listFiles();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
		}
	}

}
