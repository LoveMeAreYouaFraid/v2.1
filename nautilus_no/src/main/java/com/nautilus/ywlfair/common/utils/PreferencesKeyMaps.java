package com.nautilus.ywlfair.common.utils;

public enum PreferencesKeyMaps {
	HOMEPAPER_JSON_KEY("homepaper_json"),
	ACCESSTOKEN_KEY("accessToken");
	
	private String preferencesKey;

	private PreferencesKeyMaps(String key) {
		preferencesKey = key;
	}
	
	public String getPreferencesKey() {
		return preferencesKey;
	}
}
