package com.spring.room.io.process;

import com.snail.mina.protocol.config.RoomMessageConfig;
import com.snail.mina.protocol.processor.register.RegisterProcessor;
import com.snail.mina.protocol.processor.register.RegisterReq;
import com.spring.room.io.process.active.ActiveProcessor;

public class RoomIoControl {

	public void init() {
		RoomMessageConfig.addProcessor(new ActiveProcessor());
		RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
		
		RoomMessageConfig.initProcessor();
	}
}
