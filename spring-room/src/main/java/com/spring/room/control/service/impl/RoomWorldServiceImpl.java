package com.spring.room.control.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.Message;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.server.DeployRoleResp;
import com.spring.logic.message.request.server.DeployRoomResp;
import com.spring.logic.message.request.server.RemoveRoleResp;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;
import com.spring.logic.util.LogicUtil;
import com.spring.room.config.RoomServerConfig;
import com.spring.room.control.service.RoomWorldService;

@Service
public class RoomWorldServiceImpl implements RoomWorldService {
	
	private static final Log logger = LogFactory.getLog(RoomWorldServiceImpl.class);
	
	private MessageService messageService;

	@Override
	public int deployRoomInfoSuccessed(int roomId) {
		DeployRoomResp resp = new DeployRoomResp();
		resp.setResult(1);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_DEPLOY_ROOM_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		logger.info("deploy room success " + roomId);
		
		return 1;
	}

	@Override
	public int deployRoomInfoFailed(int roomId) {
		DeployRoomResp resp = new DeployRoomResp();
		resp.setResult(0);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_DEPLOY_ROOM_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		logger.info("deploy room failed " + roomId);
		
		return 1;
	}

	@Override
	public int removeRoomInfoSuccessed(int roomId) {
		return 1;
	}

	@Override
	public int removeRoomInfoFailed(int roomId) {
		return 1;
	}

	@Override
	public int deployRoleInfoSuccessed(int roomId, int roleId) {
		DeployRoleResp resp = new DeployRoleResp();
		resp.setResult(1);
		resp.setRoleId(roleId);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		logger.info("deploy role success " + roomId + ", roleId = " + roleId);
		
		return 1;
	}

	@Override
	public int deployRoleInfoFailed(int roomId, int roleId) {
		DeployRoleResp resp = new DeployRoleResp();
		resp.setResult(0);
		resp.setRoleId(roleId);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		logger.info("deploy role failed " + roomId + ", roleId = " + roleId);
		
		return 1;
	}

	@Override
	public int removeRoleInfoSuccessed(int roomId, int roleId) {
		RemoveRoleResp resp = new RemoveRoleResp();
		resp.setResult(1);
		resp.setRoleId(roleId);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_REMOVE_ROLE_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		return 1;
	}

	@Override
	public int removeRoleInfoFailed(int roomId, int roleId) {
		RemoveRoleResp resp = new RemoveRoleResp();
		resp.setResult(0);
		resp.setRoleId(roleId);
		resp.setRoomId(roomId);
		
		Message message = messageService.createMessage(GameMessageType.WORLD_2_ROOM_REMOVE_ROLE_RESP, 0, "", resp);
		messageService.sendWorldMessage(message);
		
		return 1;
	}

	@Override
	public void reportRoomServerInfo(int roomCount, int roleCount) {
		RoomServerInfo sendRoomServerInfo = new RoomServerInfo();
		
		sendRoomServerInfo.setIp(RoomServerConfig.ROOM_SERVER_IP);
		sendRoomServerInfo.setPort(RoomServerConfig.ROOM_SERVER_PORT);
		sendRoomServerInfo.setRoomServerId(RoomServerConfig.ROOM_SERVER_ID);
		sendRoomServerInfo.setServerName(RoomServerConfig.ROOM_SERVER_ID + "");
		sendRoomServerInfo.setSession(null);
		sendRoomServerInfo.setRoleCount(roomCount);
		sendRoomServerInfo.setRoomCount(roleCount);
		
		messageService.sendWorldMessage(messageService.createMessage(GameMessageType.ROOM_2_WORLD_ROOM_INFO, 0, LogicUtil.tojson(sendRoomServerInfo)));
	}

	@Autowired
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
