package com.spring.logic.message.service.impl;

import org.apache.mina.common.IoSession;
import org.springframework.stereotype.Service;

import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.message.service.MessageService;
import com.spring.logic.server.cache.GateServerCache;
import com.spring.logic.server.cache.RoomServerCache;
import com.spring.logic.server.info.RoomServerInfo;

@Service
public class MessageServiceImpl implements MessageService {

	@Override
	public Message createMessage(RoomMessageHead head) {
		Message message = new Message();
		message.setiRoomHead(head);
		
		return message;
	}

	@Override
	public Message createMessage(int msgType, int sceneId, String userState) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(msgType);
		head.setSceneId(sceneId);
		head.setUserState(userState);
		
		message.setiRoomHead(head);
		
		return message;
	}

	@Override
	public Message createMessage(RoomMessageHead head, IRoomBody body) {
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(body);
		
		return message;
	}

	@Override
	public Message createMessage(int msgType, int sceneId, String userState, IRoomBody body) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(msgType);
		head.setSceneId(sceneId);
		head.setUserState(userState);
		
		message.setiRoomHead(head);
		message.setiRoomBody(body);
		
		return message;
	}

	@Override
	public boolean sendGateMessage(int gateId, Message message) {
		IoSession session = GateServerCache.getIoSession(gateId);
		
		if (session != null && session.isConnected()) {
			session.write(message);
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean sendGateMessage(int gateId, int msgType, IRoomBody body) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(msgType);
		
		message.setiRoomHead(head);
		message.setiRoomBody(body);
		
		return sendGateMessage(gateId, message);
	}

	@Override
	public boolean sendRoomMessage(int roomServerId, Message message) {
		RoomServerInfo roomServerInfo = RoomServerCache.getRoomServerInfo(roomServerId);
		
		if (roomServerInfo != null && roomServerInfo.getSession() != null && roomServerInfo.getSession().isConnected()) {
			roomServerInfo.getSession().write(message);
			return true;
		}
		
		return false;
	}
	
	public boolean sendRoomMessage(int roomServerId, int msgType, IRoomBody body) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(msgType);
		
		message.setiRoomHead(head);
		message.setiRoomBody(body);
		
		return sendRoomMessage(roomServerId, message);
	}
	
	@Override
	public Message createErrorMessage(int errorCode, String addInfo) {
		Message message = new Message();
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_ERROR_RECEIVE);
		head.setUserState(errorCode + "$$$$" + addInfo);
		
		message.setiRoomHead(head);
		
		return message;
	}
}
