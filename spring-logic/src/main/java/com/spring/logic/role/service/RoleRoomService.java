package com.spring.logic.role.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

public interface RoleRoomService {

	public int roleJoinRoomCheck(RoleInfo roleInfo);
	
	public void roleAutoJoinRoom(RoleInfo roleInfo);
	
	public void roleChooseJoinRoom(RoleInfo roleInfo, RoomTypeEnum roomTypeEnum);
}
