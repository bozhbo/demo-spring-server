package com.spring.logic.message.request.server;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class DisconnectRoleReq extends BaseRoomReq {

	private int roomId;
	private int roleId;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roomId = getInt(buffer, order);
		this.roleId = getInt(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.roomId);
		setInt(buffer, order, this.roleId);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
}

