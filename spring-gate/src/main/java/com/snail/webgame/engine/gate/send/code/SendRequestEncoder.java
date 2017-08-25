package com.snail.webgame.engine.gate.send.code;



import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;




public class SendRequestEncoder implements ProtocolEncoder {


	public void dispose(IoSession session) throws Exception {
		
	
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		
 
		if(message instanceof byte[])
		{
			ByteBuffer buffer = ByteBuffer.allocate(16,false);
			byte [] b =(byte[]) message;
			buffer.setAutoExpand( true );
			buffer.put(b);
			buffer.flip();
			out.write(buffer);
		}
	}

}
