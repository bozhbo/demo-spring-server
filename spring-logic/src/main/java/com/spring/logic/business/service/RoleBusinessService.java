package com.spring.logic.business.service;

import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

public interface RoleBusinessService {

	public RoomTypeEnum getRoomType(RoleInfo roleInfo);
	
	public DeployRoleReq getDeployRoleMessage(RoleInfo roleInfo);
	
}
