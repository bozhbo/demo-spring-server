package com.snail.webgame.engine.game.module.login.processor.service;

import com.snail.webgame.engine.game.base.actor.support.IActorService;
import com.snail.webgame.engine.game.module.login.msg.TempLoginReq;
import com.snail.webgame.engine.game.module.login.msg.TempLoginResp;
import com.snail.webgame.engine.game.temp.TempRoleInfo;

public interface TempLoginActorService extends IActorService {

	public TempLoginResp login(TempLoginReq req, TempRoleInfo roleInfo);
	
}
