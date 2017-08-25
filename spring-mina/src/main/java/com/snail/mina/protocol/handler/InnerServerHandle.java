package com.snail.mina.protocol.handler;


import java.nio.charset.Charset;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.TransportType;
import org.apache.mina.transport.socket.nio.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerServerHandle extends IoHandlerAdapter {
	
	private static Logger logger = LoggerFactory.getLogger("room");
	
	private SessionStateHandler ioHandler;
	private byte[] b = new byte[0];

	public InnerServerHandle(SessionStateHandler ioHandler) {
		this.ioHandler = ioHandler;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		synchronized (b) {
			if (message instanceof String) {
				if (ioHandler != null) {
					String result = ioHandler.execCommand(((String) message));
				
					if (result != null) {
						ByteBuffer buffer = ByteBuffer.allocate(12);
						buffer.setAutoExpand(true);
						buffer.putString(result, Charset.forName("UTF-8").newEncoder());
						buffer.flip();
						session.write(buffer);
					}
				}
			}
		}
	}
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);

		try {
			if (session.getTransportType() == TransportType.SOCKET) {
				((SocketSessionConfig) session.getConfig()).setReceiveBufferSize(204800);
				((SocketSessionConfig) session.getConfig()).setKeepAlive(true);
				((SocketSessionConfig) session.getConfig()).setTcpNoDelay(true);

				session.setIdleTime(IdleStatus.BOTH_IDLE, 300);
			}
		} catch (Exception e) {
			logger.error("InnerServerHandle sessionOpened error", e);
		}
	}
	
	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		super.sessionIdle(session, status);
		logger.info("InnerServerHandle : client is Idle");
		sessionClosed(session);
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);

		if (session != null && session.isConnected()) {
			logger.info("InnerServerHandle : client is closed");
			session.close();
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		super.exceptionCaught(session, cause);
		logger.error("InnerServerHandle : session exception", cause);
	}
}
