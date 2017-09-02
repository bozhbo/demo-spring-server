package com.snail.client.main.net.handler;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.net.msg.login.LoginResp;
import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;

public class GameClientHandler extends IoHandlerAdapter {
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Message msg = (Message)message;
		
		if (msg.getiRoomHead().getMsgType() == GameMessageType.GAME_CLIENT_LOGIN_RECEIVE) {
			ClientControl.roleService.loginEnd((LoginResp)msg.getiRoomBody());
		}
	}
}
