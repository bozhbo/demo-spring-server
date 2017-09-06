package com.spring.room.control.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.RoomConfig;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.control.service.RoomControlService;
import com.spring.room.control.service.RoomLogicService;

/**
 * room-world合并实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomControlServiceAllImpl implements RoomControlService {
	
	private RoomLogicService roomLogicService;

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
		
		this.roomLogicService.sendJoinRoomMsg(playingRoomInfo, roleInfo);
		
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
	public void reportRoomServerInfo(int roomCount, int roleCount) {
		RoomServerInfo roomServerInfo = RoomServerCache.getRoomServerInfo(RoomServerConfig.ROOM_ID);
		
		if (roomServerInfo == null) {
			roomServerInfo = new RoomServerInfo();
			roomServerInfo.setRoomServerId(RoomServerConfig.ROOM_ID);
			RoomServerCache.addRoomServerInfo(roomServerInfo);
		}
		
		roomServerInfo.setRoleCount(roleCount);
		roomServerInfo.setRoomCount(roomCount);
	}
	
	@Autowired
	public void setRoomLogicService(RoomLogicService roomLogicService) {
		this.roomLogicService = roomLogicService;
	}
}
