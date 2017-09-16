package com.spring.logic.role.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.request.server.RemoveRoleReq;
import com.spring.logic.message.request.world.init.InitSceneResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleRoomService;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.RoomService;
import com.spring.logic.server.service.RoomServerService;

@Service
public class RoleRoomServiceImpl implements RoleRoomService {
	
	private static final Log logger = LogFactory.getLog(RoleRoomServiceImpl.class);
	
	private RoomService roomService;
	
	private MessageService messageService;
	
	private RoomServerService roomServerService;

	@Override
	public void autoJoin(RoleInfo roleInfo, RoomTypeEnum roomTypeEnum, DeployRoleReq deployRoleReq) {
		if (roleInfo.getRoomId() > 0) {
			// TODO deploy to room server
			return;
		}
		
		int roomId = this.roomService.joinRoom(roomTypeEnum, roleInfo.getRoleId());
		
		if (roomId == 0) {
			messageService.createErrorMessage(740002, "");
			logger.warn("role join room failed for no room");
			return;
		}
		
		if (this.roomService.needDeployRoom(roomId)) {
			int roomServerId = this.roomServerService.GetFitRoomServerId();
			
			if (roomServerId == 0) {
				messageService.createErrorMessage(740003, "");
				logger.warn("role join room failed for get room server error");
			}
			
			this.roomService.deployRoomAndSet(roomId, roomServerId, (t) -> {roomServerService.deployRoomInfo(roomServerId, roomId, roomTypeEnum); return 1;});
		}
		
		if (deployRoleReq != null) {
			int roomServerId = roomService.getRoomServerId(roomId);
			
			if (roomServerId == 0) {
				messageService.createErrorMessage(740003, "");
				logger.warn("role join room failed for no room server");
				return;
			}
			
			deployRoleReq.setRoomId(roomId);
			
			this.roomServerService.deployRoleInfo(roomServerId, deployRoleReq);
		} else {
			messageService.createErrorMessage(740004, "");
		}
	}
	
	@Override
	public void leaveRoom(RoleInfo roleInfo) {
		RoomInfo roomInfo = this.roomService.queryRoom(roleInfo.getRoomId());
		
		if (roomInfo == null) {
			roleInfo.setRoomId(0);
			refreshSceneInfo(roleInfo);
			return;
		}
		
		RemoveRoleReq removeRoleReq = new RemoveRoleReq();
		removeRoleReq.setRoleId(roleInfo.getRoleId());
		removeRoleReq.setRoomId(roleInfo.getRoomId());
		
		int roomServerId = roomService.getRoomServerId(roleInfo.getRoomId());
		
		// 离开RoomServer
		this.roomServerService.removeRoleInfo(roomServerId, removeRoleReq);
		
		// 等待离开房间回复消息后再离开WorldServer
	}
	
	@Override
	public void refreshSceneInfo(RoleInfo roleInfo) {
		InitSceneResp resp = new InitSceneResp();
		resp.setOnline(10);
		
		// 刷新大厅数据
		Message message = messageService.createMessage(roleInfo.getRoleId(), GameMessageType.GAME_CLIENT_WORLD_SCENE_INIT_RECEIVE, 0, "", resp);
		messageService.sendGateMessage(roleInfo.getGateId(), message);
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	@Autowired
	public void setRoomServerService(RoomServerService roomServerService) {
		this.roomServerService = roomServerService;
	}

	
	
	
}
