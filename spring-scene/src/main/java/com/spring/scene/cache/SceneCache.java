package com.spring.scene.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.spring.scene.obj.GameScene;

public class SceneCache {

	private static Map<Integer, GameScene> map = new ConcurrentHashMap<>();
	
	public static void addGameScene(GameScene gameScene) {
		map.put(gameScene.getSceneId(), gameScene);
	}
	
	public static GameScene getGameScene(int sceneId) {
		return map.get(sceneId);
	}
	
	public static void removeGameScene(int sceneId) {
		map.remove(sceneId);
	}
}
