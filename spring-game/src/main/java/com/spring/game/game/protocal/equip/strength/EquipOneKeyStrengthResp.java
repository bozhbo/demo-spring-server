package com.snail.webgame.game.protocal.equip.strength;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;


public class EquipOneKeyStrengthResp extends MessageBody {
	private int result;
	private int heroId;// 英雄编号
	private int fightValue;// 战斗力

	private int equipId;// 装备编号
	private short equipLevel;// 装备强化等级（默认 0）
	private int equipFightValue;// 装备战斗力

	private int exp; // 装备强化经验
	private int itemSurplusNum; // 装备升级碎片

	private byte sourceType;// 1:银子 2:金子 3:体力 7:玩家经验 8:竞技场货币-勇气点 9:征战四方货币 正义点
							// 10:工会币 15:玩家等级 28:跨服币
	// 32:战功 34:历史战功 49:体力值购买次数 50:银子购买次数 51:经验活动剩余次数 52:金币活动剩余次数 53:用户名修改次数
	// 54:历史最高战斗力
	private int sourceChange;// 资源变动数,正值为增加,负值为减少

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroId", 0);
		ps.add("fightValue", 0);

		ps.add("equipId", 0);
		ps.add("equipLevel", 0);
		ps.add("equipFightValue", 0);

		ps.add("exp", 0);
		ps.add("itemSurplusNum", 0);
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

	public short getEquipLevel() {
		return equipLevel;
	}

	public void setEquipLevel(short equipLevel) {
		this.equipLevel = equipLevel;
	}

	public int getEquipFightValue() {
		return equipFightValue;
	}

	public void setEquipFightValue(int equipFightValue) {
		this.equipFightValue = equipFightValue;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getItemSurplusNum() {
		return itemSurplusNum;
	}

	public void setItemSurplusNum(int itemSurplusNum) {
		this.itemSurplusNum = itemSurplusNum;
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
