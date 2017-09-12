package com.letion.geetionlib.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liu-feng
 * @version 1.0
 */
public class TJSON {

	private static Gson gson = null;

	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private TJSON() {

	}

	/**
	 * 将对象转换成json格式
	 * 
	 * @param ts
	 * @return
	 */
	public static String objectToJson(Object ts) {
		String jsonStr = null;
		if (gson != null) {
			jsonStr = gson.toJson(ts);
		}
		return jsonStr;
	}

	/**
	 * 将json格式转换成List对象
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static <T extends Object> ArrayList<T> parseModelList(String jsonStr, com.google.gson.reflect.TypeToken<ArrayList<T>> type) {
		ArrayList<T> objList = null;
		if (gson != null) {
			objList = gson.fromJson(jsonStr, type.getType());
		}
		return objList;
	}

	/**
	 * 将json格式转换成List对象，并准确指定类型
	 * 
	 * @param jsonStr
	 * @param type
	 * @return
	 */
	public static List<?> parseModel(String jsonStr, java.lang.reflect.Type type) {
		List<?> objList = null;
		if (gson != null) {
			objList = gson.fromJson(jsonStr, type);
		}
		return objList;
	}

	/**
	 * 将json格式转换成map对象
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<?, ?> jsonToMap(String jsonStr) {
		Map<?, ?> objMap = null;
		if (gson != null) {
			java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
			}.getType();
			objMap = gson.fromJson(jsonStr, type);
		}
		return objMap;
	}

	/**
	 * 将json转换成bean对象
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static <T> T parseModel(String jsonStr, Class<T> cl) {
		T obj = null;
		if (gson != null) {
			obj = gson.fromJson(jsonStr, cl);
		}
		return obj;
	}

}