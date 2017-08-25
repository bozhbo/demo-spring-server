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
 * 类介绍:服务器连接处理类
 * 
 * @author zhoubo
 * @since 2014-11-19
 */
public class ServerHandle extends IoHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger("room");

	private Executor executor;
	private SessionStateHandler ioHandler;

	public ServerHandle(SessionStateHandler ioHandler, Executor executor) {
		this.ioHandler = ioHandler;
		this.executor = executor;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		logger.info("ServerHandle : client is Idle");
		sessionClosed(session);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);

		try {
			if (this.ioHandler != null) {
				ioHandler.sessionClose(session);
			}
		} catch (Exception e) {
			logger.error("ServerHandle sessionClosed error", e);
		}
		
		if (session != null && session.isConnected()) {
			logger.info("ServerHandle : client is closed");
			session.close();
		}

		if (session.getAttribute("serverName") != null) {
			String serverName = (String) session.getAttribute("serverName");

			if (serverName != null && !"".equals(serverName)) {
				RoomMessageConfig.serverMap.remove(serverName);
				logger.info("ServerHandle : server " + serverName + " is closed");
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		logger.error("ServerHandle : session exception", cause);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		try {
			if (ioHandler != null) {
				ioHandler.sessionOpened(session);
			} else {
				if (session.getTransportType() == TransportType.SOCKET) {
					((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(204800);
					((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
					((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);

					session.setIdleTime(IdleStatus.BOTH_IDLE, 20);
				}

				logger.warn("ServerHandle : Server Session is use default socket setting");
				logger.warn("ServerHandle : ReceiveBufferSize = " + 204800);
				logger.warn("ServerHandle : KeepAlive = " + true);
				logger.warn("ServerHandle : TcpNoDelay = " + true);
				logger.warn("ServerHandle : IdleStatus = BOTH_IDLE(20)");
			}
		} catch (Exception e) {
			logger.error("ServerHandle sessionOpened error", e);
		}
	}

	public void messageReceived(IoSession session, Object in) {
		if (in instanceof Message) {
			Message message = (Message) in;

			if (message.getiRoomHead().getMsgType() == RoomValue.MESSAGE_TYPE_HEARTBEAT_FF01) {
				// 心跳连接，直接返回
				if (session != null && session.isConnected()) {
					session.write(in);
				}
			} else {
				if (ioHandler != null) {
					if (!ioHandler.checkRoleMessage(session, message.getiRoomHead())) {
						return;
					}
				}

				if (RoomMessageConfig.processorMap.containsKey(message.getiRoomHead().getMsgType())) {
					if (this.executor == null) {
						message.run();

						logger.warn("ServerHandle : Io processor thread is call processor");
					} else {
						this.executor.execute(message);
					}
				} else {
					logger.error("ServerHandle : these is no processor to use with input msgtype = " + Integer.toString(message.getiRoomHead().getMsgType(), 16));
				}
			}
		} else {
			if (logger.isErrorEnabled()) {
				logger.error("ServerHandle : receive type is not message");
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
