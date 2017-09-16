package com.spring.common.logic.message;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.spring.logic.message.request.server.DeployRoleReq;

public class GameWorldDeployRoleReq extends DeployRoleReq {
	
	private int gateId;
	private String roleName;
	private int gold;
	private int vipLevel;
	private String header;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		super.bytes2Req(buffer, order);
		
		this.gateId = getInt(buffer, order);
		this.roleName = getString(buffer, order);
		this.gold = getInt(buffer, order);
		this.vipLevel = getInt(buffer, order);
		this.header = getString(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		super.resp2Bytes(buffer, order);
		
		setInt(buffer, order, this.gateId);
		setString(buffer, order, this.roleName);
		setInt(buffer, order, this.gold);
		setInt(buffer, order, this.vipLevel);
		setString(buffer, order, this.header);
	}

	public int getGateId() {
		return gateId;
	}

	public void setGateId(int gateId) {
		this.gateId = gateId;
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
