package com.spring.room.io.process.handler;

import org.apache.mina.common.IoSession;

import com.snail.mina.protocol.handler.SessionStateHandler;
import com.snail.mina.protocol.info.IRoomHead;

public class RoomServerSessionHandler implements SessionStateHandler {

	@Override
	public void sessionClose(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionOpened(IoSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkRoleMessage(IoSession session, IRoomHead roomHead) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getRegisterReserve() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerEnd(IoSession session, String serverReserve) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String execCommand(String command) {
		// TODO Auto-generated method stub
		return null;
	}

}
