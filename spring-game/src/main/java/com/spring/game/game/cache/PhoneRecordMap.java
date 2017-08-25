package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.info.PhoneRecordInfo;

public class PhoneRecordMap {

	private static Map<String, PhoneRecordInfo> map = new HashMap<String, PhoneRecordInfo>();
	
	public static void addPhoneRecordInfo(PhoneRecordInfo info) {
		if (!map.containsKey(info.getAccount())) {
			map.put(info.getAccount(), info);
		}
	}
	
	public static boolean isPhoneLink(String acc) {
		if (map.containsKey(acc)) {
			return true;
		}
		
		return false;
	}
}
