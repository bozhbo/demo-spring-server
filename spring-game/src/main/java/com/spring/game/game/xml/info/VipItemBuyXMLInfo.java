package com.snail.webgame.game.xml.info;

import java.util.HashMap;
import java.util.Map;

public class VipItemBuyXMLInfo {
	private int no;
	private String itemId;
	
	/**
	 * key：购买次数 val：每一次购买价格
	 */
	private Map<Integer,Integer> priceMap = new HashMap<Integer,Integer>();
	
	/**
	 * key：vipLv val:购买次数上限
	 */
	private Map<Integer,Integer> numMap = new HashMap<Integer,Integer>();

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Map<Integer, Integer> getPriceMap() {
		return priceMap;
	}

	public void setPriceMap(Map<Integer, Integer> priceMap) {
		this.priceMap = priceMap;
	}

	public Map<Integer, Integer> getNumMap() {
		return numMap;
	}

	public void setNumMap(Map<Integer, Integer> numMap) {
		this.numMap = numMap;
	}
	
	public int fetchBuyMaxNumByVipLv(int vipLv) {
		return numMap.get(vipLv);
	}
	
	public Integer fetchBuyPriceByNum(int num) {
		return priceMap.get(num);
	}
	
}
