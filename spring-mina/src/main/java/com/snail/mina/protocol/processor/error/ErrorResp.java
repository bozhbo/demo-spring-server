package com.snail.mina.protocol.processor.error;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

/**
 * 
 * 类介绍:错误信息返回类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class ErrorResp extends BaseRoomResp {
	
	private int result;
	
	public ErrorResp(int result) {
		this.result = result;
	}

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, result);
	}

}