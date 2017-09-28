package com.snail.client.web.process.login;

import com.snail.client.web.msg.login.LoginResp;
import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;

public class LoginProcessor implements IProcessor {

	@Override
	public void processor(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		// TODO Auto-generated method stub
		return LoginResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_LOGIN_RECEIVE;
	}

}
