package com.spring.logic.room.service.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.cache.RoomCahce;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.room.service.CacheService;

public class CacheServiceImpl implements CacheService {

	public RoomInfo createRoom(RoomTypeEnum roomTypeEnum) {
		RoomInfo roomInfo = null;
		Map<Integer, RoomInfo> emptyRoomMap = RoomCahce.getEmptyRoomMap();
		
		if (emptyRoomMap.size() > 0) {
			Set<Entry<Integer, RoomInfo>> set = emptyRoomMap.entrySet();
			
			for (Entry<Integer, RoomInfo> entry : set) {
				roomInfo = entry.getValue();
				emptyRoomMap.remove(entry.getKey());
				break;
			}
		}
		
		if (roomInfo == null) {
			if (RoomCahce.getAllRoomMap().size() > RoomConfig.ROOM_MAX_COUNT) {
				return null;
			}
			
			roomInfo = new RoomInfo(RoomConfig.createRoomId());
			RoomCahce.getAllRoomMap().put(roomInfo.getRoomId(), roomInfo);
		}
		
		roomInfo.setRoomType(roomTypeEnum);
		roomInfo.getList().clear();
		roomInfo.setRoomState(0);
		
		RoomCahce.getPlayingRoomMap().get(roomTypeEnum).put(roomInfo.getRoomId(), roomInfo);
		
		return roomInfo;
	}
	
	public void closeRoom(RoomInfo roomInfo) {
		roomInfo.getList().clear();
		roomInfo.setRoomState(0);
		
		RoomCahce.getPlayingRoomMap().get(roomInfo.getRoomState()).remove(roomInfo.getRoomId());
		
		RoomCahce.getEmptyRoomMap().put(roomInfo.getRoomId(), roomInfo);
	}
	
	public RoomInfo queryRoom(int roomId) {
		return RoomCahce.getAllRoomMap().get(roomId);
	}
	
	public int getPlayingRoomSize(RoomTypeEnum roomTypeEnum) {
		return RoomCahce.getPlayingRoomMap().get(roomTypeEnum).size();
	}
}
