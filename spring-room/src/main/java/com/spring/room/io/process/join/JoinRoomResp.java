package com.spring.room.io.process.join;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class JoinRoomResp extends BaseRoomResp {
	
	private int roomId;
	private int roleId;
	private String roleName;
	private int gold;
	private int vipLevel;
	private String header;
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, roomId);
		setInt(buffer, order, roleId);
		setString(buffer, order, roleName);
		setInt(buffer, order, gold);
		setInt(buffer, order, vipLevel);
		setString(buffer, order, header);
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	
}
