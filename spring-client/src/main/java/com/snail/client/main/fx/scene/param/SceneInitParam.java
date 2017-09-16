package com.snail.client.main.fx.scene.param;

import com.snail.client.main.fx.scene.ISceneParam;
import com.spring.logic.message.request.world.init.InitSceneResp;

public class SceneInitParam implements ISceneParam {

	private InitSceneResp resp;
	
	public SceneInitParam(InitSceneResp resp) {
		this.resp = resp;
	}

	public InitSceneResp getResp() {
		return resp;
	}

	public void setResp(InitSceneResp resp) {
		this.resp = resp;
	}
	
	
}
