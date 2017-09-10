package com.spring.world.io.process.server.room;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.RemoveRoleResp;
import com.spring.logic.role.cache.RoleCache;
import com.spring.logic.role.info.RoleInfo;
import com.spring.logic.room.service.RoomService;

public class RemoveRoleProcessor implements IProcessor {

	private static final Log logger = LogFactory.getLog(DeployRoleProcessor.class);

	private RoomService roomService;

	@Override
	public void processor(Message message) {
		RemoveRoleResp req = (RemoveRoleResp) message.getiRoomBody();

		RoleInfo roleInfo = RoleCache.getRoleInfo(req.getRoleId());

		if (roleInfo == null) {
			// TODO error msg
			return;
		}

		synchronized (roleInfo) {
			if (req.getResult() != 1) {
				// 角色移除失败
				logger.warn("remove role failed " + req.getRoleId());
			} else {
				// 角色移除成功
				roomService.leaveRoom(req.getRoomId(), req.getRoleId());
				roleInfo.setRoomId(0);
			}
			
			// TODO 刷新大厅数据
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		return RemoveRoleResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.WORLD_2_ROOM_REMOVE_ROLE_RESP;
	}

	@Autowired
	public void setRoomService(RoomService roomService) {
		this.roomService = roomService;
	}

}
