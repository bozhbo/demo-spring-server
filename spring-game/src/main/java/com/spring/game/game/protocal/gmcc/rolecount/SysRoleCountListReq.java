package com.snail.webgame.game.protocal.gmcc.rolecount;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SysRoleCountListReq extends MessageBody {
	
	private byte type;
	private String roleCounts;

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("type", 0);
		ps.addString("roleCounts", "flashCode", 0);
	}

	public String getRoleCounts() {
		return roleCounts;
	}

	public void setRoleCounts(String roleCounts) {
		this.roleCounts = roleCounts;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
