package com.spring.room.control.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomMessageService;

@Service
public class RoomControlServiceAllImpl implements RoomControlService {
	
	private RoomMessageService roomMessageService;

	@Override
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public PlayingRoomInfo deployRoomInfo(RoomInfo roomInfo) {
		RoomInfo newRoomInfo = new RoomInfo(roomInfo.getRoomId());
		newRoomInfo.setRoomType(roomInfo.getRoomType());
		
		PlayingRoomInfo playingRoomInfo = new PlayingRoomInfo(newRoomInfo);
		
		return playingRoomInfo;
	}

	@Override
	public int deployRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo) {
		if (playingRoomInfo.getRoomInfo().getList().size() >= RoomConfig.ROOM_MAX_ROLES) {
			return 0;
		}
		
		playingRoomInfo.getRoomInfo().getList().add(roleInfo);
		
		sendJoinRoomMsg(playingRoomInfo, roleInfo);
		
		return 1;
	}

	@Override
	public int deployRoomInfoSuccessed(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoomInfoFailed(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void sendJoinRoomMsg(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo) {
		List<RoleInfo> list = playingRoomInfo.getRoomInfo().getList();
		
		for (RoleInfo roleInfo2 : list) {
			Message message = this.roomMessageService.createJoinRoomMsg(playingRoomInfo.getRoomInfo().getRoomId(), roleInfo2.getRoleId(), roleInfo);
		}
	}

	@Autowired
	public void setRoomMessageService(RoomMessageService roomMessageService) {
		this.roomMessageService = roomMessageService;
	}

}
