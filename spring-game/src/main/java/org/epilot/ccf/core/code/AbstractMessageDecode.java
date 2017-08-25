package org.epilot.ccf.core.code;




import org.apache.mina.common.ByteBuffer;
import org.epilot.ccf.core.protocol.Message;


/**
 * 消息解码抽象方法
 * 
 */
public abstract  class AbstractMessageDecode {

	

	public AbstractMessageDecode()
	{

	}
	/**
	 * 将字节流转化为具体的消息
	 * @return 消息对象
	 */
	public abstract Message decodeMessage(ByteBuffer buffer );
}
