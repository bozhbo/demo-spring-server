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
import com.spring.logic.server.cache.RoomManageServerCache;
import com.spring.logic.server.service.RoomServerService;

@Service
public class RoleRoomServiceImpl implements RoleRoomService {
	
	private static final Log logger = LogFactory.getLog(RoleRoomServiceImpl.class);
	
	private RoomService roomService;
	
	private MessageService messageService;
	
	private RoomServerService roomServerService;

	@Override
	public void joinRoom(RoleInfo roleInfo, RoomTypeEnum roomTypeEnum, DeployRoleReq deployRoleReq) {
		if (roleInfo.getRoomId() > 0) {
			// 出现已经存在roomId(1.第一次发送后客户端继续点击 2.第一次发送未成功 3.room部署结果未返回)
			deployRole(roleInfo.getRoomId(), roleInfo, deployRoleReq);
			return;
		}
		
		int roomId = roomService.joinRoom(roomTypeEnum, roleInfo.getRoleId());
		
		if (roomId == 0) {
			messageService.sendGateMessage(roleInfo.getGateId(), messageService.createErrorMessage(roleInfo.getRoleId(), 740002, ""));
			logger.warn("role join room failed for no room");
			return;
		}
		
		if (roomService.needDeployRoom(roomId)) {
			int roomServerId = this.roomServerService.GetFitRoomServerId();
			
			if (roomServerId == 0) {
				logger.warn("role join room failed for get room server error");
				
				roomService.leaveRoom(roomId, roleInfo.getRoleId());
				messageService.sendGateMessage(roleInfo.getGateId(), messageService.createErrorMessage(roleInfo.getRoleId(), 740003, ""));
				
				return;
			}
			
			roomService.deployRoomAndSet(roomId, roomServerId, (t) -> {roomServerService.deployRoomInfo(roomServerId, roomId, roomTypeEnum); return 1;});
		
			// 加入管理缓存
			RoomManageServerCache.addRoomId(roomServerId, roomId);
		}
		
		if (deployRoleReq != null) {
			if (!deployRole(roomId, roleInfo, deployRoleReq)) {
				roomService.leaveRoom(roomId, roleInfo.getRoleId());
			} else {
				// 预先设置房间号
				roleInfo.setRoomId(roomId);
			}
		} else {
			messageService.sendGateMessage(roleInfo.getGateId(), messageService.createErrorMessage(roleInfo.getRoleId(), 740003, ""));
		}
	}
	
	private boolean deployRole(int roomId, RoleInfo roleInfo, DeployRoleReq deployRoleReq) {
		int roomServerId = roomService.getRoomServerId(roomId);
		
		if (roomServerId == 0) {
			messageService.sendGateMessage(roleInfo.getGateId(), messageService.createErrorMessage(roleInfo.getRoleId(), 740003, ""));
			logger.warn("role join room failed for no room server");
			return false;
		}
		
		deployRoleReq.setRoomId(roomId);
		
		return roomServerService.deployRoleInfo(roomServerId, deployRoleReq);
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
		roomServerService.removeRoleInfo(roomServerId, removeRoleReq);
		
		roleInfo.setRoomId(0);
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
