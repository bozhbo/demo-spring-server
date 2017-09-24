package com.spring.room.control.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.role.info.RoomRoleInfo;
import com.spring.logic.room.info.PlayingRoomInfo;
import com.spring.room.control.service.RoomMessageService;

@Service
public class RoomMessageServiceImpl implements RoomMessageService {
	
	private MessageService messageService;

	@Override
	public void send2AllRoles(PlayingRoomInfo playingRoomInfo, RoomMessageHead roomMessageHead, IRoomBody roomBody) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo roomRoleInfo : roleList) {
			try {
				RoomMessageHead sendRoomMessageHead = roomMessageHead.clone();
				sendRoomMessageHead.setRoleId(roomRoleInfo.getRoleId());

				messageService.sendGateMessage(roomRoleInfo.getGateId(), sendRoomMessageHead, roomBody);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void send2AllRolesExcept(PlayingRoomInfo playingRoomInfo, RoomRoleInfo exceptRoomRoleInfo,
			RoomMessageHead roomMessageHead, IRoomBody roomBody) {
		List<RoomRoleInfo> roleList = playingRoomInfo.getList();

		for (RoomRoleInfo roomRoleInfo : roleList) {
			if (roomRoleInfo == exceptRoomRoleInfo) {
				continue;
			}
			
			try {
				RoomMessageHead sendRoomMessageHead = roomMessageHead.clone();
				sendRoomMessageHead.setRoleId(roomRoleInfo.getRoleId());

				messageService.sendGateMessage(roomRoleInfo.getGateId(), sendRoomMessageHead, roomBody);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	
}
