package com.spring.logic.message.request.room;

import java.util.HashMap;
import java.util.Map;

import com.spring.logic.util.LogicUtil;

public class RoomOperateJsonRes {

	private Map<String, Object> map = new HashMap<>();
	
	public static RoomOperateJsonRes instance() {
		return new RoomOperateJsonRes();
	}
	
	public RoomOperateJsonRes addIntValue(String key, int value) {
		map.put(key, value);
		return this;
	}
	
	public RoomOperateJsonRes addStringValue(String key, String value) {
		map.put(key, value);
		return this;
	}
	
	@Override
	public String toString() {
		if (map.size() == 0) {
			return "";
		}
		
		return LogicUtil.tojson(map);
	}
}
