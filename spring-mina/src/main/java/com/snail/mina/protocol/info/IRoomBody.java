package com.snail.mina.protocol.info;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:消息体接口
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public interface IRoomBody {

	public void bytes2Req(ByteBuffer buffer, ByteOrder order);
	
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order);
	
	public void setMsgType(int msgType);
	
	public int getMsgType();
}
