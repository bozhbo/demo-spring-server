package com.snail.webgame.game.pvp.competition.register;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomReq;

public class RegisterResp extends BaseRoomReq {
	private long startTime; // 已启动时间 单位为毫秒

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		startTime = getLong(buffer, order);
		
	}
	
	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
}
