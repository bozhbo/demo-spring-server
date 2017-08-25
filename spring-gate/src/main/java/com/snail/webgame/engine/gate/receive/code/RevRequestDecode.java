package com.snail.webgame.engine.gate.receive.code;

 

import java.net.InetSocketAddress;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.gate.config.TempTestConfig;
import com.snail.webgame.engine.gate.config.WebGameConfig;

 



public class RevRequestDecode   extends CumulativeProtocolDecoder {
	private static final Logger log =LoggerFactory.getLogger("logs");


 
	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		//正式开服指定
		if(in.position() == 0 && (in.remaining() == 1 || in.remaining() == 3)){
			InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
			String ip = address.getAddress().getHostAddress();
			if(ip != null && ip.equals(WebGameConfig.getInstance().getLocalConfig().getLocalIP())){
				char c =  (char)in.get();
				if(in.remaining() == 2){
					in.get();
					in.get();
				}
				if(c == 'i' && TempTestConfig.instance().isOpen()){
					TempTestConfig.instance().setOpen(false);
					log.info("The server formally open succeeds! ");
				}
				
			}
			return false;
		}
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
					
					out.write(str);
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
			
				if(in.remaining()>4&&in.prefixedDataAvailable(4,204800))
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
			 catch(Exception e){
			 
				 	InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
				 	String ip = address.getAddress().getHostAddress();
					String port = String.valueOf(address.getPort());
					if(log.isErrorEnabled())
					{
						log.error("Analyse data stream failure,ip="+ip+",port="+port+
								",session will be closed!");
					}
					session.close();//关闭连接
					return false;
			  }
		}
	}

}
