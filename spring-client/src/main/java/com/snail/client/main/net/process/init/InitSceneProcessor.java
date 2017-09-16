package com.snail.client.main.net.process.init;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.init.InitSceneResp;

public class InitSceneProcessor implements IProcessor {

	@Override
	public void processor(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		// TODO Auto-generated method stub
		return InitSceneResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_WORLD_SCENE_INIT_RECEIVE;
	}

}
