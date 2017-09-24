package com.spring.logic.message.request.server;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class RoomSettlementRes extends BaseRoomResp {

	private int roleId;
	private int gold;
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, roleId);
		setInt(buffer, order, gold);
	}
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roleId = getInt(buffer, order);
		this.gold = getInt(buffer, order);
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	
}
