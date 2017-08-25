package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.WeaponExpXmlInfo;

/**
 * 神兵经验缓存 WeaponExp.xml
 * 
 * @author xiasd
 * 
 */

public class WeaponExpXmlInfoMap {
	private static HashMap<Short, WeaponExpXmlInfo> weaponExpMap = new HashMap<Short, WeaponExpXmlInfo>();

	public static void addWeaponXmlInfo(WeaponExpXmlInfo weaponExpXmlInfo) {
		weaponExpMap.put(weaponExpXmlInfo.getLevel(), weaponExpXmlInfo);
	}

	public static WeaponExpXmlInfo getWeaponXmlInfoByLevel(Short level) {
		return weaponExpMap.get(level);
	}

}
