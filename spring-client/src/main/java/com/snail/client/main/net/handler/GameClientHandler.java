package com.snail.client.main.net.handler;

import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.net.msg.login.LoginResp;
import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.world.init.InitSceneResp;
import com.spring.room.game.message.init.GameRoomInitResp;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

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
		
		if (msgType == GameMessageType.GAME_CLIENT_LOGIN_RECEIVE) {
			RoomMessageHead head = (RoomMessageHead)msg.getiRoomHead();
			ClientControl.MY_ROLE_ID = head.getRoleId();
			ClientControl.roleService.loginEnd((LoginResp)msg.getiRoomBody());
		} else if (msgType == GameMessageType.GAME_CLIENT_ROOM_JOIN) {
			// 加入新玩家
			ClientControl.roleService.joinRoomEnd((GameRoomRoleInfoRes)msg.getiRoomBody());
		} else if (msgType == GameMessageType.ROOM_CLIENT_ROOM_INIT) {
			// 房间初始化
			ClientControl.roleService.roomInit((GameRoomInitResp)msg.getiRoomBody());
		} else if (msgType == GameMessageType.GAME_CLIENT_WORLD_SCENE_INIT_RECEIVE) {
			// 大厅初始化
			ClientControl.roleService.back2SceneEnd((InitSceneResp)msg.getiRoomBody());
		} else if (msgType == GameMessageType.GAME_CLIENT_PLAY_RECEIVE) {
			IProcessor processor = RoomMessageConfig.processorMap.get(msgType);
			processor.processor(msg);
		}
	}
}
