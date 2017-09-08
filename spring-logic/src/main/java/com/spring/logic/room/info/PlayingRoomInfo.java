package com.spring.logic.room.info;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

/**
 * 用于房间服务器运行对象
 * 
 * @author zhoubo
 *
 */
public class PlayingRoomInfo {

	private int roomId;
	private RoomTypeEnum roomType;
	
	private List<RoleInfo> list = new ArrayList<>();
	
	public PlayingRoomInfo(int roomId, RoomTypeEnum roomType) {
		this.roomId = roomId;
		this.roomType = roomType;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public RoomTypeEnum getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeEnum roomType) {
		this.roomType = roomType;
	}

	public List<RoleInfo> getList() {
		return list;
	}

	public void setList(List<RoleInfo> list) {
		this.list = list;
	}

	
}
