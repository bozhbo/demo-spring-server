package com.snail.client.main.fx.scene.param;

import com.snail.client.main.fx.scene.ISceneParam;
import com.snail.client.main.net.msg.join.JoinRoomResp;

public class RoomParam implements ISceneParam {
	
	private JoinRoomResp resp;
	
	public RoomParam(JoinRoomResp resp) {
		this.resp = resp;
	}
	
	public JoinRoomResp getResp() {
		return resp;
	}

	public void setResp(JoinRoomResp resp) {
		this.resp = resp;
	}
}
