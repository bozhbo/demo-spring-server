package com.spring.scene.obj;

import com.spring.scene.config.GameSceneConfigInfo;

public class GameScene {

	private int sceneId;

	private GameSceneConfigInfo gameSceneConfigInfo;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public void setGameSceneConfigInfo(GameSceneConfigInfo gameSceneConfigInfo) {
		this.gameSceneConfigInfo = gameSceneConfigInfo;
	}

}
