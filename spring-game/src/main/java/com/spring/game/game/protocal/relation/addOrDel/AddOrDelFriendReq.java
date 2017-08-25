package com.snail.webgame.game.protocal.relation.addOrDel;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class AddOrDelFriendReq extends MessageBody {
	private int relRoleId; // 删除或者 添加的角色ID
	private int flag; // 0 - 添加好友 1 - 删除好友

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("relRoleId", 0);
		ps.add("flag", 0);
	}

	public int getRelRoleId() {
		return relRoleId;
	}

	public void setRelRoleId(int relRoleId) {
		this.relRoleId = relRoleId;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
