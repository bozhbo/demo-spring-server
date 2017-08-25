package com.snail.webgame.game.protocal.relation.onekey.energy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OneKeyEnergyReq extends MessageBody {
	private int flag; // 0 - 一键领取 1 - 一键赠送

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("flag", 0);

	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
