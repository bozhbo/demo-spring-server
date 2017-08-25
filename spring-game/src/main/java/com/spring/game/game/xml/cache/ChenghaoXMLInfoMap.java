package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.ChenghaoXMLInfo;

public class ChenghaoXMLInfoMap {
	
	//KEY :no  VALUE info
	private static Map<Integer, ChenghaoXMLInfo> noMap = new HashMap<Integer, ChenghaoXMLInfo>();
	//KEY :type  KEY: num  VALUE info
	private static Map<String,Map<Integer, ChenghaoXMLInfo>> typeMap = new HashMap<String,Map<Integer, ChenghaoXMLInfo>>();

	public static void addChenghaoXMLInfo(ChenghaoXMLInfo xmlInfo) {
		noMap.put(xmlInfo.getNo(), xmlInfo);
		
		Map<Integer, ChenghaoXMLInfo> map = typeMap.get(xmlInfo.getType());
		if(map == null){
			typeMap.put(xmlInfo.getType(), map = new HashMap<Integer, ChenghaoXMLInfo>());
		}
		map.put(xmlInfo.getNum(), xmlInfo);
	}

	public static ChenghaoXMLInfo getChenghaoXMLInfoByNo(int no) {
		return noMap.get(no);
	}

	public static ChenghaoXMLInfo getChenghaoXMLInfoByTypeAndNum(String type, int num) {
		Map<Integer, ChenghaoXMLInfo> m = typeMap.get(type);
		if(m == null){
			return null;
		}else{
			return m.get(num);
		}
		
	}
	
	public static Map<Integer, ChenghaoXMLInfo> getChenghaoXMLInfoMapByType(String type) {
		return typeMap.get(type);
	}
}
