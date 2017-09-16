package com.spring.room.io.process.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.RemoveRoleReq;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.event.RemoveRoleInfoEvent;

public class RoleRemoveProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoleRemoveProcessor.class);

	@Override
	public void processor(Message message) {
		RemoveRoleReq req = (RemoveRoleReq)message.getiRoomBody();
		RoomServerConfig.getRoomThread().addRoomEvent(new RemoveRoleInfoEvent(req));
		
		logger.info("start remove role " + req.getRoleId());
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return RemoveRoleReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_REMOVE_ROLE_REQ;
	}
}
