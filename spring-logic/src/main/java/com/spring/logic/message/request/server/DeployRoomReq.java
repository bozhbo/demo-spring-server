package com.spring.logic.message.request.server;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class DeployRoomReq extends BaseRoomReq {

	private int roomId;
	private int roomType;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roomId = getInt(buffer, order);
		this.roomType = getInt(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.roomId);
		setInt(buffer, order, this.roomType);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getRoomType() {
		return roomType;
	}

	public void setRoomType(int roomType) {
		this.roomType = roomType;
	}
	
	
}
