package com.spring.world.room.service.impl;

import org.springframework.stereotype.Service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.world.room.service.RoomClientService;

/**
 * 多进程实现
 * 
 * @author zhoubo
 *
 */
@Service
public class RoomClientServiceImpl implements RoomClientService {

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int deployRoomInfo(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoomInfo(RoomInfo roomInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int removeRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		return 0;
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
	public void autoJoin(RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveRoom(RoleInfo roleInfo) {
		// TODO Auto-generated method stub
		
	}

}
