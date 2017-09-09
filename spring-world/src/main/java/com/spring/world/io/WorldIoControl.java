package com.spring.world.io;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.processor.register.RegisterProcessor;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.spring.logic.bean.GlobalBeanFactory;
import com.spring.world.io.process.active.ActiveProcessor;
import com.spring.world.io.process.common.base.CommonProcessor;
import com.spring.world.io.process.role.login.LoginProcessor;

public class WorldIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new ActiveProcessor());
		RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
		
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(CommonProcessor.class));
		RoomMessageConfig.addProcessor(GlobalBeanFactory.getBeanByName(LoginProcessor.class));
		
		
		RoomMessageConfig.initProcessor();
	}
}
