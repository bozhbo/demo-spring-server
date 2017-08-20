package com.spring.scene.config;

/**
 * 
 * 场景基础配置信息
 * @author Administrator
 *
 */
public class GameSceneConfigInfo {
	
	private int sceneId;
	private int sceneType;
	private String configXml;

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getSceneType() {
		return sceneType;
	}

	public void setSceneType(int sceneType) {
		this.sceneType = sceneType;
	}

	public String getConfigXml() {
		return configXml;
	}

	public void setConfigXml(String configXml) {
		this.configXml = configXml;
	}

	
}
