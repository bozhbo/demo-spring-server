package com.spring.world.io.process.common.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.role.service.RoleRoomService;

@Component
public class CommonProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(CommonProcessor.class);
	
	private RoleRoomService roleRoomService;

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		CommonReq req = (CommonReq)message.getiRoomBody();
		RoleInfo roleInfo = RoleCache.getRoleInfo(head.getRoleId());
		
		if (roleInfo == null) {
			// TODO error msg
			return;
		}
		
		synchronized (roleInfo) {
			if (req.getOptionType() == GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_AUTO_START) {
				// 快速开始
				this.roleRoomService.autoJoin(roleInfo);
			} else if (req.getOptionType() == GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_LEAVE_ROOM) {
				// 返回大厅
				this.roleRoomService.leaveRoom(roleInfo);
			}
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return CommonReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND;
	}

	@Autowired
	public void setRoleRoomService(RoleRoomService roleRoomService) {
		this.roleRoomService = roleRoomService;
	}
}
