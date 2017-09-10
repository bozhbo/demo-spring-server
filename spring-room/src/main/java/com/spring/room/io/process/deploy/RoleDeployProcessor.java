package com.spring.room.io.process.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleReq;

public class RoleDeployProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoleDeployProcessor.class);

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		DeployRoleReq req = (DeployRoleReq)message.getiRoomBody();
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return DeployRoleReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_REQ;
	}

}
