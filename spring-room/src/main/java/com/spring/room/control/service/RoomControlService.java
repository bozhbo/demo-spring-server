package com.spring.room.control.service;


import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomControlService {

	/**
	 * 房间线程主循环
	 * 
	 * @param playingRoomInfo
	 * @return
	 */
	public int loopRoomInfo(PlayingRoomInfo playingRoomInfo, long curTime);
	
	/**
	 * 准备
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void ready(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	/**
	 * 弃牌
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void giveUp(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	/**
	 * 跟注
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void follow(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	/**
	 * 加注
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void add(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str);
	
	/**
	 * 看牌
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void look(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	/**
	 * 比牌
	 * 
	 * @param playingRoomInfo
	 * @param roleId
	 */
	public void compare(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo, String str);
}
