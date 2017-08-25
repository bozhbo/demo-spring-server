package com.snail.webgame.game.xml.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.xml.info.EnchantXMLInfo;
import com.snail.webgame.game.xml.info.EnchantXMLUpgrade;
import com.snail.webgame.game.xml.info.EquipEffectConfigInfo;
import com.snail.webgame.game.xml.info.EquipStrengthenConfigInfo;
import com.snail.webgame.game.xml.info.EquipSuitConfigInfo;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

public class EquipXMLInfoMap {

	// <no,EquipXMLInfo>
	private static HashMap<Integer, EquipXMLInfo> equipMap = new HashMap<Integer, EquipXMLInfo>();

	// 主武将等级
	private static HashMap<Integer, EquipStrengthenConfigInfo> upMap = new HashMap<Integer, EquipStrengthenConfigInfo>();

	private static Map<Integer, Map<Integer, EquipEffectConfigInfo>> effectMap = new HashMap<Integer, Map<Integer, EquipEffectConfigInfo>>();

	private static Map<Integer, List<EquipSuitConfigInfo>> suitMap = new HashMap<Integer, List<EquipSuitConfigInfo>>();

	// 装备附魔消耗
	private static Map<Integer, EnchantXMLInfo> enchantMap = new HashMap<Integer, EnchantXMLInfo>();

	// KEY 时装类型 value 时装个数
	private static Map<Integer, List<EquipXMLInfo>> allShizhuangMap = new HashMap<Integer, List<EquipXMLInfo>>();

	public static List<EquipXMLInfo> getAddShizhuangEquip(Integer num) {
		return allShizhuangMap.get(num);
	}

	public static void addShizhuang(EquipXMLInfo xmlInfo) {
		List<EquipXMLInfo> allByType = allShizhuangMap.get(xmlInfo.getShizhuangType());
		if (allByType == null) {
			allShizhuangMap.put(xmlInfo.getShizhuangType(), allByType = new ArrayList<EquipXMLInfo>());
		}
		allByType.add(xmlInfo);
	}

	public static void addEquipXMLInfo(EquipXMLInfo xmlInfo) {
		equipMap.put(xmlInfo.getNo(), xmlInfo);
	}

	public static void addEquipStrengthenConfigInfo(EquipStrengthenConfigInfo xmlInfo) {
		upMap.put(xmlInfo.getQuality(), xmlInfo);
	}

	public static EquipXMLInfo getEquipXMLInfo(int equipNo) {
		return equipMap.get(equipNo);
	}

	public static EquipStrengthenConfigInfo getEquipStrengthenConfigInfo(int quality) {
		return upMap.get(quality);
	}

	public static HashMap<Integer, EquipXMLInfo> getEquipMap() {
		return equipMap;
	}

	public static HashMap<Integer, EquipStrengthenConfigInfo> getEquipUpMap() {
		return upMap;
	}

	public static void addEquipEffectConfigInfo(int character, EquipEffectConfigInfo xmlInfo) {
		if (effectMap.get(character) == null) {
			Map<Integer, EquipEffectConfigInfo> map = new HashMap<Integer, EquipEffectConfigInfo>();
			effectMap.put(character, map);
		}

		effectMap.get(character).put(xmlInfo.getType(), xmlInfo);
	}

	public static Map<Integer, EquipEffectConfigInfo> getEquipEffectConfigInfoMap(int character) {
		return effectMap.get(character);
	}

	public static void addEquipSuitConfigInfo(int no, EquipSuitConfigInfo info) {
		List<EquipSuitConfigInfo> list = suitMap.get(no);
		if (list == null) {
			list = new ArrayList<EquipSuitConfigInfo>();
			suitMap.put(no, list);
		}
		list.add(info);
	}

	public static List<EquipSuitConfigInfo> getEquipSuitConfigInfo(int no) {
		return suitMap.get(no);
	}

	public static Map<Integer, EnchantXMLInfo> getEnchantMap() {
		return enchantMap;
	}

	public static void addEnchantXMLInfo(EnchantXMLInfo xmlInfo) {
		enchantMap.put(xmlInfo.getQuailty(), xmlInfo);
	}

	public static EnchantXMLInfo getEnchantXMLInfo(int quality) {
		return enchantMap.get(quality);
	}

	public static int getEnChantExp(int equipQuality, int lv, int exp) {
		int result = exp;
		EnchantXMLInfo xmlInfo = enchantMap.get(equipQuality);
		if (xmlInfo != null && lv > 0) {
			EnchantXMLUpgrade upgrade = null;
			for (int i = 1; i <= lv; i++) {
				upgrade = xmlInfo.getEnchantXMLUpgrade(i);
				if (upgrade != null) {
					result += upgrade.getEnchantNum();
				}
			}
		}
		return result;
	}
}
