package com.snail.webgame.game.pvp.competition.cancel;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.competition.cancel.CancelFightResp;

/**
 * 
 * 类介绍:用于接收发送跨服PVP战斗取消匹配回复接口
 * 此接口暂时不被使用，发送取消匹配后直接置玩家状态为未匹配，不等回复
 *
 * @author zhoubo
 * @2015年3月27日
 */
@Deprecated
public class FightCancelProcessor extends BaseProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public FightCancelProcessor() {
		super();
	}
	
	public FightCancelProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		FightCancelResp resp = (FightCancelResp)message.getiRoomBody();
		
		if (resp != null) {
			int roleId = resp.getRoleId();
			int result = resp.getResult();
			
			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
			
			if (roleInfo != null) {
				synchronized (roleInfo) {
					RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
					
					if (roleLoadInfo == null) {
						return;
					}
					
					logger.info("role " + roleInfo.getId() + " is cancel result = " + result);
					
					if (result == 1) {
						// 取消成功
						roleLoadInfo.setInFight((byte)0);
					}
					
					if (roleInfo.getLoginStatus() == 1) {
						IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());
						
						if (session != null && session.isConnected()) {
							CancelFightResp cancelFightResp = new CancelFightResp();
							
							if (result == 1) {
								// 取消成功
								cancelFightResp.setResult(1);
								
								org.epilot.ccf.core.protocol.Message gameMessage = new org.epilot.ccf.core.protocol.Message();
								GameMessageHead head = new GameMessageHead();
								head.setMsgType(Command.FIGHT_CANCEL_SUBMIT_COMPETITION_RESP);
								head.setUserID0((int) roleInfo.getId());
								gameMessage.setHeader(head);
								gameMessage.setBody(cancelFightResp);
								session.write(gameMessage);
							} else {
								// 取消失败
								if (roleLoadInfo.getInFight() == 1) {
									// 报名中,战斗开始状态未设置
									roleLoadInfo.setFightStartTime(System.currentTimeMillis());
								}
							}
						}
					}
				}
			}
		} else {
			logger.warn("FightCancelProcessor : FightCancelResp is null");
		}
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_FIGHT_CANCEL_FE13;
	}
}
