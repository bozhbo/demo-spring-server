package com.snail.mina.protocol.processor.register;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class RegisterReq extends BaseRoomReq {
	private String serverName;
	private String reserve;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.serverName = getString(buffer, order);
		this.reserve = getString(buffer, order);
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setString(buffer, order, serverName);
		setString(buffer, order, reserve);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
	
}
