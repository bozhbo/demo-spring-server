package com.snail.webgame.engine.game.module.trade.processor;

import com.snail.webgame.engine.game.base.actor.RoleActor;
import com.snail.webgame.engine.game.base.annotation.MessageType;
import com.snail.webgame.engine.game.base.info.msg.DummyReq;
import com.snail.webgame.engine.game.module.trade.processor.service.TempTradeActorService;
import com.snail.webgame.engine.game.temp.TempRoleInfo;
import com.snail.webgame.engine.net.msg.impl.BaseMessage;
import com.snail.webgame.engine.net.processor.impl.GameProcessorImpl;

public class TempTradeProcessor extends GameProcessorImpl {

	private TempTradeActorService tradeActorService;
	
	@MessageType(inputMsgType=0xB001)
	public void buyRoleItem(BaseMessage message, int roleId, int itemId) {
		// TODO 取得2个RoleActor, call方法的第一个RoleActor确保是消息发送者的RoleActor,顺序不能错乱
		RoleActor buyRoleActor = null;
		RoleActor sellRoleActor = null;
		
		call(message, (req, actors) -> {return tradeActorService.buyRoleItem((DummyReq)req, (TempRoleInfo)actors[0], (TempRoleInfo)actors[1]);}, buyRoleActor, sellRoleActor);
	}

	public void setTradeActorService(TempTradeActorService tradeActorService) {
		this.tradeActorService = tradeActorService;
	}
}
