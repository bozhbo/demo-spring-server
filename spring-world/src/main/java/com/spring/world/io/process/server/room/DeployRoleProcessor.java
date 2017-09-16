package com.spring.world.io.process.server.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleResp;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.service.RoomService;

@Component
public class DeployRoleProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(DeployRoleProcessor.class);

	private RoomService roomService;
	
	@Override
	public void processor(Message message) {
		DeployRoleResp req = (DeployRoleResp)message.getiRoomBody();
		
		RoleInfo roleInfo = RoleCache.getRoleInfo(req.getRoleId());
		
		if (roleInfo == null) {
			logger.error("role is not exist " + req.getRoleId());
			return;
		}
		
		synchronized (roleInfo) {
			if (req.getResult() != 1) {
				// 角色添加失败
				logger.warn("deploy role failed " + req.getRoleId());
				roomService.leaveRoom(req.getRoomId(), req.getRoleId());
			} else {
				// 角色添加成功
				roleInfo.setRoomId(req.getRoomId());
				logger.info("deploy role to room success " + req.getRoleId());
			}
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return DeployRoleResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_RESP;
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}
	
}
