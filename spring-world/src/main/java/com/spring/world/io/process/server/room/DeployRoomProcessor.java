package com.spring.world.io.process.server.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoomResp;
import com.spring.logic.room.service.RoomService;

public class DeployRoomProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(DeployRoomProcessor.class);

	private RoomService roomService;
	
	@Override
	public void processor(Message message) {
		DeployRoomResp req = (DeployRoomResp)message.getiRoomBody();
		
		if (req.getResult() != 1) {
			// 房间添加失败
			logger.warn("deploy room failed " + req.getRoomId());
			roomService.closeRoom(req.getRoomId());
		} else {
			// 房间添加成功
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return DeployRoomResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_DEPLOY_ROOM_RESP;
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}
	
}
