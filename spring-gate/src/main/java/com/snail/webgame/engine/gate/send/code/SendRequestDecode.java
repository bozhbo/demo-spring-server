package com.snail.webgame.engine.gate.send.code;

 

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SendRequestDecode   extends CumulativeProtocolDecoder {
	private static final Logger log =LoggerFactory.getLogger("logs");


 
	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try{
			
				int oldPostion =in.position();
			
				if(in.remaining()>4&&in.prefixedDataAvailable(4))
				{
					int length = in.getInt(oldPostion);
					byte c[] =new byte[length+4];
					in.get(c);
					out.write(c);
					return true;
				}
				else
				{
					return false;
				}
			}
		
		catch(Exception e)
		{
			session.close();
			log.error("Analyse data stream failure!");
			return false;
		}
		 

	}


}
