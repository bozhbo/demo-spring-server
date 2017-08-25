package com.snail.webgame.game.protocal.fight.competition.award;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GetAwardReq extends MessageBody {

	private byte stage; // 领取段位

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("stage", 0);
		
	}

	public byte getStage() {
		return stage;
	}

	public void setStage(byte stage) {
		this.stage = stage;
	}
	
	
}
