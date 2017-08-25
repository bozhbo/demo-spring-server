package com.snail.webgame.game.protocal.rolemgt.logout;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class UserLogoutReq extends MessageBody {

 
	private int roleId;
	
	protected void setSequnce(ProtocolSequence ps) {
		 
		ps.add("roleId", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
