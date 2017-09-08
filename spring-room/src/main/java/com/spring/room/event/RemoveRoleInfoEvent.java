package com.spring.room.event;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.event.IRoomEvent;
import com.spring.logic.room.info.RoomInfo;

public class RemoveRoleInfoEvent implements IRoomEvent {

	private RoomInfo roomInfo;
	private RoleInfo roleInfo;
	
	public RemoveRoleInfoEvent(RoomInfo roomInfo, RoleInfo roleInfo) {
		this.roomInfo = roomInfo;
		this.roleInfo = roleInfo;
	}
	
	public RoomInfo getRoomInfo() {
		return roomInfo;
	}
	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}
	public RoleInfo getRoleInfo() {
		return roleInfo;
	}
	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}
	
	
}
