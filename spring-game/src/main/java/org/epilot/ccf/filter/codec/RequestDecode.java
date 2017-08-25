package org.epilot.ccf.filter.codec;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.SimpleByteBufferAllocator;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.epilot.ccf.common.AmfDataConvert;
import org.epilot.ccf.config.ConfigInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






public class RequestDecode  extends CumulativeProtocolDecoder {
	
	
	protected static final Logger log =LoggerFactory.getLogger("ccf");
	private String lengthEndian ;
	private String name ;
	public RequestDecode(String name) {
		 
		this.name = name; 
		lengthEndian = ConfigInit.getInstance().getLengthEndian(name);
	}

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		if(in.get(0) == 10)//AMF数据
		{
			Object msg = AmfDataConvert.byteToObj(in);
			
			out.write(msg);
			return true;
		}
		else{
			try{
				int oldPostion =in.position();
			
				
				if((lengthEndian.equalsIgnoreCase("1")&&
						in.remaining()>4&&
						in.position(oldPostion).
						order(ByteOrder.LITTLE_ENDIAN).prefixedDataAvailable(4))
					||(in.remaining()>4&&
						in.position(oldPostion).
						prefixedDataAvailable(4,204800)))
				{
					
					
					int length = in.getInt(oldPostion);//消息长度
				 
					in.getInt();
					byte c[] =new byte[length];
					in.get(c);
					out.write(c);
					return true;
				}
				else
				{
					if(in.position()>oldPostion)
					{
						in.position(in.position()-4);
					}
					return false;
				}
			}
			catch(Exception e)
			{
				log.error("Analyse data stream failure!");
				return false;
			}
		}
	}
}
