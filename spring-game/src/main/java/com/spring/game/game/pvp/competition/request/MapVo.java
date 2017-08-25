package com.snail.webgame.game.pvp.competition.request;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.webgame.engine.component.room.protocol.info.impl.BaseRoomResp;

public class MapVo extends BaseRoomResp {

	private byte mySide;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.mySide = getByte(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setByte(buffer, order, this.mySide);
	}

	public byte getMySide() {
		return mySide;
	}

	public void setMySide(byte mySide) {
		this.mySide = mySide;
	}
	
	
}
