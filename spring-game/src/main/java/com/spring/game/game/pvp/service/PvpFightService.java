package com.snail.webgame.game.pvp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.mina.common.IoSession;
import org.epilot.ccf.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.component.room.protocol.client.RoomClient;
import com.snail.webgame.engine.component.room.protocol.config.RoomMessageConfig;
import com.snail.webgame.engine.component.room.protocol.info.Message;
import com.snail.webgame.engine.component.room.protocol.info.RoomServerInfo;
import com.snail.webgame.engine.component.room.protocol.info.impl.RoomMessageHead;
import com.snail.webgame.engine.component.room.protocol.processor.register.RegisterProcessor;
import com.snail.webgame.engine.component.room.protocol.processor.register.RegisterReq;
import com.snail.webgame.engine.component.room.protocol.server.RoomServer;
import com.snail.webgame.engine.component.room.protocol.util.RoomValue;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameServerName;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.pvp.competition.cancel.FightCancelProcessor;
import com.snail.webgame.game.pvp.competition.cancel.FightCancelResp;
import com.snail.webgame.game.pvp.competition.check.CheckFightServerProcessor;
import com.snail.webgame.game.pvp.competition.check.CheckFightServerReq;
import com.snail.webgame.game.pvp.competition.check.CheckFightServerResp;
import com.snail.webgame.game.pvp.competition.end.ComFightEndProcessor;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.competition.handler.CommonPvpServerHandler;
import com.snail.webgame.game.pvp.competition.handler.CompetitionClientHandler;
import com.snail.webgame.game.pvp.competition.handler.ServerHandler;
import com.snail.webgame.game.pvp.competition.ready.ComFightReadyProcessor;
import com.snail.webgame.game.pvp.competition.ready.ComFightReadyResp;
import com.snail.webgame.game.pvp.competition.request.ComFightRequestReq;
import com.snail.webgame.game.pvp.competition.request.FightStartReq;
import com.snail.webgame.game.pvp.fight.sendmsg.SendMsgProcessor;
import com.snail.webgame.game.pvp.fight.state.FightServerStateProcessor;

/**
 * 
 * 类介绍:PVP客户端接口Service类
 * 
 * @author zhoubo
 * @2014-11-24
 */
public class PvpFightService {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 连接到远程PVP服务器,在服务器启动是调用
	 * 
	 * @throws Exception
	 *             异常
	 */
	public static void connectRoomManageServer() throws Exception {
		if (GameConfig.getInstance().getRoomIp() != null && !"".equals(GameConfig.getInstance().getRoomIp())) {
			CompetitionClientHandler competitionClientHandler = new CompetitionClientHandler();

			RoomClient.connect(GameConfig.getInstance().getRoomIp(), GameConfig.getInstance().getRoomPort(), GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId(),
					GameConfig.getInstance().getRoomId(), competitionClientHandler, ThreadPoolManager.getInstance().Pool("webgame"));

			if (GameConfig.getInstance().getRoomServerList() != null && GameConfig.getInstance().getRoomServerList().size() > 0) {
				List<RoomServerInfo> roomServerList = GameConfig.getInstance().getRoomServerList();

				for (RoomServerInfo roomServerInfo : roomServerList) {
					RoomClient.connect(roomServerInfo.getServerIp(), roomServerInfo.getServerPort(), GameConfig.getInstance().getServerName() + GameConfig.getInstance().getGameServerId(),
							roomServerInfo.getServerName(), competitionClientHandler, ThreadPoolManager.getInstance().Pool("webgame"));
				}
			}

			// 用于GameServer作为客户端接口
			// RoomMessageConfig.addProcessor(new
			// GameRegisterProcessor(RegisterResp.class));
			RoomMessageConfig.addProcessor(new ComFightEndProcessor(ComFightEndResp.class));
			RoomMessageConfig.addProcessor(new ComFightReadyProcessor(ComFightReadyResp.class));
			RoomMessageConfig.addProcessor(new FightCancelProcessor(FightCancelResp.class));
			RoomMessageConfig.addProcessor(new CheckFightServerProcessor(CheckFightServerResp.class));

			// 用于GameServer作为服务器接口
			RoomMessageConfig.addProcessor(new RegisterProcessor(RegisterReq.class));
			RoomMessageConfig.addProcessor(new SendMsgProcessor(FightStartReq.class));
			RoomMessageConfig.addProcessor(new FightServerStateProcessor());

			RoomMessageConfig.initProcessor();
		}
	}

	/**
	 * 启动战斗监听端口
	 * 
	 * @throws Exception
	 */
	public static void startFightListener(RoomServer roomServer) throws Exception {
		if (GameConfig.getInstance().getServerIp() != null && !"".equals(GameConfig.getInstance().getServerIp())) {
			CommonPvpServerHandler commonPvpServerHandler = new CommonPvpServerHandler();

			roomServer.start(GameConfig.getInstance().getServerIp(), GameConfig.getInstance().getFightPort(), 1, commonPvpServerHandler, ThreadPoolManager.getInstance().Pool("webgame"), false);
		}

		if (GameConfig.getInstance().getInnerPort() > 0) {
			// 开启内部命令端口
			roomServer.innerServerStart(GameConfig.getInstance().getServerIp(), GameConfig.getInstance().getInnerPort(), new ServerHandler());
		}
	}

