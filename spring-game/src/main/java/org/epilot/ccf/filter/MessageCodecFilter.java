package org.epilot.ccf.filter;

import java.nio.ByteOrder;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.epilot.ccf.codec.MessageDecode;
import org.epilot.ccf.codec.MessageEncode;
import org.epilot.ccf.common.AmfDataConvert;
import org.epilot.ccf.config.ConfigInit;
import org.epilot.ccf.core.code.AbstaractCodeFilter;
import org.epilot.ccf.core.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.io.amf.ASObject;


public class MessageCodecFilter extends AbstaractCodeFilter{

	private String serviceName;
	private static final Logger log =LoggerFactory.getLogger("ccf");
	public MessageCodecFilter()
	{
		
	}
	public MessageCodecFilter(String serviceName) {
		
		this.serviceName =  serviceName;
	}

	
		
	public void sessionCreated(NextFilter nextFilter, IoSession session) {
      
        nextFilter.sessionCreated(session);
    }

    public void sessionOpened(NextFilter nextFilter, IoSession session) {
      
        nextFilter.sessionOpened(session);
    }

    public void sessionClosed(NextFilter nextFilter, IoSession session) {
 
        nextFilter.sessionClosed(session);
    }

    public void sessionIdle(NextFilter nextFilter, IoSession session,
            IdleStatus status) {

        nextFilter.sessionIdle(session, status);
    }

    public void exceptionCaught(NextFilter nextFilter, IoSession session,
            Throwable cause) {

        nextFilter.exceptionCaught(session, cause);
    }


    public void messageReceived(NextFilter nextFilter, IoSession session,
            Object message) {
    	if(message instanceof byte[])
    	{
    	 
    		ByteBuffer in = ByteBuffer.allocate(((byte[])message).length);
    		in.put((byte[])message);
    		in.position(0);
    		MessageDecode messageDecode =new MessageDecode(serviceName);
    		Message msg =messageDecode.decodeMessage(in);
    		
    		if(msg!=null)
    		{
    			nextFilter.messageReceived(session, msg);
    		}
    		else
    		{
    			nextFilter.messageReceived(session, message);
    		}
    	}
    	else if(message instanceof ASObject){
    		nextFilter.messageReceived(session, message);
    	}
        
    }

    public void messageSent(NextFilter nextFilter, IoSession session,
            Object message) {

        nextFilter.messageSent(session, message);
    }

    public void filterWrite(NextFilter nextFilter, IoSession session,
            WriteRequest writeRequest) {
    	Object message =  writeRequest.getMessage();
    	ByteBuffer buffer = null;
		 
    	String lengthEndian = ConfigInit.getInstance().getLengthEndian(serviceName);

    	if(message instanceof ByteBuffer)
		{
			nextFilter.filterWrite(session, new WriteRequest(message));
			return ;
		}
    	else if(message instanceof byte[])
		{
    		buffer = ByteBuffer.allocate(16,false);
			byte [] b =(byte[]) message;
			buffer.setAutoExpand( true );
			buffer.put(b);
			buffer.flip();
		}
		else if(message instanceof Message )
		{
			buffer = ByteBuffer.allocate(16,false);
			buffer.setAutoExpand(true);
				
			buffer.putInt(0);//设置长度
				
			MessageEncode messageEncode = new MessageEncode(serviceName);
			messageEncode.encode((Message) message,buffer);
		
			int i = buffer.position();
				
			if(lengthEndian.equalsIgnoreCase("1"))
			{
				buffer.order(ByteOrder.LITTLE_ENDIAN).putInt(0,i-4);
			}
			else
			{
				
				buffer.order(ByteOrder.BIG_ENDIAN).putInt(0,i-4);
			}
			buffer.flip();
		}
		else if(message instanceof ASObject){
			byte[] b = AmfDataConvert.objToBytes((ASObject)message);
			buffer = ByteBuffer.allocate(16,false);
			buffer.setAutoExpand( true );
			buffer.put(b);
			buffer.flip();
		}
		else
		{	 
			log.error("The Message type is not ByteBuffer,message can not be sended!");
		}
			
		if(buffer !=null)
		{
			nextFilter.filterWrite(session, new WriteRequest(buffer));
		}
    }

    public void filterClose(NextFilter nextFilter, IoSession session)
            throws Exception {
 
        nextFilter.filterClose(session);
    }
}
