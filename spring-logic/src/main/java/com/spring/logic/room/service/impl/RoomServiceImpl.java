package com.spring.logic.room.service.impl;

import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public int joinRoom(RoomTypeEnum roomTypeEnum, int roleId) {
		return cacheService.randomJoinRoom(roleId, roomTypeEnum);
	}
	
	@Override
	public boolean leaveRoom(int roomId, int roleId) {
		RoomInfo roomInfo = cacheService.queryRoom(roomId);
		
		if (roomInfo != null) {
			return cacheService.leaveRoom(roleId, roomInfo.getRoomId(), roomInfo.getRoomType());
		} else {
			logger.warn("role leave room failed " + roomId + "," + roleId);
		}
		
		return false;
	}
	
	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	@Override
	public boolean needDeployRoom(int roomId) {
		RoomInfo roomInfo = queryRoom(roomId);
		
		if (roomInfo == null) {
			return false;
		}
		
		synchronized (roomInfo) {
			return roomInfo.getCurRoomServerId() == 0;
		}
	}
	
	public void deployRoomAndSet(int roomId, int roomServerId, Function<RoomInfo, Void> function) {
		RoomInfo roomInfo = queryRoom(roomId);
		
		if (roomInfo == null) {
			return;
		}
		
		synchronized (roomInfo) {
			if (roomInfo.getCurRoomServerId() == 0) {
				roomInfo.setCurRoomServerId(roomServerId);
				function.apply(roomInfo);
			}
		}
	}
}
