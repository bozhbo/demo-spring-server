package com.snail.webgame.game.protocal.equip.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class EquipDetailRe extends MessageBody {

	private int equipId;// 装备编号

	private short equipNum;// 0-删除 1-数量

	private EquipInfoRe equipInfo = new EquipInfoRe();// 装备信息


	protected void setSequnce(ProtocolSequence ps) {
		ps.add("equipId", 0);
		ps.add("equipNum", 0);
		ps.addObject("equipInfo");
	}

	public int getEquipId() {
		return equipId;
	}

	public void setEquipId(int equipId) {
		this.equipId = equipId;
	}

	public EquipInfoRe getEquipInfo() {
		return equipInfo;
	}

	public void setEquipInfo(EquipInfoRe equipInfo) {
		this.equipInfo = equipInfo;
	}

	public short getEquipNum() {
		return equipNum;
	}

	public void setEquipNum(short equipNum) {
		this.equipNum = equipNum;
	}
}
