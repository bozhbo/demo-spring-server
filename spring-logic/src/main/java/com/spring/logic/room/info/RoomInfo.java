package com.spring.logic.room.info;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.room.enums.RoomTypeEnum;

/**
 * 用于管理房间对象
 * 
 * @author zhoubo
 *
 */
public class RoomInfo {

	private int roomId;
	private RoomTypeEnum roomType;
	private int roomState; // 0-未使用 1-使用中
	
	private int curRoomServerId; // 当前的房间服务器Id
	
	private List<Integer> list = new ArrayList<>();
	
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
	
	public int getCurRoomServerId() {
		return curRoomServerId;
	}

	public void setCurRoomServerId(int curRoomServerId) {
		this.curRoomServerId = curRoomServerId;
	}

	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Room:").append(roomId).append(",Type:").append(roomType.toString()).append(",State:").append(roomState).append(",Roles:").append(list.size());
		
		return buffer.toString();
	}
}
