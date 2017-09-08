package com.spring.logic.room.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.CacheService;
import com.spring.logic.room.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	
	private static final Log logger = LogFactory.getLog(RoomServiceImpl.class);
	
	private CacheService cacheService;
	
	public RoomInfo queryRoom(int roomId) {
		return this.cacheService.queryRoom(roomId);
	}
	
	public RoomInfo randomJoinRoom(RoomTypeEnum roomTypeEnum, RoleInfo roleInfo) {
		RoomInfo roomInfo = cacheService.randomJoinRoom(roleInfo.getRoleId(), roomTypeEnum);
		
		if (roomInfo != null) {
			roleInfo.setRoomId(roomInfo.getRoomId());
			return roomInfo;
		}
		
		logger.warn("role join room failed");
		
		return null;
	}
	
	@Override
	public void leaveRoom(RoleInfo roleInfo) {
		RoomInfo roomInfo = cacheService.queryRoom(roleInfo.getRoomId());
		
		if (roomInfo != null) {
			cacheService.leaveRoom(roleInfo.getRoleId(), roomInfo.getRoomId(), roomInfo.getRoomType());
		} else {
			logger.warn("role leave room failed " + roleInfo.getRoomId());
		}
	}
	
	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
}
