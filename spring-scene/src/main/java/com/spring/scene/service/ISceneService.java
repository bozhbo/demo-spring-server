package com.spring.scene.service;

import com.spring.scene.obj.GameObject;

public interface ISceneService {

	public boolean sceneInit(int sceneId);
	
	public boolean sceneDestroy(int sceneId);

	public GameObject createGameObject(Class<? extends GameObject> gameObjectClass);
	
	public boolean addGameObjectToScene(int sceneId, GameObject gameObject);
	
	public boolean deleteGameObjectFromScene(int sceneId, GameObject gameObject);
	
	public boolean transferGameObjectToScene(int fromSceneId, int toSceneId, GameObject gameObject);
	
	public boolean holdGameObjectInScene(int sceneId, GameObject gameObject);
}
