package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:基础客户端请求类
 * 一般用于接收来自远端的消息
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public abstract class BaseRoomReq extends AbstractRoomBody {

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		return;
	}
	
	

}
