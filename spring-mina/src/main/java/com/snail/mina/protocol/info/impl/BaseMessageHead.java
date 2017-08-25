package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import com.snail.mina.protocol.info.IRoomHead;

/**
 * 
 * 类介绍:基础消息头
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public abstract class BaseMessageHead implements IRoomHead {

	private int length;	// 消息长度
	private ByteOrder messageByteOrder;	// 消息顺序
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public ByteOrder getMessageByteOrder() {
		return messageByteOrder;
	}
	public void setMessageByteOrder(ByteOrder messageByteOrder) {
		this.messageByteOrder = messageByteOrder;
	}
}
