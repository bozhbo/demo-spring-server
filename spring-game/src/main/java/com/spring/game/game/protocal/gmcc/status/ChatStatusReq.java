package com.snail.webgame.game.protocal.gmcc.status;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChatStatusReq extends MessageBody {

	private int roleId;
	private byte chatStatus;
	private long time;

	protected void setSequnce(ProtocolSequence ps) {

		ps.add("roleId", 0);
		ps.add("chatStatus", 0);
		ps.add("time", 0);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public byte getChatStatus() {
		return chatStatus;
	}

	public void setChatStatus(byte chatStatus) {
		this.chatStatus = chatStatus;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
