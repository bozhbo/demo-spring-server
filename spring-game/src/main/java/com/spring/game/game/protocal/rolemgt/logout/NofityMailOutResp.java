package com.snail.webgame.game.protocal.rolemgt.logout;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class NofityMailOutResp extends MessageBody {

 
	private String roleIds;
	
	protected void setSequnce(ProtocolSequence ps) {
		 
		ps.addString("roleIds", "flashCode", 0);
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	
}
