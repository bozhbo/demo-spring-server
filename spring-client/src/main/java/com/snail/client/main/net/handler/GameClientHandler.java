package com.snail.client.main.net.handler;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

public class GameClientHandler extends IoHandlerAdapter {
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
	}
}
