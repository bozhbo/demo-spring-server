package com.snail.webgame.game.protocal.relation.dispose;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class DisposeAddRequestResp extends MessageBody {
	private int result;
	private int action; // 0 - 同意 1 - 拒绝
	private int relRoleId; // 发送请求的角色Id

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("result", 0);
		ps.add("action", 0);
		ps.add("relRoleId", 0);

	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
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
