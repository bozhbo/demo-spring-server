package com.snail.webgame.game.common;

import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.protocol.ProtocolSequence;

public class GameMessageHead extends MessageHeader {
	private int Length;// (0~3)
	private int Version;// (4~7)
	private int UserID0;// (8~11)客户端角色ID
	private int UserID1;// (12~15)游戏通讯服务器ID
	private int UserID2;// (16~19)服务器端使用序列号
	private int UserID3;// (20~13)场景ID(IP(engine-gate 调整))
	private int MsgType;// (40~43)

	@Override
	protected void setSequnce(ProtocolSequence ps) {
		ps.add("Version", 0);
		ps.add("UserID0", 0);
		ps.add("UserID1", 0);
		ps.add("UserID2", 0);
		ps.add("UserID3", 0);
		ps.add("MsgType", 0);
	}

	public long getProtocolId() {
		return MsgType;
	}

	public void setProtocolId(long protocolId) {
		this.MsgType = (int) protocolId;
	}

	public int getMsgType() {
		return MsgType;
	}

	public void setMsgType(int msgType) {
		MsgType = msgType;
	}

	public int getLength() {
		return Length;
	}

	public void setLength(int length) {
		Length = length;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

	public int getUserID0() {
		return UserID0;
	}

	public void setUserID0(int userID0) {
		UserID0 = userID0;
	}

	public int getUserID1() {
		return UserID1;
	}

	public void setUserID1(int userID1) {
		UserID1 = userID1;
	}

	public int getUserID2() {
		return UserID2;
	}

	public void setUserID2(int userID2) {
		UserID2 = userID2;
	}

	public int getUserID3() {
		return UserID3;
	}

	public void setUserID3(int userID3) {
		UserID3 = userID3;
	}
}
