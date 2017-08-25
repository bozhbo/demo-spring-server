package org.epilot.ccf.filter.flash;

 

import java.net.InetSocketAddress;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.epilot.ccf.common.AmfDataConvert;
import org.epilot.ccf.config.Config;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 






public class RequestDecode  extends CumulativeProtocolDecoder {
	
	
	protected static final Logger log =LoggerFactory.getLogger("ccf");
	private  static String xmlStr = "<cross-domain-policy>" +
			"<allow-access-from domain=\""+Config.getInstance().getFlashDomain()
			+"\" to-ports=\""+Config.getInstance().getFlashPort()+"\" />" +
					"</cross-domain-policy>";
 
	public RequestDecode(String name) {
		 
		 
	}

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {

		if(in.get(0) == 10)//AMF数据
		{
			Object msg = AmfDataConvert.byteToObj(in);
			
			out.write(msg);
			return true;
		}
		else{
			if(in.position()==0&&in.remaining()<23)
			{
				return false;
			}
			if(in.position()==0&&in.get(22)==0&&in.remaining()==23)
		    { 
					byte b[] = new byte[22];
					in.get(b);
					in.get();//取出最后的0x00;
					String str = "";
					try
					{
						str = new String(b,"UTF-8");
					}catch(Exception e){
					 }
					if(str.startsWith("<policy-file-request/>"))
					{
						if(session.isConnected())
						{
							 
							byte bb [] = xmlStr.getBytes("UTF-8");
							ByteBuffer buffer = ByteBuffer.allocate(16,false);
							buffer.setAutoExpand(true);
							buffer.put(bb);
							buffer.put((byte) 0x00);
							buffer.flip();
							out.write(buffer);
							session.write(buffer);//返回flash的安全校验
						}
						return true;
					}
					else
					{
						return false;
					}
	
			}
			else 
			{
			  try{
					int oldPostion =in.position();
				
					if(in.remaining()>4&&in.prefixedDataAvailable(4,1024000))
					{
						int length = in.getInt(oldPostion);
						in.getInt();
						byte c[] =new byte[length];
						in.get(c);
						out.write(c);
						return true;
					}
					else
					{
						return false;
					}
				 }
				 catch(Exception e){
					 
					 InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
					 	String ip = address.getAddress().getHostAddress();
						String port = String.valueOf(address.getPort());
						if(log.isErrorEnabled())
						{
							log.error("Analyse data stream failure ,session will be closed!"+
									"ip="+ip+",port="+port);
						}
						session.close();//关闭连接
						return false;
				  }
			} 
		}
	}
}
