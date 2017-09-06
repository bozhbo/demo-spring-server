package com.snail.client.main.control;

import com.google.gson.Gson;
import com.snail.client.main.fx.scene.control.SceneControl;
import com.snail.client.main.net.service.NetService;
import com.snail.client.main.net.service.RoleService;

public class ClientControl {

	public static NetService netService;
	public static RoleService roleService;
	
	public static SceneControl sceneControl;
	
	public static int MY_ROLE_ID = 0;
	
	public static Gson gson = null;
	
	public static void init() {
		netService = new NetService();
		roleService = new RoleService();
		
		netService.init();
	}

	public static void setSceneControl(SceneControl sceneControl) {
		ClientControl.sceneControl = sceneControl;
	}
	
	public static <T> T fromJson(String json, Class<T> c) {
		try {
			return gson.fromJson(json, c);
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> String tojson(T t) {
		return gson.toJson(t);
	}
	
}
