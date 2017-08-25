package org.epilot.ccf.filter.flash;



import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;




public class RequestEncoder implements ProtocolEncoder {


	private String name;
	public RequestEncoder(String name) {
		this.name = name;
	}

	public void dispose(IoSession session) throws Exception {
		
	
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		
		if(message instanceof ByteBuffer)
		{
			out.write((ByteBuffer)message);
		}

	}

}
