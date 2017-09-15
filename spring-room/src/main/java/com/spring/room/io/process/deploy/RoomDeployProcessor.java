package com.spring.room.io.process.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoomReq;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.event.DeployRoomEvent;

public class RoomDeployProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoomDeployProcessor.class);
	
	@Override
	public void processor(Message message) {
		DeployRoomReq req = (DeployRoomReq)message.getiRoomBody();
		RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoomEvent(req.getRoomId(), RoomTypeEnum.transfer(req.getRoomType())));
	
		logger.info("start deploy room " + req.getRoomId());
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
