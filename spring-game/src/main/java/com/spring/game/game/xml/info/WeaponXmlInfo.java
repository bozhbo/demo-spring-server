package com.snail.webgame.game.xml.info;

import java.util.List;
import java.util.Map;

/**
 * 神兵XML信息
 * @author xiasd
 */
public class WeaponXmlInfo {
	private int no;// 神兵编号
	private String name;// 神兵名称
	private int weaponType;// 神兵类型
	private int exp;// 神兵被吃的时候提供经验
	private int suit;// 套装编号
	private WeaponLv weaponLv;
	private WeaponLv updateWeaponLv;
	private List<WeaponSpecial> specialList;// 神兵隐藏属性
	private Map<Short, WeaponRefine> refineMap;// 核心神兵

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WeaponLv getWeaponLv() {
		return weaponLv;
	}

	public void setWeaponLv(WeaponLv weaponLv) {
		this.weaponLv = weaponLv;
	}

	public int getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}

	public List<WeaponSpecial> getSpecialList() {
		return specialList;
	}

	public void setSpecialList(List<WeaponSpecial> specialList) {
		this.specialList = specialList;
	}

	public Map<Short, WeaponRefine> getRefineMap() {
		return refineMap;
	}

	public void setRefineMap(Map<Short, WeaponRefine> refineMap) {
		this.refineMap = refineMap;
	}

	public WeaponLv getUpdateWeaponLv() {
		return updateWeaponLv;
	}

	public void setUpdateWeaponLv(WeaponLv updateWeaponLv) {
		this.updateWeaponLv = updateWeaponLv;
	}

}
