package com.snail.webgame.game.protocal.relation.search;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class SearchRoleInfoReq extends MessageBody {
	private String roleName; // 要查询的玩家名

	@Override
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
