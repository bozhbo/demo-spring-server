package com.spring.room.control.service;


import java.util.function.Function;
import java.util.function.Supplier;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;
import com.spring.logic.server.info.RoomServerInfo;

public interface RoomControlService {

	/**
	 * 房间线程主循环
	 * 
	 * @param playingRoomInfo
	 * @return
	 */
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo);
	
	/**
	 * world要求添加房间
	 * 
	 * @param roomInfo
	 * @return
	 */
	public PlayingRoomInfo deployRoomInfo(RoomInfo roomInfo);
	
	/**
	 * world要求添加玩家
	 * 
	 * @param playingRoomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo);
	
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
	 * 发送房间服务器信息
	 */
	public void reportRoomServerInfo(int roomCount, int roleCount);
}
