package com.spring.logic.room.service;

import java.util.Map;
import java.util.function.Function;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

public interface RoomService {

	public void roomResize(Map<RoomTypeEnum, Integer> roleCountMap, Function<RoomInfo, Integer> deployRoomInfo);
	
	public void closeRoom(RoomInfo roomInfo);
	
	public RoomInfo randomJoinRoom(RoomTypeEnum roomTypeEnum, RoleInfo roleInfo);
	
}
