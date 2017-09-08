package com.spring.room.control.service;


import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.logic.room.info.RoomInfo;

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
	 * world要求移除房间
	 * 
	 * @param roomInfo
	 * @return
	 */
	public PlayingRoomInfo removeRoomInfo(RoomInfo roomInfo);
	
	/**
	 * world要求添加玩家
	 * 
	 * @param playingRoomInfo
	 * @param roleInfo
	 * @return
	 */
	public int deployRoleInfo(PlayingRoomInfo playingRoomInfo, RoleInfo roleInfo);
	
	/**
	 * world要求移除房间玩家
	 * 
	 * @param playingRoomInfo
	 * @param roleInfo
	 * @return
	 */
	public int removeRoleInfo(RoomInfo roomInfo, RoleInfo roleInfo);
	
	
}
