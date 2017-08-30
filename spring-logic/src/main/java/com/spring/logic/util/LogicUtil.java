package com.spring.logic.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LogicUtil {

	private static Gson gson = null;

	public static void initJson() {
		gson = new GsonBuilder().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
	}

	public static <T> T fromJson(String json, Class<T> c) {
		try {
			return gson.fromJson(json, c);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> String tojson(T t) {
		return gson.toJson(t);
	}
}
