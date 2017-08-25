package org.epilot.ccf.core.session.handle;

import org.apache.mina.common.IoSession;
/**
 * 
 * @author tangjie
 *
 */
public interface SessionHandle {
	
	public void notifyOpen(IoSession session);
	/**
	 * 连接建立时调用
	 * @param session
	 */
	public  void notifyCreate(IoSession session);
	/**
	 * 连接异常时调用
	 * @param session
	 */
	public  void notifyException(IoSession session);
	/**
	 * 连接空闲时调用
	 * @param session
	 */
	public void notifyIdle(IoSession session);
	
	/**
	 * 连接关闭时调用
	 * @param session
	 */
	public  void notifyClose(IoSession session);
	
	
}
