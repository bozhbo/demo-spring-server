package com.snail.mina.protocol.handler;

import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.info.IRoomHead;

/**
 * 
 * 类介绍:连接事件回调接口
 * 此接口中的方法部分为网络IO线程调用，不能出现阻塞操作
 *
 * @author zhoubo
 * @since 2014-11-19
 */
public interface SessionStateHandler {
	
	/**
	 * 客户端主动关闭连接通知
	 * 
	 * @param session
	 */
	public void sessionClose(IoSession session);
	
	/**
	 * 用于服务器session参数设置，客户端无设置可忽略此接口
	 * 
	 * @param session
	 */
	public void sessionOpened(IoSession session);
	
	/**
	 * 检查客户端角色消息号,此接口适用于服务器端检查客户端Session合法性，客户端可忽略此接口
	 * 
	 * @param session	客户端Session
	 * @param roomHead	消息头
	 * @return	boolean true-检查成功，消息交由IProcessor处理，false-检查错误，消息不再被处理
	 */
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead);
	
	/**
	 * 客户端连接服务器成功后发送注册接口的保留字段, 服务端忽略此接口
	 * 
	 * @return String
	 */
	public String getRegisterReserve();
	
	/**
	 * 服务器接收到客户端注册接口，客户端忽略此接口
	 * 
	 * @param session
	 */
	public void registerEnd(IoSession session, String serverReserve);
	
	/**
	 * 执行内部命令,客户端忽略此接口
	 * 
	 * @param command	命令名称
	 * @return	返回结果
	 */
	public String execCommand(String command);
}
