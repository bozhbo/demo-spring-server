package com.snail.webgame.game.protocal.countryfight.fight.createbattle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;

public class CreateStateFightProcessor extends BaseProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public CreateStateFightProcessor() {
		super();
	}
	
	public CreateStateFightProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		CreateStateFightResp resp = (CreateStateFightResp)message.getiRoomBody();
		
		if (resp != null) {
			//int result = resp.getResult();
			
		} else {
			logger.warn("CreateStateFightProcessor : FightCancelResp is null");
		}
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_FIGHT_CANCEL_FE13;
	}
}
