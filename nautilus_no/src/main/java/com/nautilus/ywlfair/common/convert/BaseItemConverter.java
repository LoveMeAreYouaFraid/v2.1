package com.nautilus.ywlfair.common.convert;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.nautilus.ywlfair.entity.bean.BaseItem;
import com.nautilus.ywlfair.entity.bean.NautilusItem;

public class BaseItemConverter {

	private static BaseItemConverter mInstance;

	private Context mContext;

	public static BaseItemConverter getInstance(Context context) {
		if (mInstance == null) {
			synchronized (BaseItemConverter.class) {
				if (mInstance == null) {
					mInstance = new BaseItemConverter(context);
				}
			}
		}

		return mInstance;
	}

	private BaseItemConverter(Context context) {
		mContext = context;
	}

	public BaseItem convert(String jsonString) {
		
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		BaseItem baseItem = new BaseItem();

		try {
			JSONObject root = new JSONObject(jsonString);
			int status = root.getInt(BaseItem.BASEITEM_KEY_STATUS);
			baseItem.setStatus(status);
			baseItem.setMessage(root
					.getString(NautilusItem.BASEITEM_KEY_MESSAGE));
			baseItem
					.setUptime(root.getLong(NautilusItem.BASEITEM_KEY_TIME));

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return baseItem;
	}


}
