package com.spring.logic.server.service;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.request.server.RemoveRoleReq;
import com.spring.logic.room.enums.RoomTypeEnum;

public interface RoomServerService {

	public int GetFitRoomServerId();
	
	public boolean deployRoomInfo(int roomServerId, int roomId, RoomTypeEnum roomTypeEnum);
	
	public boolean deployRoleInfo(int roomServerId, DeployRoleReq deployRoleReq);
	
	public boolean removeRoleInfo(int roomServerId, RemoveRoleReq removeRoleReq);
}
