package com.spring.world.io.process.common.base;

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
import com.spring.world.room.service.RoomClientService;

public class CommonProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(CommonProcessor.class);
	
	private RoomClientService roomClientService;

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		CommonReq req = (CommonReq)message.getiRoomBody();
		
		if (req.getOptionType() == GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_AUTO_START) {
			// 自动开始
			RoleInfo roleInfo = RoleCache.getRoleInfo(head.getRoleId());
			
			if (roleInfo != null) {
				this.roomClientService.autoJoin(roleInfo);
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
	public void setRoomClientService(RoomClientService roomClientService) {
		this.roomClientService = roomClientService;
	}

	
}
