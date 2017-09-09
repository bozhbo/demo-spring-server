package com.spring.logic.role.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.request.server.RemoveRoleReq;
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
	
	private RoleBusinessService roleLogicService;
	
	private MessageService messageService;
	
	private RoomServerService roomServerService;

	@Override
	public void autoJoin(RoleInfo roleInfo) {
		if (roleInfo.getRoomId() > 0) {
			// TODO deploy to room server
			return;
		}
		
		RoomTypeEnum roomTypeEnum = this.roleLogicService.getRoomType(roleInfo);
		
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
		
		DeployRoleReq deployRoleReq = this.roleLogicService.getDeployRoleMessage(roleInfo);
		
		if (deployRoleReq != null) {
			int roomServerId = roomService.getRoomServerId(roomId);
			
			if (roomServerId == 0) {
				messageService.createErrorMessage(740003, "");
				logger.warn("role join room failed for no room server");
				return;
			}
			
			this.roomServerService.deployRoleInfo(roomServerId, deployRoleReq);
		} else {
			messageService.createErrorMessage(740004, "");
		}
	}
	
	@Override
	public void leaveRoom(RoleInfo roleInfo) {
		if (roleInfo.getRoomId() == 0) {
			return;
		}

		RoomInfo roomInfo = this.roomService.queryRoom(roleInfo.getRoomId());
		
		if (roomInfo == null) {
			roleInfo.setRoomId(0);
			// TODO 刷新大厅数据
		}
		
		RemoveRoleReq removeRoleReq = new RemoveRoleReq();
		removeRoleReq.setRoleId(roleInfo.getRoleId());
		removeRoleReq.setRoomId(roleInfo.getRoomId());
		
		int roomServerId = roomService.getRoomServerId(roleInfo.getRoomId());
		
		// 离开RoomServer
		this.roomServerService.removeRoleInfo(roomServerId, removeRoleReq);
		
		// 等待离开房间回复消息后再离开WorldServer
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

	@Autowired
	public void setRoleLogicService(RoleBusinessService roleLogicService) {
		this.roleLogicService = roleLogicService;
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void setRoomServerService(RoomServerService roomServerService) {
		this.roomServerService = roomServerService;
	}
	
	
}
