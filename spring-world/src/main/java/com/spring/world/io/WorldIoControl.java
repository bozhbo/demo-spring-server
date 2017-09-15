package com.spring.world.io;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.processor.register.RegisterProcessor;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.world.io.process.active.ActiveProcessor;
import com.spring.world.io.process.common.base.CommonProcessor;
import com.spring.world.io.process.role.login.LoginProcessor;
import com.spring.world.io.process.server.room.DeployRoleProcessor;
import com.spring.world.io.process.server.room.DeployRoomProcessor;
import com.spring.world.io.process.server.room.RemoveRoleProcessor;

public class WorldIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new ActiveProcessor());
		RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
		
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(CommonProcessor.class));
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(LoginProcessor.class));
		
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(DeployRoleProcessor.class));
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(DeployRoomProcessor.class));
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(RemoveRoleProcessor.class));
		
		RoomMessageConfig.initProcessor();
	}
}
