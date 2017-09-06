package com.snail.client.main.net.msg.join;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class JoinRoomResp extends BaseRoomReq {
	
	private int roomId;
	private int roleId;
	private String roleName;
	private int gold;
	private int vipLevel;
	private String header;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roomId = getInt(buffer, order);
		this.roleId = getInt(buffer, order);
		this.roleName = getString(buffer, order);
		this.gold = getInt(buffer, order);
		this.vipLevel = getInt(buffer, order);
		this.header = getString(buffer, order);
		
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
