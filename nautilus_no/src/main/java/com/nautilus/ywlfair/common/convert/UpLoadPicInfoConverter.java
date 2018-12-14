package com.nautilus.ywlfair.common.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.nautilus.ywlfair.entity.bean.BaseItem;
import com.nautilus.ywlfair.entity.bean.NautilusItem;
import com.nautilus.ywlfair.entity.bean.UpLoadPicInfo;

public class UpLoadPicInfoConverter implements ContentConverter<UpLoadPicInfo> {

	private static UpLoadPicInfoConverter mInstance;

	private Context mContext;

	public static UpLoadPicInfoConverter getInstance(Context context) {
		if (mInstance == null) {
			synchronized (UpLoadPicInfoConverter.class) {
				if (mInstance == null) {
					mInstance = new UpLoadPicInfoConverter(context);
				}
			}
		}

		return mInstance;
	}

	private UpLoadPicInfoConverter(Context context) {
		mContext = context;
	}

	@Override
	public List<UpLoadPicInfo> convert(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		List<UpLoadPicInfo> nautilusItemList = new ArrayList<UpLoadPicInfo>();

		UpLoadPicInfo nautilusItem = new UpLoadPicInfo();

		try {
			JSONObject root = new JSONObject(jsonString);
			int status = root.getInt(BaseItem.BASEITEM_KEY_STATUS);
//			nautilusItem.setStatus(status);
//			nautilusItem.setMessage(root
//					.getString(NautilusItem.BASEITEM_KEY_MESSAGE));
//			nautilusItem
//					.setUptime(root.getLong(NautilusItem.BASEITEM_KEY_TIME));

			if (status == 0) {
				JSONObject resultJsonObject = root
						.getJSONObject(BaseItem.BASEITEM_KEY_RESULT);
				if (resultJsonObject != null) {

					nautilusItem = UpLoadPicInfo
							.createActiveRecord(resultJsonObject);

					nautilusItemList.add(nautilusItem);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nautilusItemList.size() > 0 ? nautilusItemList : null;
	}

	@Override
	public boolean updateDatabase(List<UpLoadPicInfo> itemList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UpLoadPicInfo convertItem(String jsonString) {
		
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		UpLoadPicInfo nautilusItem = new UpLoadPicInfo();

		try {
			JSONObject root = new JSONObject(jsonString);
			int status = root.getInt(BaseItem.BASEITEM_KEY_STATUS);
//			nautilusItem.setStatus(status);
//			nautilusItem.setMessage(root
//					.getString(NautilusItem.BASEITEM_KEY_MESSAGE));
//			nautilusItem
//					.setUptime(root.getLong(NautilusItem.BASEITEM_KEY_TIME));

			if (status == 0) {
				JSONObject resultJsonObject = root
						.getJSONObject(BaseItem.BASEITEM_KEY_RESULT);
				if (resultJsonObject != null) {

					nautilusItem = UpLoadPicInfo
							.createActiveRecord(resultJsonObject);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nautilusItem;
	}

}
