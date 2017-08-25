package com.snail.webgame.game.protocal.club.operation;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class OperationClubReq extends MessageBody {
	private int clubId;
	private int roleId;
	private int action; // 0-通过申请 1-拒绝申请 2-踢走成员3-解散公会

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("clubId", 0);
		ps.add("roleId", 0);
		ps.add("action", 0);
	}

	public int getClubId() {
		return clubId;
	}

	public void setClubId(int clubId) {
		this.clubId = clubId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

}
