package com.snail.webgame.game.xml.cache;

import java.util.HashMap;

import com.snail.webgame.game.xml.info.WeaponLv;
import com.snail.webgame.game.xml.info.WeaponXmlInfo;

/**
 * 神兵XML
 * 
 * @author xiasd
 *
 */
public class WeaponXmlInfoMap {
	private static HashMap<Integer, WeaponXmlInfo> weaponMap = new HashMap<Integer, WeaponXmlInfo>();

	public static void addWeaponXmlInfo(WeaponXmlInfo weaponXmlInfo) {
		weaponMap.put(weaponXmlInfo.getNo(), weaponXmlInfo);
	}

	public static WeaponXmlInfo getWeaponXmlInfoByNo(int no){
		return weaponMap.get(no);
	}
	
	public static WeaponLv getWeaponSkillLv(int weaponNo){
		WeaponXmlInfo info = weaponMap.get(weaponNo);
		if(info != null){
			return info.getWeaponLv();
		}
		return null;
	}
	
	public static int getWeaponType(int weaponNo){
		WeaponXmlInfo info = weaponMap.get(weaponNo);
		
		if(info != null){
			return info.getWeaponType();
		}
		return 0;
	}

	public static HashMap<Integer, WeaponXmlInfo> getWeaponMap() {
		return weaponMap;
	}

	public static void setWeaponMap(HashMap<Integer, WeaponXmlInfo> weaponMap) {
		WeaponXmlInfoMap.weaponMap = weaponMap;
	}
	
	
}
