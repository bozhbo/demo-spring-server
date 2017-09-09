package com.spring.world.io.process.role.login;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.spring.logic.request.login.LoginResp;

public class GolfFlowerResp extends LoginResp {
	
	private int version;

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		super.resp2Bytes(buffer, order);
		//setInt(buffer, order, version);
		
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
}
