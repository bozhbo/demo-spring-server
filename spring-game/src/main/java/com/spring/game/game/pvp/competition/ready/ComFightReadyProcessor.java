package com.snail.webgame.game.pvp.competition.ready;

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
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.fight.competition.ready.FightReadyResp;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.cache.SceneInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.info.RolePoint;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;

/**
 * 
 * 类介绍:战斗准备就绪处理类，通知客户端开始战斗
 *
 * @author zhoubo
 * @2014-11-25
 */
public class ComFightReadyProcessor extends BaseProcessor {

	private static final Logger logger = LoggerFactory.getLogger("logs");

	public ComFightReadyProcessor() {
		super();
	}

	public ComFightReadyProcessor(Class<? extends IRoomBody> c) {
		super(c);
	}

	@Override
	public void processor(Message message) {
		ComFightReadyResp comFightReadyResp = (ComFightReadyResp) message.getiRoomBody();

		if (logger.isInfoEnabled()) {
			logger.info("ComFightReadyProcessor : result = " + comFightReadyResp.getResult() + " server = " + comFightReadyResp.getServer() + " roles = " + comFightReadyResp.getRoleInfs());
		}

		if (comFightReadyResp.getRoleInfs() == null || "".equals(comFightReadyResp.getRoleInfs())) {
			if (logger.isWarnEnabled()) {
				logger.warn("ComFightReadyProcessor : roleInfs is null");
			}

			return;
		}

		String[] roleInfos = comFightReadyResp.getRoleInfs().split(",");
		boolean broadcastFlag = false;// 分别刷新给战斗人，因为公共方法brocastRolePointStatus过滤掉了
		
		for (String roleInfoStr : roleInfos) {
			String[] roleInfoArray = roleInfoStr.split(":");
			int roleId = 0;
			String uuid = "";

			try {
				roleId = Integer.valueOf(roleInfoArray[0]);
			} catch (Exception e) {
				if (logger.isWarnEnabled()) {
					logger.warn("ComFightReadyProcessor : roleId is not a number");
				}

				continue;
			}

			if (roleInfoArray.length > 1) {
				uuid = roleInfoArray[1];
			}

			RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);

			if (roleInfo != null) {
				synchronized (roleInfo) {
					RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

					if (roleLoadInfo == null) {
						return;
					}
					
					if (comFightReadyResp.getFightType() == 1) {
						// 竞技场PVP战斗
						if (roleLoadInfo.getInFight() != 1) {
							// 非报名状态都不能进入战斗
							continue;
						}
						
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action353.getType(), 0, "");
						
					} else if (comFightReadyResp.getFightType() == 2) {
						// 大地图战斗
						if (roleInfo.getLoginStatus() == 1 && comFightReadyResp.getResult() == 1) {
							// 可开始战斗
							MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
							
							if (pointInfo != null) {
								pointInfo.setStatus((byte) 1);

								// 广播周围玩家某单位坐标及状态修改
								SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
								broadcastFlag = true;
							} else {
								if (logger.isWarnEnabled()) {
									logger.warn("ComFightReadyProcessor warn : roleId = " + roleId + " pointInfo is null");
								}
							}
						}
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action356.getType(), 0, "");
						
					} else if (comFightReadyResp.getFightType() == 3) {
						// 对攻战斗
						if (roleLoadInfo.getInFight() == 6 || roleLoadInfo.getInFight() == 8 || roleLoadInfo.getInFight() == 9) {
							// 报名状态
						} else if (roleLoadInfo.getInFight() == 0) {
							if (System.currentTimeMillis() - roleLoadInfo.getCancelTime() > 2000) {
								// 取消超过2秒
								continue;
							}
						} else {
							// 非报名状态都不能进入战斗
							if (logger.isWarnEnabled()) {
								logger.warn("ComFightReadyProcessor : mutual fight error, inFight = " + roleLoadInfo.getInFight());
							}
							
							continue;
						}
						
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action382.getType(), 0, "");
					} else if (comFightReadyResp.getFightType() == 6) {
						// 组队副本
						if (roleLoadInfo.getInFight() == 11) {
							// 报名状态
						} else if (roleLoadInfo.getInFight() == 0) {
							if (System.currentTimeMillis() - roleLoadInfo.getCancelTime() > 2000) {
								// 取消超过2秒
								continue;
							}
						} else {
							// 非报名状态都不能进入战斗
							if (logger.isWarnEnabled()) {
								logger.warn("ComFightReadyProcessor : mutual fight error, inFight = " + roleLoadInfo.getInFight());
							}
							
							continue;
						}
						
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action489.getType(), 0, "");
					} else if (comFightReadyResp.getFightType() == 7) {
						// 竞技场3V3战斗
						if (roleLoadInfo.getInFight() == 13 || roleLoadInfo.getInFight() == 14 || roleLoadInfo.getInFight() == 15) {
							// 报名状态
						} else if (roleLoadInfo.getInFight() == 0) {
							if (System.currentTimeMillis() - roleLoadInfo.getCancelTime() > 2000) {
								// 取消超过2秒
								continue;
							}
						} else {
							// 非报名状态都不能进入战斗
							if (logger.isWarnEnabled()) {
								logger.warn("ComFightReadyProcessor : mutual fight error, inFight = " + roleLoadInfo.getInFight());
							}
							
							continue;
						}
						
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action487.getType(), 0, "");
					}
					else if(comFightReadyResp.getFightType() == 5)
					{
						// 大地图战斗
						if (roleInfo.getLoginStatus() == 1 && comFightReadyResp.getResult() == 1) {
							// 可开始战斗
							MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
							
							if (pointInfo != null) {
								if(pointInfo.getStatus() == 4)
								{
									//押镖人
									pointInfo.setStatus((byte) 6);
								}
								else if(pointInfo.getStatus() == 7)
								{
									// 护镖人
									RoleInfo yabiaoRoleInfo = RoleInfoMap.getRoleInfo(roleLoadInfo.getYabiaoFriendRoleId());
									if(yabiaoRoleInfo != null)
									{
										MapRolePointInfo yabiaoPointInfo = MapRoleInfoMap.getMapPointInfo(yabiaoRoleInfo.getId());
										if(yabiaoPointInfo != null)
										{
											yabiaoPointInfo.setStatus((byte) 5);
											
											SceneService1.brocastRolePointStatus(yabiaoPointInfo,yabiaoRoleInfo);
										}
										else
										{
											if (logger.isWarnEnabled()) {
												logger.warn("ComFightReadyProcessor warn : roleId = " + roleId + " pointInfo is null");
											}
										}
										// 护镖人专属状态 -->  8
										pointInfo.setStatus((byte) 8);
									}
								}
								else
								{
									//劫镖人
									pointInfo.setStatus((byte) 1);
								}
								
								// 广播周围玩家某单位坐标及状态修改
								SceneService1.brocastRolePointStatus(pointInfo,roleInfo);
								broadcastFlag = true;
							}
						}
						
						GameLogService.insertPlayActionLog(roleInfo, ActionType.action388.getType(), 0, "");
					
					}

					int result = 1;

					if ((comFightReadyResp.getFightType() == 1 || comFightReadyResp.getFightType() == 3) && comFightReadyResp.getResult() == 1) {
						// 竞技场PVP战斗/对攻战, 检查精力值
						if (!RoleService.subRoleRoleResource(ActionType.action355.getType(), roleInfo, ConditionType.TYPE_ENERGY, GameValue.COMPETITION_FIGHT_COST_ENERGY_VALUE , null)) {
							// 精力值不够
							result = ErrorCode.ROLE_ENERGY_ERROR_1;
						}
					}

					if (roleInfo.getLoginStatus() == 1) {
						// 在线角色发送战斗开始通知
						IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());

						if (session != null && session.isConnected()) {
							FightReadyResp resp = new FightReadyResp();

							if (comFightReadyResp.getResult() == 1) {
								if (result == 1) {
									resp.setResult(1);
									resp.setFightType(comFightReadyResp.getFightType());
									resp.setUuid(uuid);
									resp.setServer(comFightReadyResp.getServer());
								} else {
									resp.setResult(result);
								}
							} else {
								if (comFightReadyResp.getResult() == 2) {
									resp.setResult(ErrorCode.FIGHT_FIGHT_SERVER_INAVAILABLE_ERROR);
								} else {
									resp.setResult(ErrorCode.FIGHT_MATCH_SERVER_IS_BUSY_ERROR);
								}
							}

							logger.info("FightReadyResp :" + resp.toString());
							org.epilot.ccf.core.protocol.Message gameMessage = new org.epilot.ccf.core.protocol.Message();
							GameMessageHead head = new GameMessageHead();
							head.setMsgType(Command.FIGHT_READY_COMPETITION_RESP);
							head.setUserID0((int) roleInfo.getId());
							gameMessage.setHeader(head);
							gameMessage.setBody(resp);
							session.write(gameMessage);
						}
					}

					if (comFightReadyResp.getFightType() == 1) {
						// 竞技场PVP战斗, 修改状态
						if (comFightReadyResp.getResult() == 1 && result == 1) {
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 2);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
							
							//从主城场景移除
							RolePoint rolePoint1 = roleInfo.getRolePoint();
							if(rolePoint1 != null)
							{
								RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId,rolePoint1.getNo(),rolePoint1.getSceneId());
								if(rolePoint != null)
								{
									SceneService1.notifyDelAIPoint(rolePoint);
								}
							}
							
							// 记录日志
							GameLogService.insertPlayActionLog(roleInfo, ActionType.action353.getType(), 0, "");
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					} else if (comFightReadyResp.getFightType() == 2) {
						// 地图PVP战斗, 修改状态
						if (comFightReadyResp.getResult() == 1) {
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 4);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					} else if (comFightReadyResp.getFightType() == 3) {
						// 对攻战战斗, 修改状态
						if (comFightReadyResp.getResult() == 1 && result == 1) {
							// 记录日志
							// GameLogService.insertPlayActionLog(roleInfo, ActionType.action382.getType(), 0, roleLoadInfo.getInFight() + "");
							
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 7);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
							
							//从主城场景移除
							RolePoint rolePoint1 = roleInfo.getRolePoint();
							if(rolePoint1 != null)
							{
								RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId,rolePoint1.getNo(),rolePoint1.getSceneId());
								if(rolePoint != null)
								{
									SceneService1.notifyDelAIPoint(rolePoint);
								}
							}
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					} else if (comFightReadyResp.getFightType() == 5) {
						// 地图劫镖PVP战斗, 修改状态
						if (comFightReadyResp.getResult() == 1) {
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 4);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					} else if (comFightReadyResp.getFightType() == 6) {
						// 组队副本战斗, 修改状态
						if (comFightReadyResp.getResult() == 1 && result == 1) {
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 10);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
							
							//从主城场景移除
							RolePoint rolePoint1 = roleInfo.getRolePoint();
							if(rolePoint1 != null)
							{
								RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId,rolePoint1.getNo(),rolePoint1.getSceneId());
								if(rolePoint != null)
								{
									SceneService1.notifyDelAIPoint(rolePoint);
								}
							}
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					} else if (comFightReadyResp.getFightType() == 7) {
						// 3V3战斗, 修改状态
						if (comFightReadyResp.getResult() == 1 && result == 1) {
							// 设置战斗状态
							roleLoadInfo.setInFight((byte) 12);
							roleLoadInfo.setFightStartTime(System.currentTimeMillis());
							roleLoadInfo.setFightServer(comFightReadyResp.getServer());
							roleLoadInfo.setUuid(uuid);
							
							//从主城场景移除
							RolePoint rolePoint1 = roleInfo.getRolePoint();
							if(rolePoint1 != null)
							{
								RolePoint rolePoint = SceneInfoMap.getRolePoint(roleId,rolePoint1.getNo(),rolePoint1.getSceneId());
								if(rolePoint != null)
								{
									SceneService1.notifyDelAIPoint(rolePoint);
								}
							}
						} else {
							// 匹配失败，恢复正常状态
							roleLoadInfo.setInFight((byte) 0);
							roleLoadInfo.setFightStartTime(0);
							roleLoadInfo.setFightServer(null);
							roleLoadInfo.setUuid(null);
						}
					}
				}
			}
			
			if(broadcastFlag == true){
				MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo(roleId);
				
				if (pointInfo != null) {
					
					for (String roleInfoS : roleInfos) {
						String[] roleInfoArray1 = roleInfoS.split(":");
						int roleId1 = 0;
	
						try {
							roleId1 = Integer.valueOf(roleInfoArray1[0]);
						} catch (Exception e) {
							if (logger.isWarnEnabled()) {
								logger.warn("ComFightReadyProcessor : roleId is not a number");
							}
	
							continue;
						}
						SceneService1.broadPointStatus(RoleInfoMap.getRoleInfo(roleId1), pointInfo);
					}
				}
				broadcastFlag = false;
			}
		}
	}

	@Override
	public int getMsgType() {
		return RoomValue.MESSAGE_TYPE_SEND_GAME_SERVER_START_FE17;
	}
}
