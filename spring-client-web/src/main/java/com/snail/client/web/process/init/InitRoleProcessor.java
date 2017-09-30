package com.snail.client.web.process.init;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.init.InitResp;

public class InitRoleProcessor implements IProcessor {

	@Override
	public void processor(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return InitResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_INIT_RECEIVE;
	}

}
