package com.spring.logic.room.info;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

public class RoomInfo {

	private int roomId;
	private RoomTypeEnum roomType;
	private int roomState; // 0-empty
	
	private List<RoleInfo> list = new ArrayList<>();
	
	public RoomInfo(int roomId) {
		this.roomId = roomId;
		this.roomState = 0;
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
	public int getRoomState() {
		return roomState;
	}
	public void setRoomState(int roomState) {
		this.roomState = roomState;
	}

	public List<RoleInfo> getList() {
		return list;
	}

	public void setList(List<RoleInfo> list) {
		this.list = list;
	}
	
	
}
