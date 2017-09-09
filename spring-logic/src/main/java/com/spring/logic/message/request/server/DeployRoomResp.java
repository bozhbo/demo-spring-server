package com.spring.logic.message.request.server;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class DeployRoomResp extends BaseRoomResp {
	
	private int result;
	private int roomId;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.result = getInt(buffer, order);
		this.roomId = getInt(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.result);
		setInt(buffer, order, this.roomId);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

}
