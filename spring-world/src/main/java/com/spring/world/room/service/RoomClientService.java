package com.spring.world.room.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.server.info.RoomServerInfo;

public interface RoomClientService {
	
	public void init();

	public int deployRoomInfo(RoomInfo roomInfo);
	
	public int sendRoomInfo(RoomInfo roomInfo, RoomServerInfo roomServerInfo);
	
	public int deployRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo);
	
	public void roomServerRegister(int roomServerId);
	
	public void roomServerClose(int roomServerId);
	
	public void roomInfo(String info);
	
	/**
	 * 快速开始
	 * 
	 * @param roleInfo
	 */
	public void autoJoin(RoleInfo roleInfo);
}
