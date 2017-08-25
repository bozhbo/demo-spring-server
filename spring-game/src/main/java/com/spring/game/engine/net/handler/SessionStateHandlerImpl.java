package com.snail.webgame.engine.net.handler;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.handler.SessionStateHandler;
import com.snail.webgame.engine.component.room.protocol.info.IRoomHead;

/**
 * 
 * 类介绍:游戏服务器Session监听通知类
 *
 * @author zhoubo
 * @version 1.0.0
 * @since 2016年1月21日
 */
public class SessionStateHandlerImpl implements SessionStateHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("engine");

	@Override
	public void sessionClose(IoSession session) {
		if (session != null) {
			logger.info("session close from server " + session.getRemoteAddress().toString());
		}
	}

	@Override
	public void sessionOpened(IoSession session) {
		if (session != null) {
			logger.info("session open from server " + session.getRemoteAddress().toString());
		}
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		return true;
	}

	@Override
	public String getRegisterReserve() {
		return null;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		
	}

	@Override
	public String execCommand(String command) {
		return null;
	}

}
