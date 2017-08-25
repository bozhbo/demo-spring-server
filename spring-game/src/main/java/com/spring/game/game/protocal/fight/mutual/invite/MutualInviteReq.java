package com.snail.webgame.game.protocal.fight.mutual.invite;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MutualInviteReq extends MessageBody {

	private int memberRoleId; // 队员Id
	private int activityType; // 1-长坂坡  2-组队副本 3-3V3竞技场
	private int duplicateId;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("memberRoleId", 0);
		ps.add("activityType", 0);
		ps.add("duplicateId", 0);
	}

	public int getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(int memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	public int getActivityType() {
		return activityType;
	}

	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}

	public int getDuplicateId() {
		return duplicateId;
	}

	public void setDuplicateId(int duplicateId) {
		this.duplicateId = duplicateId;
	}
	
}
