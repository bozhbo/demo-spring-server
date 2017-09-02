package com.spring.world.io.process.common.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;

public class CommonProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(CommonProcessor.class);

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		CommonReq req = (CommonReq)message.getiRoomBody();
		
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return CommonReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND;
	}

}
