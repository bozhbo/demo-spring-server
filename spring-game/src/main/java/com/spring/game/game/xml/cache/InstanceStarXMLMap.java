package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snail.webgame.game.xml.info.InstanceStarXMLInfo;

public class InstanceStarXMLMap {
	private static HashMap<Integer, InstanceStarXMLInfo> map = new HashMap<Integer, InstanceStarXMLInfo>();


	public static void addInstance(int no, InstanceStarXMLInfo instanceXmlInfo,boolean modify) {
		if (map.containsKey(no)&& !modify) {
			throw new RuntimeException("Load InstanceStar.xml error! Type: " + no + " repeat");
		}
		map.put(no, instanceXmlInfo);
	}

	public static InstanceStarXMLInfo getInstanceStar(int no) {
		return map.get(no);
	}

	public static List<InstanceStarXMLInfo> getInstanceStarType(int type) {
		List<InstanceStarXMLInfo> list = new ArrayList<InstanceStarXMLInfo>();
		if(map != null && map.size() > 0){
			for(InstanceStarXMLInfo xmlInfo : map.values()){
				if(xmlInfo.getType() == type){
					list.add(xmlInfo);
				}
			}
		}
		return list;
	}
	
}
