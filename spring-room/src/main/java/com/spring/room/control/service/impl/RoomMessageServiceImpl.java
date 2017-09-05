package com.spring.room.control.service.impl;

import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.role.info.RoleInfo;
import com.spring.room.control.service.RoomMessageService;
import com.spring.room.io.process.join.JoinRoomResp;

@Service
public class RoomMessageServiceImpl implements RoomMessageService {

	@Override
	public Message createJoinRoomMsg(int roomId, int roleId, RoleInfo roleInfo) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setRoleId(roleId);
		head.setMsgType(GameMessageType.GAME_CLIENT_ROOM_JOIN);
		
		JoinRoomResp resp = new JoinRoomResp();
		resp.setGold(roleInfo.getGold());
		resp.setHeader(roleInfo.getHeader());
		resp.setRoleId(roleInfo.getRoleId());
		resp.setRoleName(roleInfo.getRoleName());
		resp.setRoomId(roomId);
		resp.setVipLevel(roleInfo.getVipLevel());
		
		return message;
	}
}
