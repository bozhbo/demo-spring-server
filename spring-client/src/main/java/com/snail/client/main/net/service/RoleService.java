package com.snail.client.main.net.service;

import java.util.UUID;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.param.SceneParam;
import com.snail.client.main.net.msg.login.LoginReq;
import com.snail.client.main.net.msg.login.LoginResp;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.spring.common.GameMessageType;

public class RoleService {

	public void login(String userName, String password) {
		ClientControl.netService.checkSession();
		
		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(GameMessageType.GAME_CLIENT_LOGIN_SEND);
		
		LoginReq req = new LoginReq();
		req.setAccount("bob");
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
		ClientControl.sceneControl.forward("scene", new SceneParam(resp));
	}
	
	public void fastStart() {
		
	}
}
