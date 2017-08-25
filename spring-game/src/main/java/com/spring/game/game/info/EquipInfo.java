package com.snail.webgame.game.info;

import java.util.Map;

import com.snail.webgame.game.xml.cache.EquipXMLInfoMap;
import com.snail.webgame.game.xml.info.EquipXMLInfo;

/**
 * 装备信息
 * @author zenggang
 */
public class EquipInfo {

	public static final int PRO_TYPE_BASE = 1;// 基础属性信息(强化)
	public static final int PRO_TYPE_GEM = 2;// 宝石镶嵌属性信息

	public static final int EQUIP_TYPE = 1;
	public static final int TYPE_EQUIP_USED = 1;
	public static final int EQUIP_LEVLE = 0;// 默认装备强化等级

	private int id;// id 数据库主键ID
	private int equipNo;// 装备编号 XML/道具编号
	private int equipType;// 装备位置
	private short level;// 装备强化等级（默认 0）
	private int exp;// 装备强化经验
	private short refineLv; // 装备精炼等级	
	private short enchantLv;//附魔等级
	private int enchantExp;// 附魔经验
	
	// 缓存
	private int fightValue;// 战斗力

	public EquipInfo() {
	}

	public EquipInfo(int equipNo, int equipType, short level) {
		this.equipNo = equipNo;
		this.equipType = equipType;
		this.level = level;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public int getEquipType() {
		if (equipType == 0) {
			EquipXMLInfo xmlinfo = EquipXMLInfoMap.getEquipXMLInfo(equipNo);
			if (xmlinfo != null) {
				return xmlinfo.getEquipType();
			}
		}
		return equipType;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public short getLevel() {
		return level;
	}

	public void setLevel(short level) {
		this.level = level;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public short getRefineLv() {
		return refineLv;
	}

	public void setRefineLv(short refineLv) {
		this.refineLv = refineLv;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public short getEnchantLv() {
		return enchantLv;
	}

	public void setEnchantLv(short enchantLv) {
		this.enchantLv = enchantLv;
	}

	public int getEnchantExp() {
		return enchantExp;
	}

	public void setEnchantExp(int enchantExp) {
		this.enchantExp = enchantExp;
	}

	/**
	 * 宝石信息
	 * @return
	 */
	public Map<Integer, Integer> getStoneMap() {
		return null;
	}

	public String getStoneStr(Map<Integer, Integer> stoneMap) {
		StringBuilder stoneSB = new StringBuilder();// 宝石字符串
		if (stoneMap != null) {
			for (int seat : stoneMap.keySet()) {
				int stoneNo = stoneMap.get(seat);
				stoneSB.append(seat).append(":").append(stoneNo).append(",");
			}
		}

		if (stoneSB.length() > 0) {
			return stoneSB.substring(0, stoneSB.length() - 1);
		}
		return "";
	}

	/**
	 * 根据宝石位置获取装备宝石编号
	 * @param seat
	 * @return
	 */
	public int getEquipStoneNoBySeat(int seat) {
		Map<Integer, Integer> stoneMap = getStoneMap();
		if (stoneMap != null && stoneMap.containsKey(seat)) {
			return stoneMap.get(seat);
		}
		return 0;
	}

	/**
	 * 根据装备宝石编号获取宝石位置
	 * @param stoneNo
	 * @return
	 */
	public int getEquipStoneSeatByStoneNo(int stoneNo) {
		Map<Integer, Integer> stoneMap = getStoneMap();
		if (stoneMap != null) {
			for (int seat : stoneMap.keySet()) {
				if (stoneMap.get(seat) == stoneNo) {
					return seat;
				}
			}
		}
		return 0;
	}

}
