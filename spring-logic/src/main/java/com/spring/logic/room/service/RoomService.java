package com.spring.logic.room.service;

import java.util.function.Function;

import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.RoomInfo;

/**
 * 房间管理Service
 * 操作单位为RoomInfo
 * 
 * @author Administrator
 *
 */
public interface RoomService {

	public RoomInfo queryRoom(int roomId);
	
	public int joinRoom(RoomTypeEnum roomTypeEnum, int roleId);
	
	public boolean leaveRoom(int roomId, int roleId);
	
	public boolean needDeployRoom(int roomId);
	
	public void deployRoomAndSet(int roomId, int roomServerId, Function<RoomInfo, Integer> function);
	
	public int getRoomServerId(int roomId);
	
	public void closeRoom(int roomId);

}
