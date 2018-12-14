package com.nautilus.ywlfair.common.convert;

import java.util.List;

public interface ContentConverter<T> {
	public List<T> convert(String jsonString);
	public boolean updateDatabase(List<T> itemList);
	public T convertItem(String jsonString);
}
