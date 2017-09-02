package com.spring.world.io.process.active;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class ActiveReq extends BaseRoomReq {

	private String gateServerName;
	private int flag;
	private String groupServerId;
	
	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.gateServerName = getString(buffer, order);
		this.flag = getInt(buffer, order);
		this.groupServerId = getString(buffer, order);
		
	}

	public String getGateServerName() {
		return gateServerName;
	}

	public void setGateServerName(String gateServerName) {
		this.gateServerName = gateServerName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getGroupServerId() {
		return groupServerId;
	}

	public void setGroupServerId(String groupServerId) {
		this.groupServerId = groupServerId;
	}
	
	
}
