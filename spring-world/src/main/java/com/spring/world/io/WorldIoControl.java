package com.spring.world.io;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.spring.world.io.process.role.login.LoginProcessor;

public class WorldIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new LoginProcessor());
		
		RoomMessageConfig.initProcessor();
	}
}
