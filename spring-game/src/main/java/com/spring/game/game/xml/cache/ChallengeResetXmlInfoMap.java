package com.snail.webgame.game.xml.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.xml.info.ChallengeResetXMLInfo;


public class ChallengeResetXmlInfoMap {

	//<关卡类型，<No,ChallengeResetXMLInfo>>
	private static Map<Byte, Map<Integer, ChallengeResetXMLInfo>> map = new HashMap<Byte, Map<Integer, ChallengeResetXMLInfo>>();

	public static Map<Byte, Map<Integer, ChallengeResetXMLInfo>> getChallengeRest() {
		return map;
	}

	/**
	 * 添加
	 * @param no
	 * @param info
	 */
	public static void addChallengeRest(byte no, Map<Integer, ChallengeResetXMLInfo> resetMap) {
		if(no != 0 && resetMap != null && resetMap.size() > 0){
			map.put(no, resetMap);
		}
	}

	/**
	 * 获取
	 * @param no
	 * @param times
	 * @return
	 */
	public static ChallengeResetXMLInfo getInfoByNo(byte type, int times) {
		Map<Integer, ChallengeResetXMLInfo> xmlMap = map.get(type);
		if(xmlMap != null && xmlMap.size() > 0){
			if(xmlMap.containsKey(times)){
				return xmlMap.get(times);
			}
		}
		return null;
	}

}
