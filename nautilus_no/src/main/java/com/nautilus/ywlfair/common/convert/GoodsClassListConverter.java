package com.nautilus.ywlfair.common.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.nautilus.ywlfair.entity.bean.BaseItem;
import com.nautilus.ywlfair.entity.bean.GoodsClassInfo;
import com.nautilus.ywlfair.common.utils.JsonUtil;

public class GoodsClassListConverter implements ContentConverter<GoodsClassInfo> {
	
	private static GoodsClassListConverter mInstance;

	private Context mContext;
	
	public static GoodsClassListConverter getInstance(Context context) {
		if (mInstance == null) {
			synchronized (GoodsClassListConverter.class) {
				if (mInstance == null) {
					mInstance = new GoodsClassListConverter(context);
				}
			}
		}

		return mInstance;
	}

	private GoodsClassListConverter(Context context) {
		mContext = context;
	}

	@Override
	public List<GoodsClassInfo> convert(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		
		List<GoodsClassInfo> nautilusItemList = new ArrayList<GoodsClassInfo>();
		
		try {
			JSONObject root = new JSONObject(jsonString);
			int status = root.getInt(BaseItem.BASEITEM_KEY_STATUS);

			if (status == 0) {
				JSONObject resultJsonObject = root.getJSONObject(BaseItem.BASEITEM_KEY_RESULT);
				if (resultJsonObject != null) {
					JSONArray activityInfoListJsonArray = resultJsonObject.getJSONArray("goodsClassInfoList");
					if (activityInfoListJsonArray != null) {
						JsonUtil<GoodsClassInfo> jsonUtil = new JsonUtil<GoodsClassInfo>();
						
						nautilusItemList = jsonUtil.json2List(activityInfoListJsonArray.toString(), GoodsClassInfo.class.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nautilusItemList;
	}

	@Override
	public boolean updateDatabase(List<GoodsClassInfo> itemList) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GoodsClassInfo convertItem(String jsonString) {
		// TODO Auto-generated method stub
		return null;
	}

}
