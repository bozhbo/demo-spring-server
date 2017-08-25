package com.snail.mina.protocol.processor;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;

/**
 * 
 * 类介绍:具体业务处理接口
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public interface IProcessor {

	/**
	 * 业务处理方法
	 * 
	 * @param message	消息类
	 */
	public void processor(Message message);
	
	/**
	 * 返回请求消息体的类对象
	 * 
	 * @return Class 消息体的类对象
	 */
	public Class<? extends IRoomBody> getRoomBodyClass();
	
	/**
	 * 返回消息的接口号
	 * 
	 * @return	int 接口号
	 */
	public int getMsgType();
}
