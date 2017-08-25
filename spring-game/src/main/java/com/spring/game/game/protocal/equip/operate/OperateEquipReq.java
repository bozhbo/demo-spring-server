package com.snail.webgame.game.protocal.equip.operate;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OperateEquipReq extends MessageBody {

	private int heroId;// 英雄编号
	private String equipId;// 装备编号
	private byte equipType;// 副将装备位置（主武将不管）
	private byte action;// 行为 1:穿上 2:卸下 3:一键穿装备 4:穿时装 5:卸时装

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("heroId", 0);
		ps.addString("equipId", "flashCode", 0);
		ps.add("equipType", 0);
		ps.add("action", 0);
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public byte getEquipType() {
		return equipType;
	}

	public void setEquipType(byte equipType) {
		this.equipType = equipType;
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}
}
