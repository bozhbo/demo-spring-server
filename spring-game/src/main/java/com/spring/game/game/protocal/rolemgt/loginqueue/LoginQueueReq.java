package com.snail.webgame.game.protocal.rolemgt.loginqueue;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class LoginQueueReq extends MessageBody {
	
	private String account;

	@Override
	protected void setSequnce(ProtocolSequence ps) 
	{
		ps.addString("account", "flashCode", 0);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
