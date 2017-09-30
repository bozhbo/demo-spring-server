package com.spring.logic.message.request.world.init;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class InitResp extends BaseRoomResp {
	
	private int roleId;
	private int roomId;
	private String roleName;
	private int vipLevel;
	private String head;
	private int gold;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.roleId = getInt(buffer, order);
		this.roomId = getInt(buffer, order);
		this.roleName = getString(buffer, order);
		this.vipLevel = getInt(buffer, order);
		this.head = getString(buffer, order);
		this.gold = getInt(buffer, order);
	}

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, this.roleId);
		setInt(buffer, order, this.roomId);
		setString(buffer, order, this.roleName);
		setInt(buffer, order, this.vipLevel);
		setString(buffer, order, this.head);
		setInt(buffer, order, this.gold);
		
	}
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	

}
