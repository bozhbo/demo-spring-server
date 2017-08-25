package com.snail.webgame.game.xml.info;

import java.util.HashMap;

public class EquipStrengthenConfigInfo {
	private int quality; // 装备颜色 对象Equip.xml
	private HashMap<Integer, EquipXMLUpgrade> equipXMLUpgradeMap = new HashMap<Integer, EquipXMLUpgrade>();

	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public HashMap<Integer, EquipXMLUpgrade> getEquipXMLUpgradeMap() {
		return equipXMLUpgradeMap;
	}

	public void setEquipXMLUpgradeMap(HashMap<Integer, EquipXMLUpgrade> equipXMLUpgradeMap) {
		this.equipXMLUpgradeMap = equipXMLUpgradeMap;
	}

}
