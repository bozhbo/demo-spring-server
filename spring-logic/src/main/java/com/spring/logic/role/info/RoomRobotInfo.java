package com.spring.logic.role.info;

public class RoomRobotInfo extends RoleInfo {

	/**
	 * 房间服务器最后一次回传信息时间
	 */
	private long lastUpdateTime;
	
	/**
	 * 发送到房间服务器的时间
	 */
	private long sendToRoomTime;

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long getSendToRoomTime() {
		return sendToRoomTime;
	}

	public void setSendToRoomTime(long sendToRoomTime) {
		this.sendToRoomTime = sendToRoomTime;
	}
	
	
}
