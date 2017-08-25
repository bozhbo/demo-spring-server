package com.snail.webgame.game.pvp.fight.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.info.impl.RoomMessageHead;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;

/**
 * 
 * 类介绍:接收战斗服务器发送的服务器当前战斗数量，用于判断战斗服务器的压力
 *
 * @author zhoubo
 * @2014-11-20
 */
public class FightServerStateProcessor extends BaseProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public FightServerStateProcessor() {
		super();
	}
	
	public FightServerStateProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		RoomMessageHead roomMessageHead = (RoomMessageHead)message.getiRoomHead();
		message.getSession().setAttribute("fightServerCurrentCounts", roomMessageHead.getUserState());
		
		// =============== debug info ===============
		if (RoomValue.USE_SERVER_STATE_MONITOR) {
			logger.info("fight server state : " + message.getSession().getAttribute("serverName") + " fightServerCurrentCounts = " + roomMessageHead.getUserState());	
		}
		// =============== debug info ===============
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_SERVER_STATE_FE09;
	}


}