package com.snail.webgame.game.protocal.relation.dispose;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DisposeAddRequestReq extends MessageBody{
	private int action; //0 - 同意 1 - 拒绝  2 - 黑名单 3 - 移除黑名单
	private int relRoleId; //发送请求的角色Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("action", 0);
		ps.add("relRoleId", 0);
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(int relRoleId) {
		this.relRoleId = relRoleId;
	}

}
