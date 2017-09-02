package com.snail.client.main.fx.scene.impl;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.IScene;
import com.snail.client.main.fx.scene.ISceneParam;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.param.SceneParam;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SceneScene implements IScene {

	private Scene scene;
	
	private Text scenetitle;

	public SceneScene() {
		init();
	}

	public void init() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setVisible(true);
		
		// userInfo.textProperty().bind(ClientControl.refreshTask.messageProperty());

		scenetitle = new Text("");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		grid.add(scenetitle, 0, 0, 1, 1);
		
		Button btn1 = new Button("返回登录");
		btn1.setOnAction((event) -> ClientControl.sceneControl.forward("Login", null));
		grid.add(btn1, 0, 1, 1, 1);

		Button btn = new Button("快速开始");
		btn.setOnAction((event) -> ClientControl.roleService.fastStart());
		grid.add(btn, 0, 2, 1, 1);

		scene = new Scene(grid, SceneControl.WIDTH, SceneControl.HEIGHT);
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public void init(ISceneParam sceneParam) {
		SceneParam sceneParam1 = (SceneParam)sceneParam;
		
		if (sceneParam1.getResp().getResult() != 1) {
			
		} else {
			scenetitle.setText("Name:" + sceneParam1.getResp().getRoleName());
		}
	}
}
