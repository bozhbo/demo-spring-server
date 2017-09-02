package com.snail.client.main.fx.scene.impl;

import com.snail.client.main.fx.scene.IScene;
import com.snail.client.main.fx.scene.ISceneParam;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class ErrorScene implements IScene {
	
	private Button btn;
	private Scene scene;
	private StackPane root;
	
	public ErrorScene() {
		root = new StackPane();
		scene = new Scene(root, 300, 250);
		btn = new Button("Home");
		
		root.getChildren().add(btn);
	}

	@Override
	public Scene getScene() {
		return scene;
	}

	@Override
	public void init(ISceneParam sceneParam) {
		// TODO Auto-generated method stub
		
	}

}
