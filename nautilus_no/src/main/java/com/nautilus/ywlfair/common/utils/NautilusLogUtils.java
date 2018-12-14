package com.nautilus.ywlfair.common.utils;

import android.util.Log;

public class NautilusLogUtils {
	private static final String TAG = "Nautilus_Log";
	private static final boolean LOG_ENABLE = true;
	
	public static void Log(String message) {
		if (LOG_ENABLE) {
			Log.d(TAG, message);
		}
	}
}
