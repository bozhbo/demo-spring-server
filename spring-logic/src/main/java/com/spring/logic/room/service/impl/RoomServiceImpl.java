package com.spring.logic.room.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.CacheService;
import com.spring.logic.room.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {
	
	private static final Log logger = LogFactory.getLog(RoomServiceImpl.class);
	
	private CacheService cacheService;
	
	@Override
	public void roomResize(Map<RoomTypeEnum, Integer> roleCountMap, Function<RoomInfo, Integer> deployRoomInfo) {
		Set<Entry<RoomTypeEnum, Integer>> set = roleCountMap.entrySet();
		
		for (Entry<RoomTypeEnum, Integer> entry : set) {
			int playingRooms = this.cacheService.getPlayingRoomSize(entry.getKey());
			
			if (playingRooms < 10) {
				// no room
				RoomInfo roomInfo = this.cacheService.createRoom(entry.getKey());
				
				if (deployRoomInfo.apply(roomInfo) != 1) {
					this.cacheService.closeRoom(roomInfo);
				}
			} else {
				if (roleCountMap.get(entry.getKey()) * 1.0 / (playingRooms * RoomConfig.ROOM_MAX_ROLES) > 0.7) {
					RoomInfo roomInfo = this.cacheService.createRoom(entry.getKey());
					
					if (roomInfo != null && deployRoomInfo.apply(roomInfo) != 1) {
						this.cacheService.closeRoom(roomInfo);
					}
				}
			}
		}
	}
	
	@Override
	public void closeRoom(RoomInfo roomInfo) {
		this.cacheService.closeRoom(roomInfo);
	}
	
	public RoomInfo randomJoinRoom(RoomTypeEnum roomTypeEnum, RoleInfo roleInfo) {
		if (roleInfo.getRoomId() > 0) {
			logger.warn("role is in room " + roleInfo.getRoomId());
			return null;
		}
		
		RoomInfo roomInfo = cacheService.randomPlayingRoom(roomTypeEnum);
		
		if (roomInfo != null) {
			roleInfo.setRoomId(roomInfo.getRoomId());
			return roomInfo;
		}
		
		return null;
	}
	
	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
}