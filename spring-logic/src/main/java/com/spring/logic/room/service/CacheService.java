package com.spring.logic.room.service;

import java.util.List;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

/**
 * 房间缓存管理Service
 * 提供统一的内存管理,支持多线程访问
 * 
 * @author Administrator
 *
 */
public interface CacheService {

	public RoomInfo queryRoom(int roomId);
	
	public int randomJoinRoom(int roleId, RoomTypeEnum roomTypeEnum);
	
	public boolean leaveRoom(int roleId, int roomId, RoomTypeEnum roomTypeEnum);
	
	public void closeRoom(int roomId);
	
	public List<Integer> getAllRoles(int roomId);
	
	public void printAllRooms();
}
