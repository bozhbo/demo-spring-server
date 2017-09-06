package com.snail.client.main.fx;

import com.google.gson.GsonBuilder;
import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.impl.ErrorScene;
import com.snail.client.main.fx.scene.impl.MainScene;
import com.snail.client.main.fx.scene.impl.SceneScene;

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
		SceneScene sceneScene = new SceneScene();
		
		sceneControl.register("Login", mainScene);
		sceneControl.register("Error", errorScene);
		sceneControl.register("scene", sceneScene);
		
		primaryStage.setTitle("Hello");
		primaryStage.setScene(mainScene.getScene());
		primaryStage.show();
		
		primaryStage.setOnCloseRequest((event) -> System.exit(0));
		
		sceneControl.forward("Login", null);
		
		ClientControl.setSceneControl(sceneControl);
	}

	public static void main(String[] args) {
		ClientControl.gson = new GsonBuilder().setDateFormat("MM/dd/yyyy HH:mm:ss").create();
		
		launch(args);
		
		ClientControl.netService.checkSession();
	}
	
}
