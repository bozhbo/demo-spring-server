package com.snail.webgame.game.protocal.equip.operate;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OperateEquipResp extends MessageBody {

	private int result;
	private int heroId;// 英雄编号
	private int fightValue;// 战斗力

	private String upEquipId;// 穿上的装备编号
	private String downEquipId;// 卸下的装备编号
	
	private byte action;// 行为 1:穿上 2:卸下 3:一键穿装备
	private byte equipType;// 副将装备位置（主武将不管）

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("heroId", 0);
		ps.add("fightValue", 0);

		ps.addString("upEquipId", "flashCode", 0);
		ps.addString("downEquipId", "flashCode", 0);
		
		ps.add("action", 0);
		ps.add("equipType", 0);
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

	public String getUpEquipId() {
		return upEquipId;
	}

	public void setUpEquipId(String upEquipId) {
		this.upEquipId = upEquipId;
	}

	public String getDownEquipId() {
		return downEquipId;
	}

	public void setDownEquipId(String downEquipId) {
		this.downEquipId = downEquipId;
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	public byte getEquipType() {
		return equipType;
	}

	public void setEquipType(byte equipType) {
		this.equipType = equipType;
	}
}
