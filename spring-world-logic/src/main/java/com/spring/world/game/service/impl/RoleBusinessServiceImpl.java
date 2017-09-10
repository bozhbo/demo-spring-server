package com.spring.world.game.service.impl;

import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

public class RoleBusinessServiceImpl implements RoleBusinessService {

	@Override
	public RoomTypeEnum getRoomType(RoleInfo roleInfo) {
		return RoomTypeEnum.ROOM_TYPE_NEW;
	}

	@Override
	public DeployRoleReq getDeployRoleMessage(RoleInfo roleInfo) {
		return new DeployRoleReq();
	}

}
