package com.snail.webgame.game.protocal.recruit.recruit;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChestItemReq extends MessageBody {

	private byte action;// RecruitKind.xml No 1-银子单抽 2-银子十抽 3-装备单抽 4-装备十抽  7-武将单抽 8-武将十连抽（武将）

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("action", 0);
	}

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

}