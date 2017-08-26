package com.snail.client.main.fx;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.impl.ErrorScene;
import com.snail.client.main.fx.scene.impl.MainScene;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {
	
	private SceneControl sceneControl;

	@Override
	public void start(Stage primaryStage) {
		ClientControl.init();
		
		sceneControl = new SceneControl(primaryStage);
		
		MainScene mainScene = new MainScene();
		ErrorScene errorScene = new ErrorScene();
		
		sceneControl.register("Login", mainScene);
		sceneControl.register("Error", errorScene);
		
		primaryStage.setTitle("Hello");
		primaryStage.setScene(mainScene.getScene());
		primaryStage.show();
		
		primaryStage.setOnCloseRequest((event) -> System.exit(0));
		
		sceneControl.forward("Login");
	}

	public static void main(String[] args) {
		launch(args);
	}
}