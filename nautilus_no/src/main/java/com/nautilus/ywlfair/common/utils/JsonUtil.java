package com.nautilus.ywlfair.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * JSON转换工具类
 *
 * @param <T>
 */
public class JsonUtil<T> {

    public List<T> json2List(String jsonStr, String className) {
        List<T> list = new ArrayList<T>();
        if (jsonStr != null && !"".equals(jsonStr)) {
            try {
                JSONArray jarry = new JSONArray(jsonStr);
                int length = jarry.length();
                for (int i = 0; i < length; i++) {
                    list.add(json2Bean(jarry.getString(i), className));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public T json2Bean(String jsonString, String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (TextUtils.isEmpty(jsonString)) {
                return (T) clazz.newInstance();
            } else {
                jsonString = jsonString.replaceAll(", null", "");
                GsonBuilder builder = new GsonBuilder();
//                builder.registerTypeAdapter(Uri.class, new UriDeserializer());
                Gson gson = builder.create();
                T t = (T) gson.fromJson(jsonString, clazz);
                return t;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String bean2Json(T t) {
        return new Gson().toJson(t);
    }

    public String list2Json(List<T> t) {
        return new Gson().toJson(t);
    }

//    public class UriDeserializer implements JsonDeserializer<Uri> {
//        @Override
//        public Uri deserialize(final JsonElement src, final Type srcType,
//                               final JsonDeserializationContext context) throws JsonParseException {
//            return Uri.parse(src.getAsString());
//        }
//    }
}
