package com.spring.world.game.service.impl;

import org.springframework.stereotype.Service;

import com.spring.common.logic.message.GameWorldDeployRoleReq;
import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.enums.RoomTypeEnum;

@Service
public class RoleBusinessServiceImpl implements RoleBusinessService {

	@Override
	public RoomTypeEnum getRoomType(RoleInfo roleInfo) {
		return RoomTypeEnum.ROOM_TYPE_NEW;
	}

	@Override
	public DeployRoleReq getDeployRoleMessage(RoleInfo roleInfo) {
		GameWorldDeployRoleReq req = new GameWorldDeployRoleReq();
		
		req.setRoleId(roleInfo.getRoleId());
		req.setGateId(roleInfo.getGateId());
		req.setGold(roleInfo.getGold());
		req.setHeader(roleInfo.getHeader());
		req.setRoleName(roleInfo.getRoleName());
		req.setVipLevel(roleInfo.getVipLevel());
		
		return req;
	}

}
