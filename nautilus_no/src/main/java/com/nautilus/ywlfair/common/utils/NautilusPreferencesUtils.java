package com.nautilus.ywlfair.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NautilusPreferencesUtils {

	private static NautilusPreferencesUtils mInstance = null;

	private Context mContext = null;
	private SharedPreferences mSharedPreferences = null;

	public static NautilusPreferencesUtils getInstance(Context context) {
		if (mInstance == null) {
			synchronized (NautilusPreferencesUtils.class) {
				if (mInstance == null) {
					mInstance = new NautilusPreferencesUtils(context);
				}
			}
		}

		return mInstance;
	}

	private NautilusPreferencesUtils(Context context) {
		mContext = context;

		if (mSharedPreferences == null) {
			mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		}
	}
}