	/**
	 * 发送PVP战斗请求(竞技场、长坂坡、组队副本)
	 * 
	 * @param req
	 *            请求Req
	 * @return int 1-发送成功 0-发送失败
	 */
	public static int sendPvpCompetition(ComFightRequestReq req) {
		if (req == null) {
			return 0;
		}

		IoSession session = RoomMessageConfig.serverMap.get(GameConfig.getInstance().getRoomId());

		if (session != null && session.isConnected()) {
			Message message = new Message();
			RoomMessageHead head = new RoomMessageHead();
			head.setMsgType(RoomValue.MESSAGE_TYPE_FIGHT_REQUEST_FE03);
			message.setiRoomHead(head);
			message.setiRoomBody(req);

			session.write(message);
			return 1;
		} else {
			return ErrorCode.FIGHT_FIGHT_SERVER_INAVAILABLE_ERROR;
		}
	}

	/**
	 * 发送PVP地图战斗请求
	 * 
	 * @param list
	 *            玩家传输对象集合
	 * @param fightType  2-普通拦截,5-劫镖
	 * @return int 1-发送成功 0-发送失败
	 */
	public static int sendPvpMap(List<ComFightRequestReq> list,byte fightType) {
		if (list == null || list.size() == 0) {
			return 0;
		}

		if(GameValue.PVE_FIGHT_FLAG == 0)
		{
			return ErrorCode.MUTUAL_STATUS_ERROR;
		}
		
		Message message = new Message();

		FightStartReq fightStartReq = new FightStartReq();
		fightStartReq.setFightType((byte) fightType);
		fightStartReq.setCount(list.size());
		fightStartReq.setList(list);
		
		for (ComFightRequestReq comFightRequestReq : list) {
			comFightRequestReq.setUuid(UUID.randomUUID().toString());
		}

		RoomMessageHead head = new RoomMessageHead();
		head.setMsgType(RoomValue.MESSAGE_TYPE_FIGHT_START_FE05);
		message.setiRoomHead(head);
		message.setiRoomBody(fightStartReq);

		// 要求发送战斗服务器开始战斗
		Set<Entry<String, IoSession>> set = RoomMessageConfig.serverMap.entrySet();

		if (set == null || set.size() == 0) {
			logger.warn("sendPvpMapFight[1] : send to fight server error, there is no fight server available");
			return 0;
		}

		List<String> fullList = null;

		for (Entry<String, IoSession> entry : set) {
			if (entry.getKey().startsWith(GameServerName.ROOM_FIGHT_NAME)) {
				IoSession session = entry.getValue();

				if (session != null && session.isConnected()) {
					int fightServerCurrentCounts = session.getAttribute("fightServerCurrentCounts") != null ? (Integer) session.getAttribute("fightServerCurrentCounts") : 0;
					int fightServerMaxCounts = session.getAttribute("fightServerMaxCounts") != null ? (Integer) session.getAttribute("fightServerMaxCounts") : 0;

					if (fightServerCurrentCounts * 1.0 / fightServerMaxCounts > GameValue.FIGHT_SERVER_NORMAL_LIMIT) {
						// 高压力游戏服务器
						if (fullList == null) {
							fullList = new ArrayList<String>();
						}

						if (fightServerCurrentCounts < fightServerMaxCounts) {
							// 服务器压力不能超过总负荷
							fullList.add(entry.getKey());
						}
					} else {
						// 低压力游戏服务器
						session.write(message);
						session.setAttribute("fightServerCurrentCounts", fightServerCurrentCounts + 1);

						logger.info("sendPvpMapFight[1] : send to low fight server " + entry.getKey() + " message " + Integer.toHexString(RoomValue.MESSAGE_TYPE_FIGHT_START_FE05) + " successed");
						return 1;
					}
				}
			}
		}

		if (fullList != null) {
			for (String string : fullList) {
				IoSession session = RoomMessageConfig.serverMap.get(string);

				if (session != null && session.isConnected()) {
					session.write(message);
					int fightServerCurrentCounts = session.getAttribute("fightServerCurrentCounts") != null ? (Integer) session.getAttribute("fightServerCurrentCounts") : 0;
					session.setAttribute("fightServerCurrentCounts", fightServerCurrentCounts + 1);

					logger.warn("sendPvpMapFight[1] : send to high fight server " + string + " message " + Integer.toHexString(RoomValue.MESSAGE_TYPE_FIGHT_START_FE05) + " successed");
					return 1;
				}
			}
		}

		// 无战斗服务器可用
		logger.warn("sendPvpMapFight[1] : send to fight server error, fight server is full load or no fight server available");
		return 0;
	}

	/**
	 * 发送战斗服务器检查请求
	 * 
	 * @param req
	 */
	public static void sendFightServerCheck(CheckFightServerReq req) {
		IoSession session = RoomMessageConfig.serverMap.get(GameConfig.getInstance().getRoomId());

		if (session != null && session.isConnected()) {
			Message message = new Message();
			RoomMessageHead head = new RoomMessageHead();
			head.setMsgType(RoomValue.MESSAGE_TYPE_SEND_GAME_SERVER_CHECK_FE19);
			message.setiRoomHead(head);
			message.setiRoomBody(req);

			session.write(message);
		}
	}

	/**
	 * 取得随机管理服务器
	 * 
	 * @return 管理服务器IoSession
	 */
	public static IoSession getRandomRoomManager() {
		Set<Map.Entry<String, IoSession>> set = RoomMessageConfig.serverMap.entrySet();

		if (set.size() > 1) {
			int index = RandomUtil.getRandom(0, set.size() - 2);
			int temp = 0;

			for (Entry<String, IoSession> entry : set) {
				if (entry.getKey().equals(GameConfig.getInstance().getRoomId())) {
					continue;
				}

				if (temp++ == index) {
					return entry.getValue();
				}
			}
		}

		return RoomMessageConfig.serverMap.get(GameConfig.getInstance().getRoomId());
	}
}
