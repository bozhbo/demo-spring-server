package com.spring.room.io.process.play;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.business.service.RoleBusinessService;
import com.spring.logic.message.request.common.base.CommonReq;
import com.spring.logic.message.request.server.DeployRoleReq;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.cache.RoleRoomCache;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.role.service.RoleRoomService;
import com.spring.logic.room.enums.RoomTypeEnum;
import com.spring.logic.util.LogicUtil;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.event.RoleOperateEvent;
import com.spring.room.thread.RoomLoopThread;

public class PlayGameProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(PlayGameProcessor.class);
	
	private MessageService messageService;
	
	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		CommonReq req = (CommonReq)message.getiRoomBody();
		RoomRoleInfo roomRoleInfo = RoleRoomCache.getRoomRoleInfo(head.getRoleId());
		
		if (roomRoleInfo == null) {
			Message errorMessage = messageService.createErrorMessage(740002, "", head);
			message.getSession().write(errorMessage);
			
			logger.warn("role not exist " + head.getRoleId());
			return;
		}
		
		int roomId = head.getSceneId();
		
		RoomLoopThread roomLoopThread = RoomServerConfig.getRoomThread().getRoomLoopThread(roomId);
		
		if (roomLoopThread == null) {
			Message errorMessage = messageService.createErrorMessage(740002, "", head);
			message.getSession().write(errorMessage);
			
			logger.warn("room thread not exist" + roomId);
			return;
		} else {
			roomLoopThread.addRoomEvent(new RoleOperateEvent(roomId, req.getOptionType(), req.getOptionStr(), roomRoleInfo));
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
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
}
