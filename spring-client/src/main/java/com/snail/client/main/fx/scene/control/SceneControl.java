package com.snail.client.main.fx.scene.control;

import java.util.HashMap;
import java.util.Map;

import com.snail.client.main.fx.scene.IScene;

import javafx.stage.Stage;

public class SceneControl {
	
	public Map<String, IScene> sceneMap = new HashMap<>();
	
	private Stage primaryStage;
	
	public static int HEIGHT = 500;
	public static int WIDTH = 800;
	
	public SceneControl(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void register(String name, IScene scene) {
		sceneMap.put(name, scene);
	}

	public void forward(String name) {
		if (sceneMap.containsKey(name)) {
			primaryStage.setScene(sceneMap.get(name).getScene());
		} else {
			primaryStage.setScene(sceneMap.get("Error").getScene());
		}
	}
}
