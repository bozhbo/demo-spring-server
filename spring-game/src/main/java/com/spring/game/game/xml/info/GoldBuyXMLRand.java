package com.snail.webgame.game.xml.info;

import java.util.Map;

public class GoldBuyXMLRand {

	private int no;
	private Map<Float, GoldBuyXMLRandItem> mulMap;

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Map<Float, GoldBuyXMLRandItem> getMulMap() {
		return mulMap;
	}

	public void setMulMap(Map<Float, GoldBuyXMLRandItem> mulMap) {
		this.mulMap = mulMap;
	}
}
