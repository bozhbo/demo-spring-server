package com.snail.client.web.process.room.init;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.room.game.message.init.GameRoomInitResp;

public class RoomJoinProcessor implements IProcessor {

	@Override
	public void processor(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		// TODO Auto-generated method stub
		return GameRoomInitResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_ROOM_JOIN;
	}

}
