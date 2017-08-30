package com.spring.world.room.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;

public interface RoomClientService {

	public int deployRoomInfo(RoomInfo roomInfo);
	
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo);
}
