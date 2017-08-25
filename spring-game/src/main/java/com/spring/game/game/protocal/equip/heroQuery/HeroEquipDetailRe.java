package com.snail.webgame.game.protocal.equip.heroQuery;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

import com.snail.webgame.game.protocal.equip.query.EquipInfoRe;

public class HeroEquipDetailRe extends MessageBody {

	private int equipId;// 装备编号

	private EquipInfoRe equipInfo;// 装备信息

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("equipId", 0);
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

	public void setEquipInfo(EquipInfoRe equipInfoRe) {
		this.equipInfo = equipInfoRe;
	}
	
}
