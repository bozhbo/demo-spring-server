package com.snail.webgame.game.protocal.mine.invite;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class InviteMineHelpReq extends MessageBody {

	private int mineId;// 矿id
	private int helpId;// 好友Id 0-所在工会

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("mineId", 0);
		ps.add("helpId", 0);
	}

	public int getMineId() {
		return mineId;
	}

	public void setMineId(int mineId) {
		this.mineId = mineId;
	}

	public int getHelpId() {
		return helpId;
	}

	public void setHelpId(int helpId) {
		this.helpId = helpId;
	}
}
