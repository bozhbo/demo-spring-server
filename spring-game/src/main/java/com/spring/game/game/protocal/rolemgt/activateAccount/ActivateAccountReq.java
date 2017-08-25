package com.snail.webgame.game.protocal.rolemgt.activateAccount;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ActivateAccountReq extends MessageBody {
	
	private String account;//账号
	private String activateCode;//激活码

	@Override
	protected void setSequnce(ProtocolSequence ps) {

		ps.addString("account", "flashCode", 0);
		ps.addString("activateCode", "flashCode", 0);
		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getActivateCode() {
		return activateCode;
	}

	public void setActivateCode(String activateCode) {
		this.activateCode = activateCode;
	}

}
