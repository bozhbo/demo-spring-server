package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.WeaponSuitXmlInfo;

/**
 * 神兵套装效果 MagicSuit.xml
 * 
 * @author xiasd
 * 
 */
public class WeaponSuitXmlInfoMap {
	private static HashMap<Integer, WeaponSuitXmlInfo> weaponSuitMap = new HashMap<Integer, WeaponSuitXmlInfo>();

	public static void addWeaponSuitXmlInfo(WeaponSuitXmlInfo weaponSuitXmlInfo) {
		weaponSuitMap.put(weaponSuitXmlInfo.getNo(), weaponSuitXmlInfo);
	}

	public static WeaponSuitXmlInfo getWeaponSuitXmlInfoByNo(int no) {
		return weaponSuitMap.get(no);
	}

}
