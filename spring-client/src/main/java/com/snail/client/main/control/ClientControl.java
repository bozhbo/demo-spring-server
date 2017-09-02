package com.snail.client.main.control;

import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.net.service.NetService;
import com.snail.client.main.net.service.RoleService;

public class ClientControl {

	public static NetService netService;
	public static RoleService roleService;
	
	public static SceneControl sceneControl;
	
	public static void init() {
		netService = new NetService();
		roleService = new RoleService();
		
		netService.init();
	}

	public static void setSceneControl(SceneControl sceneControl) {
		ClientControl.sceneControl = sceneControl;
	}
	
}
