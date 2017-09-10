package com.spring.room.io.process.deploy;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoomReq;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.event.DeployRoomEvent;

public class RoomDeployProcessor implements IProcessor {
	
	@Override
	public void processor(Message message) {
		DeployRoomReq req = (DeployRoomReq)message.getiRoomBody();
		RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoomEvent(req.getRoomId(), RoomTypeEnum.valueOf(req.getRoomType() + "")));
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return DeployRoomReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_DEPLOY_ROOM_REQ;
	}

}
