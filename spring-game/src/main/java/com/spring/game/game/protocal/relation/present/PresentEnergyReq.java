package com.snail.webgame.game.protocal.relation.present;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class PresentEnergyReq extends MessageBody {
	private int relRoleId; // 赠送的好友Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("relRoleId", 0);
	}

	public int getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(int relRoleId) {
		this.relRoleId = relRoleId;
	}

}
