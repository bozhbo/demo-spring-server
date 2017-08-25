package org.epilot.ccf.core.code;


import org.epilot.ccf.core.protocol.MessageBody;
import org.epilot.ccf.core.protocol.MessageHeader;
import org.epilot.ccf.core.util.ByteBufferDataHandle;



/**
 * 对象字节流之间的转换
 *
 */

public abstract class ProtocolChange {
	/**
	 *	通过协议将字节流转化为对象
	 * @param key 
	 * @param msgHandle
	 */
	public abstract void setBodyObject(String key,MessageBody body,ByteBufferDataHandle byteBufferDataHandle);
	/**
	 *	通过协议将字节流转化为对象
	 * @param key 
	 * @param msgHandle
	 */
	public abstract void setHeaderObject(String key,MessageHeader header,ByteBufferDataHandle byteBufferDataHandle);
	/**
	 *	通过协议将对象转化为字节流
	 * @param key 
	 * @param msgHandle
	 */
	public abstract void getBodyBuffer(String key,MessageBody body,ByteBufferDataHandle byteBufferDataHandle);
	/**
	 *	通过协议将对象转化为字节流
	 * @param key 
	 * @param msgHandle
	 */
	public abstract void getHeaderBuffer(String key,MessageHeader header,ByteBufferDataHandle messageDataHandle);

}
