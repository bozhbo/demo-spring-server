package com.snail.webgame.game.common;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoSession;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.epilot.ccf.core.session.handle.SessionHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.ServerMap;

public class SendSessionHandleImpl implements SessionHandle {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public void notifyClose(IoSession session) {

		// 通讯服务器断开，清除列表注册
		if (session.getAttribute("serverName") != null) {

			String serverName = (String) session.getAttribute("serverName");

			if (logger.isInfoEnabled()) {
				logger.info("Server Interrupt :serverName=" + serverName);
			}
			ServerMap.removeServer(serverName);
		}

	}

	public void notifyCreate(IoSession session) {

	}

	public void notifyException(IoSession session) {

		session.close();

	}

	public void notifyIdle(IoSession session) {

		// session.close();
	}

	public void notifyOpen(IoSession session) {
		((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
		((SocketSessionConfig) session.getConfig()).setTcpNoDelay(false);
		((SocketSessionConfig) session.getConfig()).setSendBufferSize(204800);
		((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(204800);
		session.setWriteTimeout(10);
		session.setIdleTime(IdleStatus.WRITER_IDLE, 30);

	}

}
