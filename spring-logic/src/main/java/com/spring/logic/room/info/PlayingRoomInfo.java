package com.spring.logic.room.info;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomPlayingEnum;
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
	private RoomPlayingEnum roomState;
	private long lastUpdateTime;
	private long readyTime;
	private long sendCardTime;
	private int curGoldUnit;
	
	private RoomRoleInfo roomRoleInfo;
	
	private List<RoomRoleInfo> list = new ArrayList<>();
	private List<RoomRoleInfo> playingList = new ArrayList<>();
	
	public PlayingRoomInfo(int roomId, RoomTypeEnum roomType) {
		this.roomId = roomId;
		this.roomType = roomType;
		
		this.roomState = RoomPlayingEnum.ROOM_STATE_INIT;
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

	public List<RoomRoleInfo> getList() {
		return list;
	}

	public void setList(List<RoomRoleInfo> list) {
		this.list = list;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public RoomPlayingEnum getRoomState() {
		return roomState;
	}

	public void setRoomState(RoomPlayingEnum roomState) {
		this.roomState = roomState;
	}

	public List<RoomRoleInfo> getPlayingList() {
		return playingList;
	}

	public void setPlayingList(List<RoomRoleInfo> playingList) {
		this.playingList = playingList;
	}

	public long getReadyTime() {
		return readyTime;
	}

	public void setReadyTime(long readyTime) {
		this.readyTime = readyTime;
	}

	public RoomRoleInfo getRoomRoleInfo() {
		return roomRoleInfo;
	}

	public void setRoomRoleInfo(RoomRoleInfo roomRoleInfo) {
		this.roomRoleInfo = roomRoleInfo;
	}

	public long getSendCardTime() {
		return sendCardTime;
	}

	public void setSendCardTime(long sendCardTime) {
		this.sendCardTime = sendCardTime;
	}

	public int getCurGoldUnit() {
		return curGoldUnit;
	}

	public void setCurGoldUnit(int curGoldUnit) {
		this.curGoldUnit = curGoldUnit;
	}

	
}
