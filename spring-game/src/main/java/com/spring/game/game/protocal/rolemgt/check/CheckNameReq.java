package com.snail.webgame.game.protocal.rolemgt.check;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class CheckNameReq extends MessageBody {

	private String account;// 玩家登陆帐号
	private String roleName;// 角色名称

	protected void setSequnce(ProtocolSequence ps) {
		ps.addString("account", "flashCode", 0);
		ps.addString("roleName", "flashCode", 0);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
