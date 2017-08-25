package com.snail.webgame.game.pvp.fight.sendmsg;

import java.util.List;

import org.apache.mina.common.IoSession;

import com.snail.webgame.engine.component.room.protocol.config.RoomMessageConfig;
import com.snail.webgame.engine.component.room.protocol.info.IRoomBody;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.info.impl.RoomMessageHead;
import com.snail.webgame.engine.component.room.protocol.processor.BaseProcessor;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;
import com.snail.webgame.game.pvp.competition.ready.ComFightReadyResp;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.FightStartReq;

/**
 * 
 * 类介绍:接收战斗请求消息发送后结果处理类
 * 此消息由战斗服务器(本服PVP)发出
 * 用于通知游戏服务器战斗可以开始或战斗不能开始
 *
 * @author zhoubo
 * @2014-11-25
 */
public class SendMsgProcessor extends BaseProcessor {
	
	//private static final Logger logger = LoggerFactory.getLogger("logs");
	
	public SendMsgProcessor() {
		super();
	}
	
	public SendMsgProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		RoomMessageHead head = (RoomMessageHead) message.getiRoomHead();
		
		IoSession session = message.getSession();
		
		String serverReserve = null;
		
		if (message.getSession() != null) {
			serverReserve = session.getAttribute("fightServerIp").toString();
		}
		
		FightStartReq fightStartReq = (FightStartReq)message.getiRoomBody();
		List<ComFightRequestReq> list = fightStartReq.getList();
		
		Message gameMessage = new Message();
		ComFightReadyResp dispatchGameResp = new ComFightReadyResp();
		
		if (head.getUserState() == 1) {
			// 发送成功
			dispatchGameResp.setResult(1);
		} else {
			// 发送失败
			dispatchGameResp.setResult(head.getUserState());
		}
		
		dispatchGameResp.setServer(serverReserve);
		dispatchGameResp.setFightType(fightStartReq.getFightType());
		gameMessage.setiRoomBody(dispatchGameResp);
		
		for (ComFightRequestReq fightRequestReq : list) {
			if (dispatchGameResp.getRoleInfs() == null || "".equals(dispatchGameResp.getRoleInfs())) {
				dispatchGameResp.setRoleInfs(fightRequestReq.getRoleId() + ":" + fightRequestReq.getUuid());
			} else {
				dispatchGameResp.setRoleInfs(dispatchGameResp.getRoleInfs() + "," + fightRequestReq.getRoleId() + ":" + fightRequestReq.getUuid());
			}
		}
		
		RoomMessageConfig.processorMap.get(RoomValue.MESSAGE_TYPE_SEND_GAME_SERVER_START_FE17).processor(gameMessage);
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_SEND_MSG_RESULT_FE15;
	}

}
