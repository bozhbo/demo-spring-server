package com.snail.client.main.fx.scene.param;

import com.snail.client.main.fx.scene.ISceneParam;
import com.spring.room.game.message.init.GameRoomInitResp;

public class RoomInitParam implements ISceneParam {

	private GameRoomInitResp resp;
	
	public RoomInitParam(GameRoomInitResp resp) {
		this.resp = resp;
	}

	public GameRoomInitResp getResp() {
		return resp;
	}

	public void setResp(GameRoomInitResp resp) {
		this.resp = resp;
	}
	
	
}
