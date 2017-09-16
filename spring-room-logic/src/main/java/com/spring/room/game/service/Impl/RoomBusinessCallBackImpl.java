package com.spring.room.game.service.Impl;

import org.springframework.stereotype.Service;

import com.spring.common.logic.message.GameWorldDeployRoleReq;
import com.spring.logic.business.service.RoomBusinessCallBack;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

@Service
public class RoomBusinessCallBackImpl implements RoomBusinessCallBack {

	@Override
	public int roomRoleOnRecover(RoomRoleInfo roomRoleInfo, DeployRoleReq req) {
		GameWorldDeployRoleReq gameWorldDeployRoleReq = (GameWorldDeployRoleReq)req;
		
		roomRoleInfo.setGateId(gameWorldDeployRoleReq.getGateId());
		roomRoleInfo.setGold(gameWorldDeployRoleReq.getGold());
		roomRoleInfo.setHeader(gameWorldDeployRoleReq.getHeader());
		roomRoleInfo.setRoleName(gameWorldDeployRoleReq.getRoleName());
		roomRoleInfo.setVipLevel(gameWorldDeployRoleReq.getVipLevel());
		
		return 1;
	}

	@Override
	public int roomRoleOnAdd(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int roomRoleOnRemove(PlayingRoomInfo playingRoomInfo, RoomRoleInfo roomRoleInfo) {
		// TODO Auto-generated method stub
		return 1;
	}

}
