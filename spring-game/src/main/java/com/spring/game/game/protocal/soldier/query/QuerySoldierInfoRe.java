package com.snail.webgame.game.protocal.soldier.query;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class QuerySoldierInfoRe extends MessageBody {
	private byte soldierType;	//士兵类型
	private int soldierLevel;	//士兵等级
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("soldierType", 0);
		ps.add("soldierLevel", 0);
	}
	public byte getSoldierType() {
		return soldierType;
	}
	public void setSoldierType(byte soldierType) {
		this.soldierType = soldierType;
	}
	public int getSoldierLevel() {
		return soldierLevel;
	}
	public void setSoldierLevel(int soldierLevel) {
		this.soldierLevel = soldierLevel;
	}
}
