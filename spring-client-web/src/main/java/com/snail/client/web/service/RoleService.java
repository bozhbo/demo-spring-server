package com.snail.client.web.service;

import java.util.UUID;

import com.snail.client.web.control.ClientControl;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.common.base.CommonResp;
import com.spring.logic.message.request.world.init.InitSceneResp;
import com.spring.logic.message.request.world.login.LoginReq;
import com.spring.logic.message.request.world.login.LoginResp;
import com.spring.room.game.message.init.GameRoomInitResp;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

public class RoleService {

	public void login(String userName, String password) {
		ClientControl.netService.checkSession();
		
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_LOGIN_SEND);
		
		LoginReq req = new LoginReq();
		req.setAccount(userName);
		req.setClientType(1);
		req.setIP(12);
		req.setMac(UUID.randomUUID().toString());
		req.setMd5Pass(UUID.randomUUID().toString());
		req.setPackageName("0.0.1");
		req.setValidate("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		ClientControl.netService.sendMessage(message);
	}
	
	public void loginEnd(LoginResp resp) {
	}
	
	public void fastStart() {
		ClientControl.netService.checkSession();
		
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND);
		
		CommonResp req = new CommonResp();
		req.setOptionType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_AUTO_START);
		req.setOptionStr("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		ClientControl.netService.sendMessage(message);
	}
	
	public void joinRoomEnd(GameRoomRoleInfoRes resp) {
	}
	
	public void roomInit(GameRoomInitResp resp) {
	}
	
	public void back2Scene() {
		ClientControl.netService.checkSession();
		
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND);
		
		CommonResp req = new CommonResp();
		req.setOptionType(GameMessageType.GAME_CLIENT_WORLD_COMMON_SEND_LEAVE_ROOM);
		req.setOptionStr("");
		
		Message message = new Message();
		message.setiRoomHead(head);
		message.setiRoomBody(req);
		
		ClientControl.netService.sendMessage(message);
	}
	
	public void back2SceneEnd(InitSceneResp resp) {
	}
}
