package com.snail.mina.protocol.processor.register;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class RegisterResp extends BaseRoomResp {
	private long startTime; // 已启动时间 单位为毫秒

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setLong(buffer, order, startTime);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	
}
