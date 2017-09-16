package com.spring.room.io.process;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.processor.register.RegisterProcessor;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.spring.common.GameMessageType;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.logic.business.service.RoomBusinessService;
import com.spring.room.io.process.active.ActiveProcessor;
import com.spring.room.io.process.deploy.RoleDeployProcessor;
import com.spring.room.io.process.deploy.RoleRemoveProcessor;
import com.spring.room.io.process.deploy.RoomDeployProcessor;

public class RoomIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new ActiveProcessor());
		RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
		
		RoomBusinessService roomBusinessService = GlobalBeanFactory.getBeanByName(RoomBusinessService.class);
		
		RoleDeployProcessor roleDeployProcessor = new RoleDeployProcessor(roomBusinessService.getProcessClass(GameMessageType.WORLD_2_ROOM_DEPLOY_ROLE_REQ));
		
		RoomMessageConfig.addProcessor(roleDeployProcessor);
		RoomMessageConfig.addProcessor(new RoleRemoveProcessor());
		RoomMessageConfig.addProcessor(new RoomDeployProcessor());
		
		RoomMessageConfig.initProcessor();
	}
}
