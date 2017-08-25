package com.snail.mina.protocol.info;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:消息头接口
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public interface IRoomHead {
	
	public int getMsgType();
	
	public void setMsgType(int msgType);
	
	public void bytes2Head(ByteBuffer buffer, ByteOrder order);
	
	public void head2Bytes(ByteBuffer buffer, ByteOrder order);
}
