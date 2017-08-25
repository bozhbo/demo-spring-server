package com.snail.webgame.game.protocal.fight.mutual.leave;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class MakeLeaveReq extends MessageBody {

	private int memberRoleId;// 队员ID
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("memberRoleId", 0);
		
	}

	public int getMemberRoleId() {
		return memberRoleId;
	}

	public void setMemberRoleId(int memberRoleId) {
		this.memberRoleId = memberRoleId;
	}

	
}
