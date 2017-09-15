package com.snail.client.main.net.service;

import java.util.UUID;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.param.RoomInitParam;
import com.snail.client.main.fx.scene.param.RoomParam;
import com.snail.client.main.fx.scene.param.SceneParam;
import com.snail.client.main.net.msg.common.CommonResp;
import com.snail.client.main.net.msg.login.LoginReq;
import com.snail.client.main.net.msg.login.LoginResp;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;
import com.spring.room.game.message.init.GameRoomInitResp;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

import javafx.application.Platform;

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
		Platform.runLater(() -> {
			ClientControl.sceneControl.forward("scene", new SceneParam(resp));
	    });
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
		Platform.runLater(() -> {
			ClientControl.sceneControl.forward("room", new RoomParam(resp));
	    });
	}
	
	public void roomInit(GameRoomInitResp resp) {
		Platform.runLater(() -> {
			ClientControl.sceneControl.forward("room", new RoomInitParam(resp));
	    });
	}
}
