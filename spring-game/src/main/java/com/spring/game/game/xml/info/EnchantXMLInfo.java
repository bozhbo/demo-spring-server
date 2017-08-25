package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

public class EnchantXMLInfo {

	private int quailty;

	private int maxLv;
	private Map<Integer, EnchantXMLUpgrade> lvMap = new HashMap<Integer, EnchantXMLUpgrade>();

	public int getQuailty() {
		return quailty;
	}

	public void setQuailty(int quailty) {
		this.quailty = quailty;
	}

	public int getMaxLv() {
		return maxLv;
	}

	public Map<Integer, EnchantXMLUpgrade> getLvMap() {
		return lvMap;
	}
	
	public EnchantXMLUpgrade getEnchantXMLUpgrade(int lv){
		return lvMap.get(lv);
	}

	public void addEnchantXMLUpgrade(EnchantXMLUpgrade xmlInfo) {
		if (xmlInfo.getNo() > maxLv) {
			maxLv = xmlInfo.getNo();
		}
		lvMap.put(xmlInfo.getNo(), xmlInfo);
	}
}
