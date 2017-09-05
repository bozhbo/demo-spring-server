package com.spring.room.control.service;

import com.snail.mina.protocol.info.Message;
import com.spring.logic.role.info.RoleInfo;

public interface RoomMessageService {

	public Message createJoinRoomMsg(int roomId, int roleId, RoleInfo roleInfo);
}
