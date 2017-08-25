package com.snail.webgame.engine.net.msg.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseMessageHead;

public class GameMessageHead extends BaseMessageHead {
	
	private int version;// (4~7)
	private int userID0;// (8~11)客户端角色ID
	private int userID1;// (12~15)游戏通讯服务器ID
	private int userID2;// (16~19)服务器端使用序列号
	private int userID3;// (20~13)场景ID(IP(engine-gate 调整))
	private int msgType;// (40~43)

	@Override
	public int getMsgType() {
		return this.msgType;
	}

	@Override
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	@Override
	public void bytes2Head(ByteBuffer buffer, ByteOrder order) {
		this.version = buffer.order(order).getInt();
		this.userID0 = buffer.order(order).getInt();
		this.userID1 = buffer.order(order).getInt();
		this.userID2 = buffer.order(order).getInt();
		this.userID3 = buffer.order(order).getInt();
		this.msgType = buffer.order(order).getInt();
		
	}

	@Override
	public void head2Bytes(ByteBuffer buffer, ByteOrder order) {
		buffer.order(order).putInt(this.version);
		buffer.order(order).putInt(this.userID0);
		buffer.order(order).putInt(this.userID1);
		buffer.order(order).putInt(this.userID2);
		buffer.order(order).putInt(this.userID3);
		buffer.order(order).putInt(this.msgType);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getUserID0() {
		return userID0;
	}

	public void setUserID0(int userID0) {
		this.userID0 = userID0;
	}

	public int getUserID1() {
		return userID1;
	}

	public void setUserID1(int userID1) {
		this.userID1 = userID1;
	}

	public int getUserID2() {
		return userID2;
	}

	public void setUserID2(int userID2) {
		this.userID2 = userID2;
	}

	public int getUserID3() {
		return userID3;
	}

	public void setUserID3(int userID3) {
		this.userID3 = userID3;
	}

	
}
