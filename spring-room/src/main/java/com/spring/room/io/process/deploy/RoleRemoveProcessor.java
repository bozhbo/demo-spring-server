package com.spring.room.io.process.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.RemoveRoleReq;

public class RoleRemoveProcessor implements IProcessor {
	
	private static final Log logger = LogFactory.getLog(RoleRemoveProcessor.class);

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		RemoveRoleReq req = (RemoveRoleReq)message.getiRoomBody();
		
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return RemoveRoleReq.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_REMOVE_ROLE;
	}

}
