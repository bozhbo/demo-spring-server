package com.snail.webgame.game.protocal.fight.mutual.invite;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualInviteResp extends MessageBody {

	private int leaderRoleId; // 队长Id
	private String leaderRoleName; // 队长Id
	private int activityType; // 1-长坂坡  2-组队副本 3-3V3竞技场

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("leaderRoleId", 0);
		ps.addString("leaderRoleName", "flashCode", 0);
		ps.add("activityType", 0);
	}

	public int getLeaderRoleId() {
		return leaderRoleId;
	}

	public void setLeaderRoleId(int leaderRoleId) {
		this.leaderRoleId = leaderRoleId;
	}

	public String getLeaderRoleName() {
		return leaderRoleName;
	}

	public void setLeaderRoleName(String leaderRoleName) {
		this.leaderRoleName = leaderRoleName;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	
}
