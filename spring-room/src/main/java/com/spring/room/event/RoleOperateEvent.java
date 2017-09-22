package com.spring.room.event;

import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.event.IRoomEvent;

public class RoleOperateEvent implements IRoomEvent {

	private int msgType;
	private int roomId;
	private RoomRoleInfo roomRoleInfo;
	private String optionStr;
	
	public RoleOperateEvent(int roomId, int msgType, String optionStr, RoomRoleInfo roomRoleInfo) {
		this.roomId = roomId;
		this.msgType = msgType;
		this.optionStr = optionStr;
		this.roomRoleInfo = roomRoleInfo;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getOptionStr() {
		return optionStr;
	}

	public void setOptionStr(String optionStr) {
		this.optionStr = optionStr;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public RoomRoleInfo getRoomRoleInfo() {
		return roomRoleInfo;
	}

	public void setRoomRoleInfo(RoomRoleInfo roomRoleInfo) {
		this.roomRoleInfo = roomRoleInfo;
	}
	
	
}
