package com.snail.webgame.game.protocal.fightdeploy.buy;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class BuyDeployPosReq extends MessageBody {

	private byte position;// 选择购买开启位置

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("position", 0);
	}

	public byte getPosition() {
		return position;
	}

	public void setPosition(byte position) {
		this.position = position;
	}
}
