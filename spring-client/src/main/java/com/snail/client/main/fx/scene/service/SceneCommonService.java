package com.snail.client.main.fx.scene.service;

import com.snail.client.main.control.ClientControl;

public class SceneCommonService {

	public static void login(String userName, String password) {
		ClientControl.roleService.login(userName, password);
	}
}
