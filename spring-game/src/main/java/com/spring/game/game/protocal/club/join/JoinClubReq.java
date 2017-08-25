package com.snail.webgame.game.protocal.club.join;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class JoinClubReq extends MessageBody {
	private int clubId;
	private int action; // 0 - 加入 1 - 退出公会

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.add("action", 0);
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
