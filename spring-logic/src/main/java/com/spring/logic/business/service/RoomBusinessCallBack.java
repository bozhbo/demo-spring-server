package com.spring.logic.business.service;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomBusinessCallBack {

	public int roomRoleOnRecover(RoomRoleInfo roomRoleInfo, DeployRoleReq req);
	
	public int roomRoleOnAdd(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	public int roomRoleOnRemove(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
}
