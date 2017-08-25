package com.snail.webgame.engine.net.msg.handler;

import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.common.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.net.msg.IStringHandle;

public class ChargeStringHandle implements IStringHandle {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static ChargeStringHandle chargeStringHandle = new ChargeStringHandle();
	
	private ChargeStringHandle() {
		
	}
	
	public synchronized static ChargeStringHandle getChargeStringHandle() {
		return chargeStringHandle;
	}

	@Override
	public String decodeString(ByteBuffer buffer, ByteOrder order) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();//通过GB2312的编码重新构造一个解码器
		try {
			String str = buffer.getString(decoder);//把此字节按GB2312的编码转换成字符串
			return str;
		} catch (CharacterCodingException e) {
			logger.error("ChargeStringHandle decodeString error", e);
			return null;
		}
	}

	@Override
	public void encodeString(ByteBuffer buffer, ByteOrder order, String str) {
		byte[] c = null;
		if (str == null || str.length() == 0) {
			c = new byte[1];
			buffer.put(c);
			return;
		}
		try {

			byte b[] = str.getBytes("UTF-8");
			c = new byte[b.length + 1];
			System.arraycopy(b, 0, c, 0, b.length);
			buffer.put(c);
			return;
		} catch (Exception e) {
			logger.error("ChargeStringHandle encodeString error", e);
			return;
		}
	}
}
