package com.spring.world.event;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.event.IRoomEvent;

public class ChooseRoomEvent implements IRoomEvent {

	private RoleInfo roleInfo;

	public RoleInfo getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
	
	
}
