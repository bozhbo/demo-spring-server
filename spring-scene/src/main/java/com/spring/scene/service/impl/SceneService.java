package com.spring.scene.service.impl;

import com.spring.scene.obj.GameObject;
import com.spring.scene.service.ISceneService;

public class SceneService implements ISceneService {

	@Override
	public boolean sceneInit(int sceneId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sceneDestroy(int sceneId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GameObject createGameObject(Class<? extends GameObject> gameObjectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addGameObjectToScene(int sceneId, GameObject gameObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteGameObjectFromScene(int sceneId, GameObject gameObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transferGameObjectToScene(int fromSceneId, int toSceneId, GameObject gameObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean holdGameObjectInScene(int sceneId, GameObject gameObject) {
		// TODO Auto-generated method stub
		return false;
	}

}
