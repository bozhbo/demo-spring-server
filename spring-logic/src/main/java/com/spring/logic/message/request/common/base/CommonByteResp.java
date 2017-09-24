package com.spring.logic.message.request.common.base;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomResp;

public class CommonByteResp extends BaseRoomResp {

	private int optionType;
	private byte[] optionByte;
	
	public CommonByteResp() {
		
	}
	
	public CommonByteResp(int optionType, byte[] optionByte) {
		this.optionType = optionType;
		this.optionByte = optionByte;
	}
	
	@Override
	public void resp2Bytes(ByteBuffer buffer, ByteOrder order) {
		setInt(buffer, order, optionType);
		setBytes(buffer, order, optionByte);
		
	}

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public byte[] getOptionByte() {
		return optionByte;
	}

	public void setOptionByte(byte[] optionByte) {
		this.optionByte = optionByte;
	}

	
	
}
