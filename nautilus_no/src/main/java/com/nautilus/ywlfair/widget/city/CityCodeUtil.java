package com.nautilus.ywlfair.widget.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

/**
 * 
 * 城市代码
 * 
 * @author zd
 * 
 */
public class CityCodeUtil {

	private ArrayList<String> province_list = new ArrayList<String>();
	private ArrayList<String> city_list = new ArrayList<String>();
	public ArrayList<String> province_list_code = new ArrayList<String>();
	public ArrayList<String> city_list_code = new ArrayList<String>();
	/** 单例 */
	public static CityCodeUtil model;
	private Context context;

	private CityCodeUtil() {
	}

	public ArrayList<String> getProvince_list_code() {
		return province_list_code;
	}

	public ArrayList<String> getCity_list_code() {
		return city_list_code;
	}

	public void setCity_list_code(ArrayList<String> city_list_code) {
		this.city_list_code = city_list_code;
	}

	public void setProvince_list_code(ArrayList<String> province_list_code) {

		this.province_list_code = province_list_code;
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static CityCodeUtil getSingleton() {
		if (null == model) {
			model = new CityCodeUtil();
		}
		return model;
	}

	public ArrayList<String> getProvince(List<CityInfo> province) {
		if (province_list_code.size() > 0) {
			province_list_code.clear();
		}
		if (province_list.size() > 0) {
			province_list.clear();
		}
		for (int i = 0; i < province.size(); i++) {
			province_list.add(province.get(i).getCity_name());
			province_list_code.add(province.get(i).getId());
		}
		return province_list;

	}

	public ArrayList<String> getCity(
			HashMap<String, List<CityInfo>> cityHashMap, String provinceCode) {
		if (city_list_code.size() > 0) {
			city_list_code.clear();
		}
		if (city_list.size() > 0) {
			city_list.clear();
		}
		List<CityInfo> city = cityHashMap.get(provinceCode);

		for (int i = 0; i < city.size(); i++) {
			city_list.add(city.get(i).getCity_name());
			city_list_code.add(city.get(i).getId());
		}
		return city_list;

	}

}
