package com.spring.room.control.service;


import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;

public interface RoomControlService {

	public int loopRoomInfo(RoomInfo roomInfo);
	
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo);
	
	public int deployRoomInfoSuccessed(RoomInfo roomInfo);
	
	public int deployRoomInfoFailed(RoomInfo roomInfo);
}
