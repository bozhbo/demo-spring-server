package com.spring.room.game.service.Impl;

import com.spring.logic.business.service.RoomBusinessCallBack;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;

public class RoomBusinessCallBackImpl implements RoomBusinessCallBack {

	@Override
	public int roomRoleOnRecover(RoomRoleInfo roomRoleInfo, DeployRoleReq req) {
		// TODO Auto-generated method stub
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
