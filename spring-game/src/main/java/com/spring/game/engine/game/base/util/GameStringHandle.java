package com.snail.webgame.engine.game.base.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.common.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.net.msg.IStringHandle;

public class GameStringHandle implements IStringHandle {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	private static GameStringHandle gameStringHandle = new GameStringHandle();
	
	private GameStringHandle() {
		
	}
	
	public synchronized static GameStringHandle getGameStringHandle() {
		return gameStringHandle;
	}

	@Override
	public String decodeString(ByteBuffer buffer, ByteOrder order) {
		Charset charset = Charset.forName("UTF-8");
		CharsetDecoder decoder = charset.newDecoder();
		try {
			if (buffer.remaining() > 2) {
				String str = buffer.getPrefixedString(2, decoder);
				return str;
			} else {
				return "";
			}

		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("", e);
			}
			return "";
		}
	}

	@Override
	public void encodeString(ByteBuffer buffer, ByteOrder order, String str) {
		if (str != null && str.length() > 0) {
			byte[] m = null;
			try {
				m = str.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {

				if (logger.isErrorEnabled()) {
					logger.error("", e);
				}
			}

			if (m != null && m.length > 0) {
				byte n[] = new byte[m.length + 2];

				System.arraycopy(m, 0, n, 2, m.length);
				byte k[] = shortToBytes((short) m.length);
				System.arraycopy(k, 0, n, 0, 2);

				buffer.put(n);
				return;
			}
		}
		
		buffer.put(shortToBytes((short) 0));
	}
	
	private byte[] shortToBytes(short sNum) {
		byte[] bytesRet = new byte[2];
		bytesRet[0] = (byte) ((sNum >> 8) & 0xFF);
		bytesRet[1] = (byte) (sNum & 0xFF);
		return bytesRet;
	}

}
