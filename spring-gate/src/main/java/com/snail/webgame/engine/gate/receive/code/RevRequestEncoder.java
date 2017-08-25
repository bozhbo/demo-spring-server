package com.snail.webgame.engine.gate.receive.code;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.config.WebGameConfig;
import com.snail.webgame.engine.gate.filter.crypto.Crypto;


public class RevRequestEncoder implements ProtocolEncoder {

	private static final Logger log =LoggerFactory.getLogger("logs");
	public void dispose(IoSession session) throws Exception {
		
	
	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception
	{
		if(message instanceof String)
		{
			String msg = (String)message;
			byte b[] = msg.getBytes("UTF-8");
			ByteBuffer buffer = ByteBuffer.allocate(16, false);
			buffer.setAutoExpand(true);
			buffer.put(b);
			buffer.put((byte)0x00);
			buffer.flip();
			out.write(buffer);
		}
		else if(message instanceof byte[])
		{
			ByteBuffer buffer = ByteBuffer.allocate(16, false);
			buffer.setAutoExpand(true);
			
			buffer.put((byte[])message);
			buffer.flip();
			out.write(buffer);
		}
	}
}
