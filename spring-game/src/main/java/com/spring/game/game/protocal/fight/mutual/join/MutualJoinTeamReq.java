package com.snail.webgame.game.protocal.fight.mutual.join;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualJoinTeamReq extends MessageBody {

	private int leaderRoleId;// 队长ID
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("leaderRoleId", 0);
		
	}

	public int getLeaderRoleId() {
		return leaderRoleId;
	}

	public void setLeaderRoleId(int leaderRoleId) {
		this.leaderRoleId = leaderRoleId;
	}
	
	
}
