package com.snail.client.web.handle;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.snail.client.web.control.ClientControl;
import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.init.InitResp;
import com.spring.logic.message.request.world.init.InitSceneResp;
import com.spring.logic.message.request.world.login.LoginResp;

public class GameClientHandler extends IoHandlerAdapter {
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Message msg = (Message)message;
		int msgType = msg.getiRoomHead().getMsgType();
		int roleId = ((RoomMessageHead)msg.getiRoomHead()).getRoleId();
		
		if (msgType == GameMessageType.GAME_CLIENT_LOGIN_RECEIVE) {
			RoomMessageHead head = (RoomMessageHead)msg.getiRoomHead();
			ClientControl.MY_ROLE_ID = head.getRoleId();
			ClientControl.roleService.loginEnd((LoginResp)msg.getiRoomBody());
		} else if (msgType == GameMessageType.GAME_CLIENT_INIT_RECEIVE) {
			InitResp resp = (InitResp)msg.getiRoomBody();
			ClientControl.roleService.initEnd(resp);
		} else if (msgType == GameMessageType.GAME_CLIENT_WORLD_SCENE_INIT_RECEIVE) {
			// 大厅初始化
			ClientControl.roleService.back2SceneEnd((InitSceneResp)msg.getiRoomBody());
		} else if (msgType == GameMessageType.GAME_CLIENT_PLAY_RECEIVE) {
			IProcessor processor = RoomMessageConfig.processorMap.get(msgType);
			processor.processor(msg);
		}
	}
}
