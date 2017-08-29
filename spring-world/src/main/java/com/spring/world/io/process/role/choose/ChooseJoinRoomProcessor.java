package com.spring.world.io.process.role.choose;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleRequestService;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.world.io.process.role.login.LoginProcessor;

public class ChooseJoinRoomProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(LoginProcessor.class);
	
	private RoleRequestService roleRequestService;

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		ChooseJoinRoomReq req = (ChooseJoinRoomReq)message.getiRoomBody();
		
		if (req.getMsgType() > RoomTypeEnum.values().length || req.getMsgType() < 1) {
			return;
		}
		
		RoleInfo roleInfo = RoleCache.getRoleInfo(head.getRoleId());
		int errorCode = this.roleRequestService.roleJoinRoomCheck(roleInfo);
		
		if (errorCode > 1) {
			return;
		}
		
		//this.roleRequestService.roleChooseJoinRoom(RoomTypeEnum.values()[req.getMsgType() - 1]);
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return ChooseJoinRoomReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_ROOM_CHOOSE_JOIN_SEND;
	}

	@Autowired
	public void setRoleRequestService(RoleRequestService roleRequestService) {
		this.roleRequestService = roleRequestService;
	}

	
}
