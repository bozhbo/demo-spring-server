package com.spring.logic.room.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

public interface RoomService {

	public RoomInfo queryRoom(int roomId);
	
	public RoomInfo randomJoinRoom(RoomTypeEnum roomTypeEnum, RoleInfo roleInfo);
	
	public void leaveRoom(RoleInfo roleInfo);
	
}
