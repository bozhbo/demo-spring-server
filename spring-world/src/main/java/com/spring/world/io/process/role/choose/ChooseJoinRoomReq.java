package com.spring.world.io.process.role.choose;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class ChooseJoinRoomReq extends BaseRoomReq {

	private int roomType;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roomType = getInt(buffer, order);

	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}

	
}
