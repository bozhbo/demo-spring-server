package org.epilot.ccf.codec;

import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.core.code.AbstractMessageEncode;
import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.util.ByteBufferDataHandle;


public class MessageEncode extends AbstractMessageEncode {
	private String serviceName;
	
	public MessageEncode(String serviceName) {
		this.serviceName = serviceName;
	}

	protected  void encodeMessage(GameMessageDataHandle messageDataHandle,Message message,ByteBuffer buffer) {
		
	
		ByteBufferDataHandle byteBufferDataHandle =new ByteBufferDataHandle(buffer);
		MessageHeader header = message.getHeader();
		MessageBody body = message.getBody();
		
		if(header ==null)//无消息头
		{
			return;
		}
 
		messageDataHandle.getHeaderBuffer(serviceName,header, byteBufferDataHandle);
		if(body !=null)
		{
			messageDataHandle.getBodyBuffer(String.valueOf(header.getProtocolId()),body, byteBufferDataHandle);
		
		}
		
	}

	@Override
	protected void encodeMessage(DefaultMessageDataHandle messageDataHandle,
			Message message, ByteBuffer buffer) {
		 
		
	}
}
