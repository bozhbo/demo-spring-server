package com.spring.world.io.process.common.push;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class PushResp extends BaseRoomResp {
	
	private String info;

	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setString(buffer, order, info);
		
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	
}
