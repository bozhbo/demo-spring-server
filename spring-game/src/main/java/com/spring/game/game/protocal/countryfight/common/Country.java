package com.snail.webgame.game.protocal.countryfight.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 国家
 * 
 * @author xiasd
 * 
 */
public class Country {
	private int countryId;
	private Map<Integer, City> cityMap = new HashMap<Integer, City>();// key-cityId

	public int getCountryId() {
		 return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public Map<Integer, City> getCityMap() {
		return cityMap;
	}

	public void setCityMap(Map<Integer, City> cityMap) {
		this.cityMap = cityMap;
	}

}
