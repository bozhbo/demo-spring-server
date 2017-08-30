package com.spring.logic.room.service;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

public interface CacheService {

	public RoomInfo createRoom(RoomTypeEnum roomTypeEnum);
	
	public void closeRoom(RoomInfo roomInfo);
	
	public RoomInfo queryRoom(int roomId);
	
	public int getPlayingRoomSize(RoomTypeEnum roomTypeEnum);
	
	public RoomInfo randomPlayingRoom(RoomTypeEnum roomTypeEnum);
}
