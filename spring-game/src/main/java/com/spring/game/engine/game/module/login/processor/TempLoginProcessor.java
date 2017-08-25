package com.snail.webgame.engine.game.module.login.processor;

import com.snail.webgame.engine.game.base.actor.RoleActor;
import com.snail.webgame.engine.game.base.actor.cahe.BaseSystemActors;
import com.snail.webgame.engine.game.base.annotation.MessageType;
import com.snail.webgame.engine.game.module.login.msg.TempLoginReq;
import com.snail.webgame.engine.game.module.login.processor.service.TempLoginActorService;
import com.snail.webgame.engine.game.temp.TempRoleInfo;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;
import com.snail.webgame.engine.net.msg.impl.GameMessageHead;
import com.snail.webgame.engine.net.processor.impl.GameProcessorImpl;

public class TempLoginProcessor extends GameProcessorImpl {
	
	private TempLoginActorService loginActorService;
	
	@MessageType(inputMsgType=0xA005)
	public void login(final BaseMessage message, final TempLoginReq loginReq) {
		final RoleActor roleActor = BaseSystemActors.getActor(RoleActor.class, message.getiMessageHead(GameMessageHead.class).getUserID0());
		call(message, (req, actors) -> {return this.loginActorService.login((TempLoginReq)req, (TempRoleInfo)actors[0]);}, roleActor);
	}
	
	public void setLoginActorService(TempLoginActorService loginActorService) {
		this.loginActorService = loginActorService;
	}
}
