package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.HeropropConfigInfo;

public class HeropropConfigInfoMap {
	private static Map<Integer, HeropropConfigInfo> map = new HashMap<Integer, HeropropConfigInfo>();
	
	public static void addHeropropConfigInfo(HeropropConfigInfo info){
		map.put(info.getNo(), info);
	}
	
	public static HeropropConfigInfo  getHeropropConfigInfo(int no){
		return map.get(no);
	}
	
}
