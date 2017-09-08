package com.spring.logic.room.service;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

public interface CacheService {

	public RoomInfo queryRoom(int roomId);
	
	public RoomInfo randomJoinRoom(int roleId, RoomTypeEnum roomTypeEnum);
	
	public boolean leaveRoom(int roleId, int roomId, RoomTypeEnum roomTypeEnum);
	
	public void printAllRooms();
}
