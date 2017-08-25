package com.snail.mina.protocol.code;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 
 * 类介绍:消息编码类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RequestEncoder implements ProtocolEncoder {

	public void dispose(IoSession session) throws Exception {

	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

		if (message instanceof ByteBuffer) {
			System.out.println( "limit = " + ((ByteBuffer)message).limit());
			out.write((ByteBuffer) message);
		}
	}

}
