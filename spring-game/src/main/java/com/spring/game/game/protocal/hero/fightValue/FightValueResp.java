package com.snail.webgame.game.protocal.hero.fightValue;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class FightValueResp extends MessageBody {

	private int result;
	private int heroFightValue; //英雄战斗力
	private int equipFightValue;//装备战斗力
	private int magicFightValue;//宝石战斗力（神兵）
	private int soldierFightValue;//兵法战斗力

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		
		ps.add("heroFightValue", 0);
		ps.add("equipFightValue", 0);
		ps.add("magicFightValue", 0);
		ps.add("soldierFightValue", 0);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getHeroFightValue() {
		return heroFightValue;
	}

	public void setHeroFightValue(int heroFightValue) {
		this.heroFightValue = heroFightValue;
	}

	public int getEquipFightValue() {
		return equipFightValue;
	}

	public void setEquipFightValue(int equipFightValue) {
		this.equipFightValue = equipFightValue;
	}

	public int getMagicFightValue() {
		return magicFightValue;
	}

	public void setMagicFightValue(int magicFightValue) {
		this.magicFightValue = magicFightValue;
	}

	public int getSoldierFightValue() {
		return soldierFightValue;
	}

	public void setSoldierFightValue(int soldierFightValue) {
		this.soldierFightValue = soldierFightValue;
	}
	
}
