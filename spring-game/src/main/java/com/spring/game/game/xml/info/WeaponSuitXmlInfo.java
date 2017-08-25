package com.snail.webgame.game.xml.info;

import java.util.Map;

/**
 * 神兵套装 MagicSuit.xml
 * 
 * @author xiasd
 * 
 */
public class WeaponSuitXmlInfo {
	private int no;// 套装编号
	private Map<Byte, WeaponSuitNum> map;// 套装效果

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public Map<Byte, WeaponSuitNum> getMap() {
		return map;
	}

	public void setMap(Map<Byte, WeaponSuitNum> map) {
		this.map = map;
	}

}
