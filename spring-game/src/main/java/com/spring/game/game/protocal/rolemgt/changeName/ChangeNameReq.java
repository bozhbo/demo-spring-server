package com.snail.webgame.game.protocal.rolemgt.changeName;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChangeNameReq extends MessageBody {

	private String roleName;

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("roleName", "flashCode", 0);
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
