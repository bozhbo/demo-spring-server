package com.snail.mina.protocol.code;

import java.nio.charset.Charset;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDecode extends CumulativeProtocolDecoder {

	private Logger logger = LoggerFactory.getLogger("room");

	public CommandDecode() {
	}

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			if (in.remaining() >= 4) {
	            String command = in.getString(Charset.forName("UTF-8").newDecoder());
	            out.write(command);
	            return true;
	        } else {
	            return false;
	        }
		} catch (Exception e) {
			session.close();
			logger.error("Analyse data stream failure!", e);
			return false;
		}
	}
}
