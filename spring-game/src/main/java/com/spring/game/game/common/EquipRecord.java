package com.snail.webgame.game.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 装备记录
 * @author zenggang
 */
public class EquipRecord {

	private int id;// id;
	private int equipNo;// 装备编号
	private int equipType;// 装备位置
	private int equipLevel;// 装备强化等级（默认 0）
	private short refineLv; // 装备精炼等级
	private short enchantLv;//附魔等级

	// 宝石信息<seat,stoneNo>
	private Map<Integer, Integer> stoneMap = new HashMap<Integer, Integer>();

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public int getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(int equipLevel) {
		this.equipLevel = equipLevel;
	}

	public int getEquipType() {
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public short getRefineLv() {
		return refineLv;
	}

	public void setRefineLv(short refineLv) {
		this.refineLv = refineLv;
	}

	public Map<Integer, Integer> getStoneMap() {
		return stoneMap;
	}

	public void setStoneMap(Map<Integer, Integer> stoneMap) {
		this.stoneMap = stoneMap;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public short getEnchantLv() {
		return enchantLv;
	}

	public void setEnchantLv(short enchantLv) {
		this.enchantLv = enchantLv;
	}
}
