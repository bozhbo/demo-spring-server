package com.snail.client.main.fx.scene.impl;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.IScene;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.service.SceneCommonService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class MainScene implements IScene {

	private Button btn;
	private Scene scene;

	public MainScene() {
		init();
	}

	public void init() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setVisible(true);

		Text scenetitle = new Text("User Login");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 30));
		grid.add(scenetitle, 0, 0, 2, 1);

		// 创建Label对象，放到第0列，第1行
		Label userName = new Label("User Name:");
		userName.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		grid.add(userName, 0, 1);

		// 创建文本输入框，放到第1列，第1行
		final TextField userTextField = new TextField();
		grid.add(userTextField, 1, 1);

		Label pw = new Label("Password:");
		pw.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		grid.add(pw, 0, 2);

		final PasswordField pwBox = new PasswordField();
		grid.add(pwBox, 1, 2);
		
		btn = new Button("login");
		btn.setOnAction((event) -> ClientControl.roleService.login(userTextField.getText().trim(), pwBox.getText().trim()));
		grid.add(btn, 1, 3, 2, 1);

		scene = new Scene(grid, SceneControl.WIDTH, SceneControl.HEIGHT);
	}

	public Button getBtn() {
		return btn;
	}

	public Scene getScene() {
		return scene;
	}
}
