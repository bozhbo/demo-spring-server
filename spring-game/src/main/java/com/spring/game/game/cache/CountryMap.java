package com.snail.webgame.game.cache;

import java.util.HashMap;
import java.util.Map;

import com.snail.webgame.game.protocal.countryfight.common.Country;

public class CountryMap {
	private Map<Integer, Country> countryMap = new HashMap<Integer, Country>();
	
	public void addCountry(int id, Country country) {
		countryMap.put(id, country);
	}

	public Map<Integer, Country> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Map<Integer, Country> countryMap) {
		this.countryMap = countryMap;
	}
	
	
}
