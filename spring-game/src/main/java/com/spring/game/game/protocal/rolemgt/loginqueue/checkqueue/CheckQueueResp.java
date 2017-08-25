package com.snail.webgame.game.protocal.rolemgt.loginqueue.checkqueue;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CheckQueueResp extends MessageBody {
	private int result;
	private String account;

	@Override
	protected void setSequnce(ProtocolSequence ps) 
	{
		ps.add("result", 0);
		ps.addString("account", "flashCode", 0);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
