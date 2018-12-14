package com.nautilus.ywlfair.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.ContactsContract.Directory;

import com.nautilus.ywlfair.R;

public class CbFileUtils {
	private String SDCardRoot;
	public static final String ACTION_SDCARD_UNMOUNT = "com.lenovo.notes.action.SDCARD_UNMOUNT";
	public static final String INTERNAL_STORAGE = File.separator + "storage" + File.separator + "sdcard0";
	private static boolean hasExternalSDcard = false;

	public CbFileUtils(Context context) {
		if (!SDCardStateOK()) {
			Intent intent = new Intent(ACTION_SDCARD_UNMOUNT);
			context.sendBroadcast(intent);
		}
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	public CbFileUtils() {
		if (!SDCardStateOK()) {
			return;
		}
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	public File createFileInSDCard(String fileName, String dir) {
		if (SDCardStateOK()) {
			createSDDir(dir);
			File file = new File(SDCardRoot + dir + File.separator + fileName);
			return file;
		} else {
			return null;
		}
	}

	public File createSDDir(String dir) {
		if (SDCardStateOK()) {
			File f = new File(SDCardRoot + dir + File.separator);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					return null;
				}
			}
			return f;
		} else {
			return null;
		}
	}

	public boolean isFileExist(String fileName, String path) throws IOException {
		if (SDCardStateOK()) {
			createSDDir(path);
			File file = createFileInSDCard(fileName, path);
			return file.exists();
		} else {
			return false;
		}
	}

