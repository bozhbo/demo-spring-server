package com.snail.webgame.game.protocal.gmcc.chat;

import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class ChatReq extends MessageBody {

	private int msgType;// 1-世界 2-公会 3-滚动栏
	private long recRoleId;
	private String recRoleName;
	private String msgContent;

	protected void setSequnce(ProtocolSequence ps) {
		ps.add("msgType", 0);
		ps.add("recRoleId", 0);
		ps.addString("recRoleName", "flashCode", 0);
		ps.addString("msgContent", "flashCode", 0);
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public long getRecRoleId() {
		return recRoleId;
	}

	public void setRecRoleId(long recRoleId) {
		this.recRoleId = recRoleId;
	}

	public String getRecRoleName() {
		return recRoleName;
	}

	public void setRecRoleName(String recRoleName) {
		this.recRoleName = recRoleName;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

}
