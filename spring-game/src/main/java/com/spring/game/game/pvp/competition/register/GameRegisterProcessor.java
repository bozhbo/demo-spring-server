package com.snail.webgame.game.pvp.competition.register;

import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;

public class GameRegisterProcessor extends BaseProcessor {
	
	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public GameRegisterProcessor() {
		super();
	}
	
	public GameRegisterProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
//		RegisterResp resp = (RegisterResp)message.getiRoomBody();
//		
//		if (resp != null) {
//			if (resp.getStartTime() < 60000) {
//				// 管理服务器启动时间在60秒内;
//				if (RoomClient.getConectTimes(GameConfig.getInstance().getRoomId()) > 1) {
//					// 连接超过1次,由于管理服务器中间发生重启
//					Set<Entry<Long, RoleInfo>> set = RoleInfoMap.getMap().entrySet();
//					
//					for (Entry<Long, RoleInfo> entry : set) {
//						RoleInfo roleInfo = entry.getValue();
//						
//						if (roleInfo != null) {
//							synchronized (roleInfo) {
//								if (roleInfo.getInFight() == 1) {
//									// 已报名未开始战斗
//								}
//							}
//						}
//					}
//				} else {
//					// 第一次注册，为启动时的正常注册连接
//				}
//			}
//		}
	}

	@Override
	public int getMsgType() {
		return 0;
		//return RoomValue.MESSAGE_TYPE_REGISTER_FE02;
	}
}
