package com.snail.mina.protocol.info.impl;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

/**
 * 
 * 类介绍:基础客户端发送类
 * 一般用于向远端发送消息
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public abstract class BaseRoomResp extends AbstractRoomBody {

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		return;
	}
	
}