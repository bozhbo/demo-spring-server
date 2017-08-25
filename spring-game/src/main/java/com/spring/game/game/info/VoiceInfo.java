package com.snail.webgame.game.info;

import java.sql.Timestamp;

import com.snail.webgame.engine.common.to.BaseTO;

/**
 * 
 * 类介绍:语音持久化对象
 *
 * @author zhoubo
 * @2015年7月31日
 */
public class VoiceInfo extends BaseTO {

	private long roomId;
	private String roomName;
	private byte roomType;
	private Timestamp createTime;
	private String roomDesc;
	
	public long getRoomId() {
		return roomId;
	}
	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public byte getRoomType() {
		return roomType;
	}
	public void setRoomType(byte roomType) {
		this.roomType = roomType;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getRoomDesc() {
		return roomDesc;
	}
	public void setRoomDesc(String roomDesc) {
		this.roomDesc = roomDesc;
	}
	
	@Override
	public byte getSaveMode() {
		return ONLINE;
	}
	
	
}
