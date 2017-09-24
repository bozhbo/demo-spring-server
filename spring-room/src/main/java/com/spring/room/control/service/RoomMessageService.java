package com.spring.room.control.service;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomMessageService {

	public void send2AllRoles(PlayingRoomInfo playingRoomInfo, RoomMessageHead roomMessageHead, IRoomBody roomBody);
	
	public void send2AllRolesExcept(PlayingRoomInfo playingRoomInfo, RoomRoleInfo exceptRoomRoleInfo, RoomMessageHead roomMessageHead, IRoomBody roomBody);
}
