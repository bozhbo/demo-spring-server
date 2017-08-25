package com.snail.webgame.engine.game.base.util;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类介绍:游戏服务器会话处理类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class SessionHandleImpl {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void notifyClose(IoSession session) {
//		//通讯服务器断开，清除列表注册
//		if (session.getAttribute("serverName") != null) {
//
//			String serverName = (String) session.getAttribute("serverName");
//
//			if (logger.isInfoEnabled()) {
//				logger.info("Server Interrupt :serverName=" + serverName);
//			}
//
//			ServerMap.removeServer(serverName);
//
//		}
	}

	public void notifyCreate(IoSession session) {
	}

	public void notifyException(IoSession session) {
		session.close();
	}

	public void notifyIdle(IoSession session) {
	}

	public void notifyOpen(IoSession session) {
	}

}
