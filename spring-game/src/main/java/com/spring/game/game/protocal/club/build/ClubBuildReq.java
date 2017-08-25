package com.snail.webgame.game.protocal.club.build;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ClubBuildReq extends MessageBody {
	private int flag; // 0 - 低级建设 1 - 中级建设 2 - 高级建设

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
