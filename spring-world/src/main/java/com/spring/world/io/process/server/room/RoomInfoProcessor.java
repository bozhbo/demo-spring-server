package com.spring.world.io.process.server.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.world.room.service.RoomManageService;

@Component
public class RoomInfoProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoomInfoProcessor.class);

	private RoomManageService roomManageService;
	
	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		roomManageService.roomInfo(head.getUserState());
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return null;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.ROOM_2_WORLD_ROOM_INFO;
	}

	@Autowired
	public void setRoomManageService(RoomManageService roomManageService) {
		this.roomManageService = roomManageService;
	}
}
