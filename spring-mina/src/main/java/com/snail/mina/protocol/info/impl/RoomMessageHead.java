package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:房间服务器消息头
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RoomMessageHead extends BaseMessageHead {
	private int msgType;// 消息号
	private int userState;	// 消息附带值
	
	@Override
	public int getMsgType() {
		return msgType;
	}
	
	@Override
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	public int getUserState() {
		return userState;
	}
	public void setUserState(int userState) {
		this.userState = userState;
	}
	
	public void bytes2Head(ByteBuffer buffer, ByteOrder order) {
		this.msgType = buffer.order(order).getInt();
		this.userState = buffer.order(order).getInt();
	}
	
	public void head2Bytes(ByteBuffer buffer, ByteOrder order) {
		buffer.order(order).putInt(msgType);
		buffer.order(order).putInt(userState);
	}
	
}
