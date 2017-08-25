package org.epilot.ccf.codec;

import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.core.code.AbstractMessageDecode;
import org.epilot.ccf.core.protocol.Message;
import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.util.ByteBufferDataHandle;
import org.epilot.ccf.mapping.ClassMapping;



public class MessageDecode  extends AbstractMessageDecode{
	private String serviceName;
	
	public MessageDecode(String serviceName) {
		this.serviceName = serviceName;
	}


	public Message decodeMessage(ByteBuffer buffer ) {
		ByteBufferDataHandle byteBufferDataHandle ;
		GameMessageDataHandle messageDataHandle  = new GameMessageDataHandle();
		
		MessageBody	body = null;
		MessageHeader header =null;
		header = ClassMapping.buildHeader(serviceName);
		
		//没有消息头,直接返回null
		if(header == null)
		{
			return null;
		}
		byteBufferDataHandle =new ByteBufferDataHandle(buffer);

		//给消息头赋值
		messageDataHandle.setHeaderObject(serviceName,header, byteBufferDataHandle);

		body = ClassMapping.buildBody(String.valueOf(header.getProtocolId()));
		
		if(body !=null)//可以存在只拥有消息头的消息
		{
			messageDataHandle.setBodyObject(String.valueOf(header.getProtocolId()),body, byteBufferDataHandle);
		}
		Message message = new Message();
		message.setHeader(header);
		message.setBody(body);
		
		return message;
	}
}
