package com.snail.mina.protocol.handler;


import java.util.concurrent.Executor;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.util.RoomValue;

/**
 * 
 * 类介绍:客户端连接处理类
 * 
 * @author zhoubo
 * @since 2014-11-19
 */
public class ClientHandle extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger("room");

	private Executor executor;
	private SessionStateHandler ioHandler;

	public ClientHandle(SessionStateHandler ioHandler, Executor executor) {
		this.ioHandler = ioHandler;
		this.executor = executor;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		sessionClosed(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);

		if (session != null && session.isConnected()) {
			session.close();
		}

		if (session.getAttribute("serverName") != null) {
			String serverName = (String) session.getAttribute("serverName");

			if (serverName != null && !"".equals(serverName)) {
				RoomMessageConfig.serverMap.remove(serverName);
				logger.info("ClientHandle : server " + serverName + " is close session");
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		logger.error("session exception", cause);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		try {
			if (ioHandler != null) {
				ioHandler.sessionOpened(session);
			} else {
				if (session.getTransportType() == TransportType.SOCKET) {
					((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
					((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);

					session.setIdleTime(IdleStatus.BOTH_IDLE, 20);
				}
				
				logger.warn("ClientHandle : Client Session is use default socket setting");
				logger.warn("ClientHandle : KeepAlive = " + true);
				logger.warn("ClientHandle : TcpNoDelay = " + true);
				logger.warn("ClientHandle : IdleStatus = BOTH_IDLE(20)");
			}
		} catch (Exception e) {
			logger.error("ClientHandle sessionOpened error", e);
		}
	}

	public void messageReceived(IoSession session, Object in) {
		if (in instanceof Message) {
			Message message = (Message) in;

			if (message.getiRoomHead().getMsgType() == RoomValue.MESSAGE_TYPE_HEARTBEAT_FF01) {
				// 心跳连接
			} else if (RoomMessageConfig.processorMap.containsKey(message.getiRoomHead().getMsgType())) {
				if (this.executor == null) {
					message.run();
					
					logger.warn("ClientHandle : Io processor thread is call processor");
				} else {
					this.executor.execute(message);
				}
			} else {
				logger.error("ClientHandle : these is no processor to use with input msgtype = " + Integer.toHexString(message.getiRoomHead().getMsgType()));
			}
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("ClientHandle : receive type is not message");
			}
		}
	}

	public Executor getExecutor() {
		return executor;
	}

	public SessionStateHandler getIoHandler() {
		return ioHandler;
	}
}
