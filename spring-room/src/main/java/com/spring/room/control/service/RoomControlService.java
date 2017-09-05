package com.spring.room.control.service;


import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;

public interface RoomControlService {

	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo);
	
	public PlayingRoomInfo deployRoomInfo(RoomInfo roomInfo);
	
	public int deployRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo);
	
	public int deployRoomInfoSuccessed(RoomInfo roomInfo);
	
	public int deployRoomInfoFailed(RoomInfo roomInfo);
	
	public int deployRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	public int deployRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	public void sendJoinRoomMsg(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo);
}
