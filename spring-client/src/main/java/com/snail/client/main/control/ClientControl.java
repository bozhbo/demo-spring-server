package com.snail.client.main.control;

import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.fx.task.RefreshTask;
import com.snail.client.main.net.service.NetService;
import com.snail.client.main.net.service.RoleService;

public class ClientControl {

	public static NetService netService;
	public static RoleService roleService;
	
	public static SceneControl sceneControl;
	public static RefreshTask refreshTask;
	
	public static void init() {
		netService = new NetService();
		roleService = new RoleService();
		refreshTask = new RefreshTask();
		
		netService.init();
	}

	public static void setSceneControl(SceneControl sceneControl) {
		ClientControl.sceneControl = sceneControl;
	}
	
}
