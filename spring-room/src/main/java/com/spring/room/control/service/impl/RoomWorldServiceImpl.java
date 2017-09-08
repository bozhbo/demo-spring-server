package com.spring.room.control.service.impl;

import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.control.service.RoomWorldService;

@Service
public class RoomWorldServiceImpl implements RoomWorldService {

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
	public int removeRoomInfoSuccessed(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoomInfoFailed(RoomInfo roomInfo) {
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
	public int removeRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo) {
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

}
