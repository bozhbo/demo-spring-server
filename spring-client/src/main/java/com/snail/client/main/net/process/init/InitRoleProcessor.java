package com.snail.client.main.net.process.init;

import com.snail.client.main.net.msg.login.LoginResp;
import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;

public class InitRoleProcessor implements IProcessor {

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
		return GameMessageType.GAME_CLIENT_INIT_RECEIVE;
	}

}
