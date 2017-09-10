package com.spring.room.game.service.Impl;

import java.util.ArrayList;
import java.util.List;

import com.spring.logic.business.service.RoomBusinessService;
import com.spring.logic.message.request.room.RoomInitResp;
import com.spring.logic.message.request.room.RoomJoinResp;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.game.message.init.GameRoomInitResp;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

public class RoomBusinessServiceImpl implements RoomBusinessService {

	@Override
	public RoomInitResp getRoomInitResp(PlayingRoomInfo playingRoomInfo) {
		GameRoomInitResp resp = new GameRoomInitResp();
		resp.setRoomId(playingRoomInfo.getRoomId());
		resp.setList(new ArrayList<>());
		
		List<RoomRoleInfo> list = playingRoomInfo.getList();
		
		for (RoomRoleInfo roomRoleInfo : list) {
			GameRoomRoleInfoRes res = new GameRoomRoleInfoRes();
			res.setGold(roomRoleInfo.getGold());
			res.setHeader(roomRoleInfo.getHeader());
			res.setRoleId(roomRoleInfo.getRoleId());
			res.setRoleName(roomRoleInfo.getRoleName());
			res.setVipLevel(roomRoleInfo.getVipLevel());
			
			resp.getList().add(res);
		}
		
		resp.setCount(resp.getList().size());
		
		return resp;
	}

	@Override
	public RoomJoinResp getRoomJoinResp(RoomRoleInfo roomRoleInfo) {
		// TODO Auto-generated method stub
		return new RoomJoinResp() ;
	}

}
