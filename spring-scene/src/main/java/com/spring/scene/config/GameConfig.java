package com.spring.scene.config;

import com.spring.scene.cache.SceneCache;
import com.spring.scene.obj.GameScene;

public class GameConfig {

	public static void init() {
		// TODO map scene xml to GameSceneConfigInfo
		GameSceneConfigInfo gameSceneConfigInfo = new GameSceneConfigInfo();
		gameSceneConfigInfo.setSceneId(1);
		
		GameScene gameScene = new GameScene();
		gameScene.setGameSceneConfigInfo(gameSceneConfigInfo);
		gameScene.setSceneId(gameSceneConfigInfo.getSceneId());
		
		SceneCache.addGameScene(gameScene);
		
	}
}
