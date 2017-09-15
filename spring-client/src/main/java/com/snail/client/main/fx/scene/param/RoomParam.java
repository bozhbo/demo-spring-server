package com.snail.client.main.fx.scene.param;

import com.snail.client.main.fx.scene.ISceneParam;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

public class RoomParam implements ISceneParam {
	
	private GameRoomRoleInfoRes resp;
	
	public RoomParam(GameRoomRoleInfoRes resp) {
		this.resp = resp;
	}
	
	public GameRoomRoleInfoRes getResp() {
		return resp;
	}

	public void setResp(GameRoomRoleInfoRes resp) {
		this.resp = resp;
	}
}
