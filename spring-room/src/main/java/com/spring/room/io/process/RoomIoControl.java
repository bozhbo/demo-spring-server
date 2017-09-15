package com.spring.room.io.process;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.processor.register.RegisterProcessor;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.spring.room.io.process.active.ActiveProcessor;
import com.spring.room.io.process.deploy.RoleDeployProcessor;
import com.spring.room.io.process.deploy.RoleRemoveProcessor;
import com.spring.room.io.process.deploy.RoomDeployProcessor;

public class RoomIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new ActiveProcessor());
		RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
		
		RoomMessageConfig.addProcessor(new RoleDeployProcessor());
		RoomMessageConfig.addProcessor(new RoleRemoveProcessor());
		RoomMessageConfig.addProcessor(new RoomDeployProcessor());
		
		RoomMessageConfig.initProcessor();
	}
}
