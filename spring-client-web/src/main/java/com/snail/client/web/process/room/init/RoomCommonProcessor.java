package com.snail.client.web.process.room.init;

import com.snail.client.web.control.ClientControl;
import com.snail.mina.protocol.info.IRoomBody;
import com.snail.mina.protocol.info.Message;
import com.snail.mina.protocol.info.impl.RoomMessageHead;
import com.snail.mina.protocol.processor.IProcessor;
import com.spring.common.GameMessageType;
import com.spring.logic.message.request.common.base.CommonResp;

public class RoomCommonProcessor implements IProcessor {

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead)message.getiRoomHead();
		CommonResp resp = (CommonResp)message.getiRoomBody();
		
		if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_READY) {
			ClientControl.roleService.roomReady(head.getRoleId(), resp);
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_CARD) {
			// 给牌 等待3秒
			ClientControl.roleService.roomGiveCard(head.getRoleId(), resp);
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_OPERATION) {
			// 轮到自己操作
			ClientControl.roleService.roomRoleOperate(head.getRoleId(), resp);
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_SHOW_CARD) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROUND) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GAME_END) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROOM_INIT) {
			// 加入房间初始化
			ClientControl.roleService.roomInit(head.getRoleId(), resp);
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROLE_JOIN) {
			// 其他玩家加入
			ClientControl.roleService.joinRoomEnd(head.getRoleId(), resp);
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ROLE_READY) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_COMPARE) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_LOOK_CARD) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_ADD) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_FOLLOW) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_GIVE_UP) {
			
		} else if (resp.getOptionType() == GameMessageType.GAME_CLIENT_PLAY_RECEIVE_WIN) {
			
		}
	}

	@Override
	public Class<? extends IRoomBody> getRoomBodyClass() {
		// TODO Auto-generated method stub
		return CommonResp.class;
	}

	@Override
	public int getMsgType() {
		return GameMessageType.GAME_CLIENT_PLAY_RECEIVE;
	}

}
