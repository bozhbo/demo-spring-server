package com.spring.room.control.service.impl;

import org.springframework.stereotype.Service;

import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.control.service.RoomWorldService;

@Service
public class RoomWorldServiceImpl implements RoomWorldService {

	@Override
	public int deployRoomInfoSuccessed(int roomId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoomInfoFailed(int roomId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoomInfoSuccessed(int roomId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoomInfoFailed(int roomId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoSuccessed(int roomId, int roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfoFailed(int roomId, int roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfoSuccessed(int roomId, int roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfoFailed(int roomId, int roleId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void reportRoomServerInfo(int roomCount, int roleCount) {
		RoomServerInfo roomServerInfo = RoomServerCache.getRoomServerInfo(RoomServerConfig.ROOM_SERVER_ID);
		
		if (roomServerInfo == null) {
			roomServerInfo = new RoomServerInfo();
			roomServerInfo.setRoomServerId(RoomServerConfig.ROOM_SERVER_ID);
			RoomServerCache.addRoomServerInfo(roomServerInfo);
		}
		
		roomServerInfo.setRoleCount(roleCount);
		roomServerInfo.setRoomCount(roomCount);
	}
}
