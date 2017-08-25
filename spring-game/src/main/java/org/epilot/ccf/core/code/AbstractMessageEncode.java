package org.epilot.ccf.core.code;




import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.codec.DefaultMessageDataHandle;
import org.epilot.ccf.codec.GameMessageDataHandle;
import org.epilot.ccf.core.protocol.Message;


/**
 * 消息编码抽象方法
 *
 */
public abstract class AbstractMessageEncode {
	GameMessageDataHandle messageDataHandle = new GameMessageDataHandle();
	DefaultMessageDataHandle defaultMessageDataHandle= new DefaultMessageDataHandle();
	public AbstractMessageEncode()
	{
		
	}
	
	/**
	 * 将对象转化为字节流
	 */
	protected abstract void encodeMessage(GameMessageDataHandle messageDataHandle,Message message,	ByteBuffer buffer);
	
	/**
	 * 将对象转化为字节流
	 */
	protected abstract void encodeMessage(DefaultMessageDataHandle messageDataHandle,Message message,	ByteBuffer buffer);
	
	
	
	public  void encode(Message message,	ByteBuffer buffer)
	{
		encodeMessage(messageDataHandle,message,buffer);
	}
	
	public  void defaultEncodeMessage(Message message,	ByteBuffer buffer)
	{
		encodeMessage(defaultMessageDataHandle,message,buffer);
	}
}
