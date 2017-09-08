package com.spring.room.control.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.server.service.ServerMessageService;
import com.spring.room.control.service.RoomLogicService;
import com.spring.room.control.service.RoomMessageService;

@Service
public class RoomLogicServiceImpl implements RoomLogicService {
	
	private RoomMessageService roomMessageService;
	
	private ServerMessageService serverMessageService;

	@Override
	public void sendJoinRoomMsg(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo) {
		List<RoleInfo> list = playingRoomInfo.getList();
		
		for (RoleInfo roleInfo2 : list) {
			Message message = this.roomMessageService.createJoinRoomMsg(playingRoomInfo.getRoomId(), roleInfo2.getRoleId(), roleInfo);
			this.serverMessageService.sendMessageByGateId(roleInfo2.getGateId(), message);
		}
	}
	
	@Autowired
	public void setRoomMessageService(RoomMessageService roomMessageService) {
		this.roomMessageService = roomMessageService;
	}

	@Autowired
	public void setServerMessageService(ServerMessageService serverMessageService) {
		this.serverMessageService = serverMessageService;
	}
}
