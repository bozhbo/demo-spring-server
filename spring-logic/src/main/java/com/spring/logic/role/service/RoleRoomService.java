package com.spring.logic.role.service;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

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
	public void autoJoin(RoleInfo roleInfo, RoomTypeEnum roomTypeEnum, DeployRoleReq deployRoleReq);
	
	/**
	 * 离开房间
	 * 
	 * @param roleInfo
	 */
	public void leaveRoom(RoleInfo roleInfo);
	
	/**
	 * 刷新大厅数据
	 * 
	 * @param roleInfo
	 */
	public void refreshSceneInfo(RoleInfo roleInfo);
}
