package com.spring.logic.message.request.common.base;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

import com.snail.mina.protocol.info.impl.BaseRoomReq;

public class CommonReq extends BaseRoomReq {
	
	private int optionType;
	private String optionStr;

	@Override
	public void bytes2Req(ByteBuffer buffer, ByteOrder order) {
		this.optionType = getInt(buffer, order);
		this.optionStr = getString(buffer, order);
		
	}

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public String getOptionStr() {
		return optionStr;
	}

	public void setOptionStr(String optionStr) {
		this.optionStr = optionStr;
	}

	
}
