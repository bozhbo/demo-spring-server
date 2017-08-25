package com.snail.webgame.engine.game.module.trade.processor.service;

import com.snail.webgame.engine.game.base.actor.support.IActorService;
import com.snail.webgame.engine.game.base.info.msg.DummyReq;
import com.snail.webgame.engine.game.module.trade.msg.TempBuyRoleItemResp;
import com.snail.webgame.engine.game.temp.TempRoleInfo;

public interface TempTradeActorService extends IActorService {

	public TempBuyRoleItemResp buyRoleItem(DummyReq req, TempRoleInfo buyRoleActor, TempRoleInfo sellRoleActor);
	
}
