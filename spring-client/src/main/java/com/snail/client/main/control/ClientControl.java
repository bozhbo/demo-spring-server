package com.snail.client.main.control;

import com.snail.client.main.net.service.NetService;
import com.snail.client.main.net.service.RoleService;

public class ClientControl {

	public static NetService netService;
	public static RoleService roleService;
	
	public static void init() {
		netService = new NetService();
		roleService = new RoleService();
		
		netService.init();
	}
}
