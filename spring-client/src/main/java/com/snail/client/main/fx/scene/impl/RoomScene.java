package com.snail.client.main.fx.scene.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.snail.client.main.control.ClientControl;
import com.snail.client.main.fx.scene.IScene;
import com.snail.client.main.fx.scene.ISceneParam;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.scene.param.RoomInitParam;
import com.snail.client.main.fx.scene.param.RoomParam;
import com.spring.room.game.message.init.GameRoomRoleInfoRes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class RoomScene implements IScene {

	private Scene scene;
	
	private Map<Integer, String> map = new HashMap<>();
	private int roomId;

	public RoomScene() {
		init();
	}

	public void init() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		grid.setVisible(true);
		
		Text room = new Text("RoomId: " + roomId + "");
		grid.add(room, 0, 0, 1, 1);
		
		Set<Entry<Integer, String>> set = map.entrySet();
		int row = 1;
		
		for (Entry<Integer, String> entry : set) {
			Text name = new Text(entry.getKey() + "");
			Text info = new Text(entry.getValue());
			
			grid.add(name, 0, row, 1, 1);
			grid.add(info, 1, row, 1, 1);
			
			row++;
		}
		
		Button btn1 = new Button("返回大厅");
		btn1.setOnAction((event) -> ClientControl.roleService.back2Scene());
		grid.add(btn1, 0, 2, 1, 1);

		Button btn = new Button("准备");
		btn.setOnAction((event) -> ClientControl.roleService.fastStart());
		grid.add(btn, 0, 3, 1, 1);

		if (scene == null) {
			scene = new Scene(grid, SceneControl.WIDTH, SceneControl.HEIGHT);
		} else {
			scene.setRoot(grid);
		}
	}

	public Scene getScene() {
		return scene;
	}

	@Override
	public void init(ISceneParam sceneParam) {
		if (sceneParam instanceof RoomParam) {
			// 别人加入
			RoomParam roomParam = (RoomParam)sceneParam;
			String info = ClientControl.tojson(roomParam.getResp());
			
			map.put(roomParam.getResp().getRoleId(), info);
		} else if (sceneParam instanceof RoomInitParam) {
			// 自己加入
			RoomInitParam roomInitParam = (RoomInitParam)sceneParam;
			
			roomId = roomInitParam.getResp().getRoomId();
			List<GameRoomRoleInfoRes> list = roomInitParam.getResp().getList();
			
			for (GameRoomRoleInfoRes gameRoomRoleInfoRes : list) {
				String info = ClientControl.tojson(gameRoomRoleInfoRes);
				map.put(gameRoomRoleInfoRes.getRoleId(), info);
			}
		}
		
		init();
	}
}
