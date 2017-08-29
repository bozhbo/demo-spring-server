package com.spring.logic.room.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.CacheService;
import com.spring.logic.room.service.RoomService;

public class RoomServiceImpl implements RoomService {
	
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
	
	@Autowired
	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}
}
