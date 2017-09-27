package com.spring.room.control.service;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.room.info.PlayingRoomInfo;

public interface RoomLogicService {

	public PlayingRoomInfo createPlayingRoomInfo(int roomId, RoomTypeEnum roomTypeEnum);
	
	public RoomRoleInfo createRoomRoleInfo(PlayingRoomInfo playingRoomInfo, DeployRoleReq req);
	
	public void addRole(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
	public void removeRole(PlayingRoomInfo playingRoomInfo, int roleId);
	
	public String getRespRoomInfo(PlayingRoomInfo playingRoomInfo);
	
	public void sendRoomSyncMessage(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo);
	
}
