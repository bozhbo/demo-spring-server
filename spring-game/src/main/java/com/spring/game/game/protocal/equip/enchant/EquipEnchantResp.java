package com.snail.webgame.game.protocal.equip.enchant;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipEnchantResp extends MessageBody {
	private int result;
	private int heroId;// 英雄编号
	private int fightValue;// 战斗力

	private int equipId;// 装备编号
	private short enchantLv;// 附魔等级（默认 0）
	private int enchantExp; // 附魔经验
	private int equipFightValue;// 装备战斗力

	private byte sourceType;// 1:银子 2:金子
	private int sourceChange;// 资源变动数,正值为增加,负值为减少

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroId", 0);
		ps.add("fightValue", 0);

		ps.add("equipId", 0);
		ps.add("enchantLv", 0);
		ps.add("enchantExp", 0);
		ps.add("equipFightValue", 0);

		ps.add("sourceType", 0);
		ps.add("sourceChange", 0);

	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getFightValue() {
		return fightValue;
	}

	public void setFightValue(int fightValue) {
		this.fightValue = fightValue;
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
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

	public int getEquipFightValue() {
		return equipFightValue;
	}

	public void setEquipFightValue(int equipFightValue) {
		this.equipFightValue = equipFightValue;
	}

	public byte getSourceType() {
		return sourceType;
	}

	public void setSourceType(byte sourceType) {
		this.sourceType = sourceType;
	}

	public int getSourceChange() {
		return sourceChange;
	}

	public void setSourceChange(int sourceChange) {
		this.sourceChange = sourceChange;
	}
}
