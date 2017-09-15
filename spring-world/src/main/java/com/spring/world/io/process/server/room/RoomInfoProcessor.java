package com.spring.world.io.process.server.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.world.io.process.handler.GameServerSessionHandler;

public class RoomInfoProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoomInfoProcessor.class);

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		
		GameServerSessionHandler gameServerSessionHandler = GlobalBeanFactory.getBeanByName("GameServerSessionHandler", GameServerSessionHandler.class);
		gameServerSessionHandler.registerEnd(message.getSession(), head.getUserState());
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return null;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.ROOM_2_WORLD_ROOM_INFO;
	}

}
