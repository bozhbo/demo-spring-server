package com.snail.client.main.fx.scene.param;

import com.snail.client.main.fx.scene.ISceneParam;
import com.snail.client.main.net.msg.login.LoginResp;

public class SceneParam implements ISceneParam {
	
	private LoginResp resp;
	
	public SceneParam(LoginResp resp) {
		this.resp = resp;
	}
	
	public LoginResp getResp() {
		return resp;
	}

	public void setResp(LoginResp resp) {
		this.resp = resp;
	}
}
