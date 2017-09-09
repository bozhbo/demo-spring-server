package com.spring.logic.server.service.impl;

import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.request.server.DeployRoomReq;
import com.spring.logic.message.request.server.RemoveRoleReq;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.server.service.RoomServerService;

public class RoomServerServiceImpl implements RoomServerService {
	
	private MessageService messageService;

	@Override
	public int GetFitRoomServerId() {
		Set<Entry<Integer, RoomServerInfo>> set = RoomServerCache.getSet();
		RoomServerInfo roomServerInfo = null;
		int minRoleCount = Integer.MAX_VALUE;
		
		for (Entry<Integer, RoomServerInfo> entry : set) {
			if (entry.getValue().getSession() == null || !entry.getValue().getSession().isConnected()) {
				continue;
			}
			
			if (entry.getValue().getRoleCount() < 100) {
				return entry.getValue().getRoomServerId();
			} else {
				if (minRoleCount > entry.getValue().getRoleCount()) {
					roomServerInfo = entry.getValue();
					minRoleCount = entry.getValue().getRoleCount();
				}
			}
		}
		
		if (roomServerInfo != null) {
			return roomServerInfo.getRoomServerId();
		}
		
		return 0;
	}

	@Override
	public boolean deployRoomInfo(int roomServerId, int roomId, RoomTypeEnum roomTypeEnum) {
		DeployRoomReq req = new DeployRoomReq();
		req.setRoomId(roomId);
		req.setRoomType(roomTypeEnum.getValue());
		
		return this.messageService.sendRoomMessage(roomServerId, GameMessageType.WORLD_2_ROOM_DEPLOY_ROOM, req);
	}
	
	@Override
	public boolean deployRoleInfo(int roomServerId, DeployRoleReq deployRoleReq) {
		return this.messageService.sendRoomMessage(roomServerId, GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE, deployRoleReq);
	}
	
	@Override
	public boolean removeRoleInfo(int roomServerId, RemoveRoleReq removeRoleReq) {
		return this.messageService.sendRoomMessage(roomServerId, GameMessageType.WORLD_2_ROOM_REMOVE_ROLE, removeRoleReq);
	}
	
	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
