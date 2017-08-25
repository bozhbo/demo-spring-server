package com.snail.mina.protocol.code;


import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 类介绍:消息解码类
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public class RequestDecode extends CumulativeProtocolDecoder {

	private Logger logger = LoggerFactory.getLogger("room");
	private String lengthEndian = "2";

	public RequestDecode() {
	}

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			int oldPostion = in.position();

			if ((lengthEndian.equalsIgnoreCase("1") && 
				in.remaining() > 4 && 
				in.position(oldPostion).order(ByteOrder.LITTLE_ENDIAN).prefixedDataAvailable(4)) || 
				(in.remaining() > 4 && 
				in.position(oldPostion).prefixedDataAvailable(4, 409600))) {

				int length = in.getInt(oldPostion);// 消息长度
				in.getInt();
				byte c[] = new byte[length];
				in.get(c);

				out.write(c);
				return true;
			} else {
				if (in.position() > oldPostion) {
					in.position(in.position() - 4);
				}

				return false;
			}
		} catch (Exception e) {
			session.close();
			
			if (session.getRemoteAddress() != null) {
				logger.error("Analyse data stream failure! " + session.getRemoteAddress().toString(), e);
			} else {
				logger.error("Analyse data stream failure! ", e);
			}
			
			return false;
		}
	}
}
