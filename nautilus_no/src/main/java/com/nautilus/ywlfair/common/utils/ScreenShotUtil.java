package com.nautilus.ywlfair.common.utils;

import java.sql.Date;

import com.nautilus.ywlfair.common.MyApplication;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

public class ScreenShotUtil {
	public static String getScreenShot(View view){
		 String fileName = TimeUtil.convertDateTime2DateStr(new Date(System.currentTimeMillis()));
		 
		 String filePath = FileUtils.getDiskCacheFile(fileName).getAbsolutePath();
		 
		 view.setDrawingCacheEnabled(true);
		 
		 view.buildDrawingCache();
		 
		 Bitmap bitmap = view.getDrawingCache();
		 
		 if(bitmap != null){
			 FileUtils.saveBitmapToFile(bitmap, filePath, 0.8f);
		 }else{
			 Toast.makeText(MyApplication.getInstance(), "截屏出现未知错误", Toast.LENGTH_SHORT).show();
			 
			 return null;
		 }
		return filePath;
	}
	
}
