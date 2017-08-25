package com.snail.webgame.game.protocal.worship.doWorship;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DoWorshipReq extends MessageBody{

	private int superRoleId;
	
	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("superRoleId", 0);
		
	}

	public int getSuperRoleId() {
		return superRoleId;
	}

	public void setSuperRoleId(int superRoleId) {
		this.superRoleId = superRoleId;
	}
	
	

}
