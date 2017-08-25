package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.xml.info.EquipXMLInfo;
import com.snail.webgame.game.xml.info.PropXMLInfo;

public class PropXMLInfoMap {

	// <no,PropXMLInfo>
	private static HashMap<Integer, PropXMLInfo> map = new HashMap<Integer, PropXMLInfo>();

	// <no,<chipNo,chipNum>>
	private static HashMap<Integer, HashMap<Integer, Integer>> composeMap = new HashMap<Integer, HashMap<Integer, Integer>>();

	// 时装碎片对应时装<chipNo,equipNo(时装编号)>
	private static HashMap<Integer, Integer> szChipMap = new HashMap<Integer, Integer>();

	public static void addPropXMLInfo(PropXMLInfo info) {
		map.put(info.getNo(), info);
	}

	public static PropXMLInfo getPropXMLInfo(int no) {
		return map.get(no);
	}

	public static HashMap<Integer, PropXMLInfo> getMap() {
		return map;
	}

	public static void addComposeXMLInfo(int itemNo, int chipNo, int chipNum) {
		HashMap<Integer, Integer> chipMap = composeMap.get(itemNo);
		if (chipMap == null) {
			chipMap = new HashMap<Integer, Integer>();
			composeMap.put(itemNo, chipMap);
		}
		Integer oldValue = chipMap.get(chipNo);
		if (oldValue == null) {
			chipMap.put(chipNo, chipNum);
		} else {
			chipMap.put(chipNo, chipNum + oldValue);
		}

		if (String.valueOf(itemNo).startsWith(GameValue.EQUIP_N0)) {
			EquipXMLInfo xmlInfo = EquipXMLInfoMap.getEquipXMLInfo(itemNo);
			if (xmlInfo != null && (xmlInfo.getEquipType() == 9 || xmlInfo.getEquipType() == 10)) {
				// 时装碎片对应时装
				szChipMap.put(chipNo, itemNo);
			}
		}
	}

	public static HashMap<Integer, Integer> getComposeXMLInfo(int itemNo) {
		return composeMap.get(itemNo);
	}
	
	public static Integer getSZItemNo(int chipNo){
		return szChipMap.get(chipNo);
	}
}
