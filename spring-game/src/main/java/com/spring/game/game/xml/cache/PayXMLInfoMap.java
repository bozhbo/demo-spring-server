package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.PayXMLInfo;

public class PayXMLInfoMap {
	
	private static HashMap<Integer, PayXMLInfo> map = new HashMap<Integer, PayXMLInfo>();
	private static HashMap<String, PayXMLInfo> pidMap = new HashMap<String, PayXMLInfo>();// key - pid 

	public static void addPayXMLInfo(PayXMLInfo xmlInfo) {
		map.put(xmlInfo.getNo(), xmlInfo);
		
		pidMap.put(xmlInfo.getPid(), xmlInfo);
	}

	public static PayXMLInfo fetchPayXMLInfo(int no) {
		return map.get(no);
	}
	
	public static PayXMLInfo getPidPayXMLInfo(String pid) {
		return pidMap.get(pid);
	}
	
	public static HashMap<Integer, PayXMLInfo> getMap()
	{
		return map;
	}
}
