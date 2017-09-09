package com.spring.logic.role.service;

import com.spring.logic.role.info.RoleInfo;

/**
 * 角色房间请求Service
 * 
 * @author Administrator
 *
 */
public interface RoleRoomService {

	/**
	 * 快速开始
	 * 
	 * @param roleInfo
	 */
	public void autoJoin(RoleInfo roleInfo);
	
	/**
	 * 离开房间
	 * 
	 * @param roleInfo
	 */
	public void leaveRoom(RoleInfo roleInfo);
}
