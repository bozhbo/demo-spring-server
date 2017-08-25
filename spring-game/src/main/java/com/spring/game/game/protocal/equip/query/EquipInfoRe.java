package com.snail.webgame.game.protocal.equip.query;

import java.util.ArrayList;
import java.util.List;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.equip.equipStone.EquipStoneRe;

public class EquipInfoRe extends MessageBody {

	private int equipId;// 装备编号
	private int heroId;// 英雄编号 0-未装备
	private int equipNo;// 装备编号
	private byte equipType;// 装备位置
	private short level;// 装备强化等级（默认 0）
	private int exp; // 装备强化经验
	private int fightValue;// 装备战斗力
	private int refineLv; // 装备精炼等级

	private int stoneNum; // 宝石数量
	private List<EquipStoneRe> stoneList = new ArrayList<EquipStoneRe>(); // 宝石相关属性
	
	private short enchantLv;//附魔等级
	private int enchantExp;// 附魔经验

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("equipId", 0);
		ps.add("heroId", 0);
		ps.add("equipNo", 0);
		ps.add("equipType", 0);

		ps.add("level", 0);
		ps.add("exp", 0);
		ps.add("fightValue", 0);

		ps.add("refineLv", 0);
		ps.add("stoneNum", 0);
		ps.addObjectArray("stoneList", "com.snail.webgame.game.protocal.equip.equipStone.EquipStoneRe", "stoneNum");
	
		ps.add("enchantLv", 0);
		ps.add("enchantExp", 0);
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(int equipNo) {
		this.equipNo = equipNo;
	}

	public byte getEquipType() {
		return equipType;
	}

	public void setEquipType(byte equipType) {
		this.equipType = equipType;
	}

	public int getStoneNum() {
		return stoneNum;
	}

	public void setStoneNum(int stoneNum) {
		this.stoneNum = stoneNum;
	}

	public List<EquipStoneRe> getStoneList() {
		return stoneList;
	}

	public void setStoneList(List<EquipStoneRe> stoneList) {
		this.stoneList = stoneList;
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

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getRefineLv() {
		return refineLv;
	}

	public void setRefineLv(int refineLv) {
		this.refineLv = refineLv;
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
}
