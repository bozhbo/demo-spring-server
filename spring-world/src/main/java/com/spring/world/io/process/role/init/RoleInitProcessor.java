package com.spring.world.io.process.role.init;

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
import com.spring.logic.role.service.RoleLoginService;

public class RoleInitProcessor implements IProcessor {

	private static final Log logger = LogFactory.getLog(RoleInitProcessor.class);
	
	@Autowired
	private RoleLoginService roleLoginService;

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead) message.getiRoomHead();

		RoleInfo roleInfo = RoleCache.getRoleInfo(head.getRoleId());
		
		if (roleInfo == null) {
			// TODO error msg
			return;
		}
		
		roleLoginService.roleInit(roleInfo);
		
		logger.info("role init " + head.getRoleId());
	}
	
	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return null;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_INIT_SEND;
	}
}
