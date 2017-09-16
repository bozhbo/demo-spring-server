package com.spring.room.io.process.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.BaseProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.event.DeployRoleInfoEvent;

public class RoleDeployProcessor extends BaseProcessor {
	
	private static final Log logger = LogFactory.getLog(RoleDeployProcessor.class);
	
	public RoleDeployProcessor() {
		super(DeployRoleReq.class);
	}
	
	public RoleDeployProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		DeployRoleReq req = (DeployRoleReq)message.getiRoomBody();
		RoomServerConfig.getRoomThread().addRoomEvent(new DeployRoleInfoEvent(req));
	
		logger.info("start deploy role " + req.getRoleId());
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_REQ;
	}
}
