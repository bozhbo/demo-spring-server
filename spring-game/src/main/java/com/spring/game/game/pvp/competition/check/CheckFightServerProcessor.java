package com.snail.webgame.game.pvp.competition.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.info.RoleInfo;

/**
 * 
 * 类介绍:用于检查战斗服务器的状态，如果战斗服务器宕机过则清除玩家当前战斗状态
 *
 * @author zhoubo
 * @2015年6月12日
 */
public class CheckFightServerProcessor extends BaseProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public CheckFightServerProcessor() {
		super();
	}
	
	public CheckFightServerProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		CheckFightServerResp req = (CheckFightServerResp) message.getiRoomBody();
		
		int result = req.getResult();
		long roleId = req.getRoleId();
		
		if (result == 2 || result == 3) {
			// 当前战斗服务器已重启或宕机
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo((int)roleId);
			
			if (roleInfo != null) {
				synchronized (roleInfo) {
					if (roleInfo.getRoleLoadInfo() != null) {
						roleInfo.getRoleLoadInfo().setInFight((byte)0);
						roleInfo.getRoleLoadInfo().setFightServer(null);
						roleInfo.getRoleLoadInfo().setUuid(null);
						roleInfo.getRoleLoadInfo().setFightStartTime(0);
					}
				}
			}
		}
		
		logger.info("CheckFightServerProcessor : receive check result = " + result + " role = " + roleId);
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_SEND_GAME_SERVER_CHECK_FE19;
	}

}