	public File write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		if (SDCardStateOK()) {
			try {
				createSDDir(path);
				file = createFileInSDCard(fileName, path);
				output = new FileOutputStream(file);
				byte buffer[] = new byte[4 * 1024];
				int temp;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
				}
				output.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return file;
		} else {
			return null;
		}
	}

	public boolean deleteSDDirFile(String path) {
		if (SDCardStateOK()) {
			File file = new File(SDCardRoot + File.separator + path);
			if (!file.exists()) {
				return false;
			}
			if (!file.isDirectory()) {
				return false;
			}
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(SDCardRoot + File.separator + path + tempList[i]);
				} else {
					temp = new File(SDCardRoot + File.separator + path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					deleteSDDirFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
					deleteSDDirFile(path + "/" + tempList[i]);// 再删除空文件夹
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static boolean deleteDirFile(String path) {
		NautilusLogUtils.Log("path: " + path);
		if (SDCardStateOK()) {
			File file = new File(path);
			if (!file.exists()) {
				return false;
			}
			if (!file.isDirectory()) {
				return false;
			}
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (path.endsWith(File.separator)) {
					temp = new File(path + tempList[i]);
				} else {
					temp = new File(path + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					deleteDirFile(temp.getAbsolutePath());
					temp.delete();
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * */
	public boolean createSDCachedFolder(String path) {
		File f = null;
		try {
			if (SDCardStateOK()) {
				f = new File(SDCardRoot + File.separator + path);
				if (f.exists()) {
					deleteSDDirFile(path);
					f.delete();
				}
				f.mkdirs();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @return integer: -1 SD card is unavailable; 0 SD card is full; 1 all are
	 *         OK;
	 */
	@SuppressWarnings("deprecation")
	public int isSDCardFull() {
		if (SDCardStateOK()) {
			// SDCardRoot;
			// File mSDCardDirectory =
			// Environment.getExternalStorageDirectory();
			NautilusLogUtils.Log("SDCardRoot:" + SDCardRoot);
			StatFs fs = new StatFs(SDCardRoot);
			long available = fs.getAvailableBlocks() * (fs.getBlockSize() / 1024);/* getBlockSize返回Kbit大小值 */
			NautilusLogUtils.Log("fs.getAvailableBlocks():" + fs.getAvailableBlocks() + "-- fs.getBlockSize():" + fs.getBlockSize() + "--available:" + available);

			if (available < 1024 && available >= 0) { // /小于1M
				return 0;
			} else if (available < 0) {
				return -1;
			}
			return 1;
			// return (fs.getAvailableBlocks() > 1 ? true : false);
		} else {
			return -1;
		}
	}

	/**
	 * 
	 * @param fromFile
	 * @param toFile
	 * @return 0 :if is OK; -1: if is not OK;
	 */
	public int CopyFile(String fromFile, String toFile) {
		try {
			if (SDCardStateOK()) {
				InputStream fosfrom = new FileInputStream(fromFile);
				OutputStream fosto = new FileOutputStream(toFile);
				byte bt[] = new byte[1024];
				int c;
				while ((c = fosfrom.read(bt)) > 0) {
					fosto.write(bt, 0, c);
				}
				fosfrom.close();
				fosto.close();
				return 0;
			} else {
				return -1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	public int CopyFile(File fromFile, File toFile) {
		try {
			if (!SDCardStateOK()) {
				return 0;
			}
			if (!toFile.exists()) {
				toFile.createNewFile();
			}
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * @param f
	 *            : File
	 * @return length of the file;
	 */
	public static long getFileSize(File f) throws Exception {// 取得文件大小
		long s = 0;
		if (!SDCardStateOK()) {
			return 0;
		}
		if (f.exists()) {
			FileInputStream fis = new FileInputStream(f);
			s = fis.available();
			fis.close();
		} else {
			// f.createNewFile();
			NautilusLogUtils.Log("File not exists!");
			return 0;
		}
		return s;
	}

	/**
	 * 
	 * @param f
	 *            : File
	 * @return size of File {@link Directory}
	 */
	public static long getDirFileSize(File f) throws Exception {
		long size = 0;
		if (!SDCardStateOK()) {
			return 0;
		}
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * @param fileS
	 *            : {@link long} of the file
	 * @return String type of file size: B\K\M\G;
	 * */
	public static String FormatFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		NautilusLogUtils.Log("file size:" + fileS);
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/****
	 * @param f
	 *            : directory of file root;
	 * @return number of the file list ;
	 */
	public static long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		if (!SDCardStateOK()) {
			return 0;
		}
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

	public List<String> listDirFile(String DirString, String extStr) {
		List<String> fileList = new ArrayList<String>();
		File FileDir = createSDDir(DirString);
		if (FileDir.isDirectory()) {
			File[] files = FileDir.listFiles();
			int len = 0;

			if (files != null)
				len = files.length;

			if (len > 0) {
				for (int i = 0; i < len; i++) {
					if (files[i].getName().endsWith(extStr)) {
						fileList.add(files[i].getAbsolutePath());
					}
				}
			}
			// return fileList;
		}
		return fileList;
	}

	public static boolean SDCardStateOK() {
		if (!android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			// System.exit(0);
			NautilusLogUtils.Log("SDCardStateOK: false!");
			return false;
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	public long getAvailaleSize() {
		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	@SuppressWarnings("deprecation")
	public static long getAvailaleSize(String filePath) {
		StatFs stat = new StatFs(filePath);
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList getSDcardList() {
		ArrayList<String> rootList = new ArrayList<String>();
		ArrayList<String> tempList = new ArrayList<String>();
		try {
			Class<?> sStorageManager = Class.forName("android.os.storage.StorageManager");

			// cairz1 start,compatible for the k6_lte row.
			Object[] obj = null;
			Constructor cons = null;
			try {
				cons = sStorageManager.getConstructor(Looper.class);
				obj = new Object[1];
				obj[0] = null;
			} catch (NoSuchMethodException e) {
				// e.printStackTrace();
				cons = sStorageManager.getConstructor(ContentResolver.class, Looper.class);
				obj = new Object[2];
				obj[0] = null;
				obj[1] = null;
			}
			// cairz1 end.

			Object receiver = cons.newInstance(obj);
			Method getVolumeList = sStorageManager.getMethod("getVolumeList", new Class<?>[0]);
			Method getVolumeState = sStorageManager.getMethod("getVolumeState", String.class);
			Object[] volumes = (Object[]) getVolumeList.invoke(receiver, new Object[] {});

			Class sStorageVolume = Class.forName("android.os.storage.StorageVolume");
			Method getPath = sStorageVolume.getMethod("getPath", new Class<?>[0]);
			Method isRemovable = sStorageVolume.getMethod("isRemovable", new Class<?>[0]);

			if (volumes != null) {
				for (int i = 0; i < volumes.length; i++) {
					String path = (String) getPath.invoke(volumes[i], new Object[] {});
					tempList.add(path);
					NautilusLogUtils.Log("Path: " + path);
					NautilusLogUtils.Log("isRemovable: " + isRemovable.invoke(volumes[i], new Object[] {}).toString());
					NautilusLogUtils.Log("getVolumeState: " + getVolumeState.invoke(receiver, tempList.get(i)));
					if ((Boolean) isRemovable.invoke(volumes[i], new Object[] {}) && getVolumeState.invoke(receiver, tempList.get(i)).equals(Environment.MEDIA_MOUNTED)
							&& (path.contains("sdcard") || path.contains("emulated"))) {
						NautilusLogUtils.Log("hasExternalSDcard");
						hasExternalSDcard = true;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}

		if (tempList.size() == 0) {
			rootList.add(Environment.getExternalStorageDirectory().getPath());
		} else {
			for (int i = 0, size = tempList.size(); i < size; i++) {
				if (tempList.get(i).contains("sdcard") || tempList.get(i).contains("emulated")) {
					rootList.add(tempList.get(i));
				}
			}
		}
		NautilusLogUtils.Log("SDCard List: " + rootList);
		return rootList;
	}

	public static boolean hasExternalSDcard() {
		getSDcardList();
		return hasExternalSDcard;
	}

	public static long getFolderSize(String filePath) {
		return getFolderSize(filePath, 0);
	}

	private static long getFolderSize(String filePath, int size) {
		File paramFile = new File(filePath);
		if (!paramFile.isDirectory())
			return paramFile.length();
		File[] tempList = paramFile.listFiles();
		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isDirectory()) {
				size += getFolderSize(tempList[i].getAbsolutePath(), size);
			} else {
				size += tempList[i].length();
			}
		}
		NautilusLogUtils.Log("Folder size: " + size / 1024 / 1024 + "M");
		return size;
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2.0f;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2.0f;
			float clip = (width - height) / 2.0f;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap srcBitmap) {
		if (srcBitmap == null) {
			return null;
		}
		Bitmap maskBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avatar);
		Bitmap result = Bitmap.createBitmap(maskBitmap.getWidth(), maskBitmap.getHeight(), Config.ARGB_8888);

		srcBitmap = Bitmap.createScaledBitmap(srcBitmap, maskBitmap.getWidth(), maskBitmap.getHeight(), false);

		srcBitmap = getScaledBitmap(srcBitmap, maskBitmap.getWidth(), maskBitmap.getHeight());

		Canvas mCanvas = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		mCanvas.drawBitmap(srcBitmap, 0, 0, null);
		mCanvas.drawBitmap(maskBitmap, 0, 0, paint);
		paint.setXfermode(null);

		return result;
	}

	private static Bitmap getScaledBitmap(Bitmap srcBitmap, int newWidth, int newHeight) {
		int width = srcBitmap.getWidth();
		int height = srcBitmap.getHeight();

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);

		return newBitmap;
	}

}
