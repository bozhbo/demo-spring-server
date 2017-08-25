package com.snail.webgame.engine.net.msg;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;

public interface IStringHandle {

	public String decodeString(ByteBuffer buffer, ByteOrder order);
	
	public void encodeString(ByteBuffer buffer, ByteOrder order, String str);
}
