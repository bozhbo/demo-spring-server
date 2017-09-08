package com.spring.room.control.service;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.RoomInfo;

public interface RoomWorldService {
	
	/**
	 * 返回world添加房间成功
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int deployRoomInfoSuccessed(RoomInfo roomInfo);
	
	/**
	 * 返回world添加房间失败
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int deployRoomInfoFailed(RoomInfo roomInfo);
	
	/**
	 * 返回world移除房间成功
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int removeRoomInfoSuccessed(RoomInfo roomInfo);
	
	/**
	 * 返回world移除房间失败
	 * 
	 * @param roomInfo
	 * @return
	 */
	public int removeRoomInfoFailed(RoomInfo roomInfo);
	
	/**
	 * 返回world添加玩家成功
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	/**
	 * 返回world添加玩家失败
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	/**
	 * 返回world移除玩家成功
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int removeRoleInfoSuccessed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	/**
	 * 返回world移除玩家失败
	 * 
	 * @param roomInfo
	 * @param roleInfo
	 * @return
	 */
	public int removeRoleInfoFailed(RoomInfo roomInfo, RoleInfo roleInfo);
	
	/**
	 * 发送房间服务器信息
	 */
	public void reportRoomServerInfo(int roomCount, int roleCount);
}
