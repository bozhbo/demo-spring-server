package com.snail.webgame.game.protocal.attackAnother.sweep;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class AttackAnotherSweepReq extends MessageBody {
	
	private int attType; //难度类型 1-普通 2-精英 3-勇士

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("attType", 0);
	}

	public int getAttType() {
		return attType;
	}

	public void setAttType(int attType) {
		this.attType = attType;
	}
}
