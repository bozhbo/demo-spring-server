package com.snail.webgame.game.pvp.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.engine.common.ServerName;
import com.snail.webgame.game.cache.FightCompetitionRankList;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.ServerMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.GameMessageHead;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.util.Command;
import com.snail.webgame.game.common.xml.cache.StageXMLInfoMap;
import com.snail.webgame.game.common.xml.cache.VipXMLInfoMap;
import com.snail.webgame.game.common.xml.info.StageXMLInfo;
import com.snail.webgame.game.common.xml.info.VipType;
import com.snail.webgame.game.common.xml.info.VipXMLInfo;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.CompetitionDao;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.CompetitiveLog;
import com.snail.webgame.game.protocal.appellation.service.TitleService;
import com.snail.webgame.game.protocal.arena.service.ArenaMgtService;
import com.snail.webgame.game.protocal.fight.competition.end.EndFightResp;
import com.snail.webgame.game.protocal.fight.competition.info.CompetitionAgainstInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.mutual.service.MutualService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.pvp.competition.end.ComFightEndRe;
import com.snail.webgame.game.pvp.competition.end.ComFightEndResp;
import com.snail.webgame.game.pvp.service.PvpFightEndService;

/**
 * 
 * 类介绍:竞技场战斗结束业务类
 *
 * @author zhoubo
 * @2015年6月15日
 */
public class CompetitionFightEndServiceImpl implements PvpFightEndService {

	private static CompetitionFightEndServiceImpl service = new CompetitionFightEndServiceImpl();

	private CompetitionFightEndServiceImpl() {

	}

	public static PvpFightEndService getInstance() {
		return service;
	}

	private static final Logger logger = LoggerFactory.getLogger("logs");

	@Override
	public void fightEnd(RoleInfo roleInfo, ComFightEndResp comFightEndResp, ComFightEndRe fightEndInfo) {
		CompetitiveLog competitiveLog = null;
		
		synchronized (roleInfo) {
			// 是否改变积分
			boolean isChangeValue = false;

			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();

			if (roleLoadInfo == null) {
				return;
			}

			if (roleLoadInfo.getInFight() != 2) {
				// 非战斗状态
				logger.warn("end Competition fight role " + roleInfo.getId() + " is not in fight");
				return;
			}

			if (!roleLoadInfo.getUuid().equals(fightEndInfo.getUuid())) {
				// 非当前战斗
				logger.warn("end Competition fight role " + roleInfo.getId() + " uuid is timeout");
				return;
			}

			competitiveLog = new CompetitiveLog();
			
			try {
				competitiveLog.setAccount(roleInfo.getAccount());
				competitiveLog.setRoleName(roleInfo.getRoleName());
				competitiveLog.setRoleId(roleInfo.getId());
				competitiveLog.setCreateTime(new Timestamp(System.currentTimeMillis()));

				competitiveLog.setBeforeRank(FightCompetitionRankList.getRoleIndex(roleInfo) + 1);
				competitiveLog.setBeforeStage(roleInfo.getCompetitionStage());
				competitiveLog.setBeforeStageValue(roleInfo.getCompetitionValue());

				competitiveLog.setUseEnergy(GameValue.COMPETITION_FIGHT_COST_ENERGY_VALUE);
				
				competitiveLog.setMatchRole(fightEndInfo.getEndCompetitionVo().getTargetNickName());
				
				HeroInfo heroInfo = HeroInfoMap.getMainHeroInfo(roleInfo);
				if (heroInfo != null) {
					competitiveLog.setHeroNo(heroInfo.getHeroNo());
				}
				
				List<HeroInfo> heroInfoList = HeroInfoMap.getFightDeployHero(roleInfo.getId());
				
				if (heroInfoList != null) {
					StringBuffer buffer = new StringBuffer();
					
					for (HeroInfo tempHeroInfo : heroInfoList) {
						if (tempHeroInfo != heroInfo && tempHeroInfo.getDeployStatus() > 0 && tempHeroInfo.getDeployStatus() < 6) {
							buffer.append(",").append(tempHeroInfo.getHeroNo());
						}
					}
					
					if (buffer.length() > 1) {
						competitiveLog.setHeroNos(buffer.toString().replaceFirst(",", ""));
					}
				}
			} catch (Exception e) {
				// 日志获取失败
			}
			
			RoleInfo targetRoleInfo = RoleInfoMap.getRoleInfoByName(fightEndInfo.getEndCompetitionVo().getTargetNickName());
			if (targetRoleInfo != null) {
				HeroInfo targetHeroInfo = HeroInfoMap.getMainHeroInfo(targetRoleInfo);
				if (targetHeroInfo != null) {
					competitiveLog.setTargetHeroNo(targetHeroInfo.getHeroNo());
				}
			}

			// 清除状态
			roleLoadInfo.setInFight((byte) 0);
			roleLoadInfo.setFightServer(null);
			roleLoadInfo.setUuid(null);

			if (fightEndInfo.getWinner() == 3) {
				// 平手不做结算
				logger.warn("end Competition fight role " + roleInfo.getId() + " is neck and neck");

				competitiveLog.setAfterRank(competitiveLog.getBeforeRank());
				competitiveLog.setAfterStage(competitiveLog.getBeforeStage());
				competitiveLog.setAfterStageValue(competitiveLog.getBeforeStageValue());
				competitiveLog.setComment("3");
			} else {
				CompetitionAgainstInfo competitionAgainstInfo = new CompetitionAgainstInfo();
				competitionAgainstInfo.setRoleName(fightEndInfo.getEndCompetitionVo().getTargetNickName());
				competitionAgainstInfo.setLevel(fightEndInfo.getEndCompetitionVo().getTargetLevel());
				competitionAgainstInfo.setMilitaryRank(fightEndInfo.getEndCompetitionVo().getTargetStage());
				competitionAgainstInfo.setIsWin(fightEndInfo.getWinner() == 2 ? (byte) 1 : (byte) 0);

				if (roleInfo.getCompetitionAgainstInfoList() == null) {
					roleInfo.setCompetitionAgainstInfoList(new ArrayList<CompetitionAgainstInfo>());
				}

				roleInfo.getCompetitionAgainstInfoList().add(competitionAgainstInfo);

				if (roleInfo.getCompetitionAgainstInfoList().size() > 20) {
					roleInfo.getCompetitionAgainstInfoList().remove(0);
				}

				EndFightResp resp = new EndFightResp();

				Map<String, Object> map = new HashMap<String, Object>();

				// 当前段位信息
				StageXMLInfo stageXMLInfo = StageXMLInfoMap.getStageXMLInfo(roleInfo.getCompetitionStage());
				int winPoint = stageXMLInfo.getWinPoint();
				int losePoint = stageXMLInfo.getLosePoint();

				if (fightEndInfo.getWinner() == 2) {
					// 胜利
					roleLoadInfo.setWinTimes(roleLoadInfo.getWinTimes() + 1);
					int torrentTimes = (roleInfo.getTorrentTimes() < 0 ? 0 : roleInfo.getTorrentTimes()) + 1;
					roleInfo.setTorrentTimes(torrentTimes > Byte.MAX_VALUE ? Byte.MAX_VALUE : (byte) torrentTimes);
					resp.setIsWin((byte) 1);
					map.put("winTimes", roleLoadInfo.getWinTimes());

					logger.info("end Competition fight role " + roleInfo.getId() + " was won");
				} else {
					// 失败
					int torrentTimes = (roleInfo.getTorrentTimes() > 0 ? 0 : roleInfo.getTorrentTimes()) + -1;
					roleInfo.setTorrentTimes(torrentTimes < Byte.MIN_VALUE ? Byte.MIN_VALUE : (byte) torrentTimes);
					roleLoadInfo.setLoseTimes(roleLoadInfo.getLoseTimes() + 1);
					map.put("loseTimes", roleLoadInfo.getLoseTimes());

					logger.info("end Competition fight role " + roleInfo.getId() + " was lost");
				}

				resp.setTargetRank(fightEndInfo.getEndCompetitionVo().getTargetStage());
				resp.setTargetLevel(fightEndInfo.getEndCompetitionVo().getTargetLevel());
				resp.setTargetRoleName(fightEndInfo.getEndCompetitionVo().getTargetNickName());

				// VIP增加的段位限制
				int vipPlus = 0;
				VipXMLInfo vipXMLInfo = VipXMLInfoMap.getVipXMLInfo(roleInfo.getVipLv());
				
				if (vipXMLInfo != null) {
					if (vipXMLInfo.getVipMap().get(VipType.KFJJCDW) != null) {
						vipPlus = vipXMLInfo.getVipMap().get(VipType.KFJJCDW).intValue();
					}
				}
				
				if (stageXMLInfo.getId() >= GameValue.XIAO_WEI_1_NO){
					// 校尉1或以上段位
					if (roleInfo.getCompetitionValue() >= stageXMLInfo.getValue() && StageXMLInfoMap.getStageXMLInfo((byte) (stageXMLInfo.getId() + 1)) != null) {
						// 积分到达当前段位峰值并且未到达顶级段位，开始晋级战
						if (fightEndInfo.getWinner() == 2) {
							// 胜利
							roleLoadInfo.setStageWinTimes((byte) (roleLoadInfo.getStageWinTimes() + 1));
							roleLoadInfo.setStageState((byte) (roleLoadInfo.getStageState() | (byte) Math.pow(2, roleLoadInfo.getStageLoseTimes() + roleLoadInfo.getStageWinTimes() - 1)));
							map.put("stageWinTimes", roleLoadInfo.getStageWinTimes());
							map.put("stageState", roleLoadInfo.getStageState());
						} else {
							// 失败
							roleLoadInfo.setStageLoseTimes((byte) (roleLoadInfo.getStageLoseTimes() + 1));
							map.put("stageLoseTimes", roleLoadInfo.getStageLoseTimes());
						}

						resp.setStageWinTimes(roleLoadInfo.getStageWinTimes());
						resp.setStageLoseTimes(roleLoadInfo.getStageLoseTimes());
						resp.setStageState(roleLoadInfo.getStageState());

						if (roleLoadInfo.getStageLoseTimes() * 2 > GameValue.COMPETITION_RISE_COMPLETE_TIMES) {
							// 晋级战失败
							roleInfo.setCompetitionValue((int) (stageXMLInfo.getValue() * GameValue.COMPETITION_DOWN_VALUE_PERCENT));
							roleInfo.setCompetitionTime(System.currentTimeMillis());
							roleLoadInfo.setStageWinTimes((byte) 0);
							roleLoadInfo.setStageLoseTimes((byte) 0);
							roleLoadInfo.setStageState((byte) 0);

							map.put("competitionValue", roleInfo.getCompetitionValue());
							map.put("stageWinTimes", roleLoadInfo.getStageWinTimes());
							map.put("stageLoseTimes", roleLoadInfo.getStageLoseTimes());
							map.put("stageState", roleLoadInfo.getStageState());
							map.put("competitionTime", roleInfo.getCompetitionTime());

							isChangeValue = true;
						}

						if (roleLoadInfo.getStageWinTimes() * 2 > GameValue.COMPETITION_RISE_COMPLETE_TIMES) {
							// 晋级战胜利
							StageXMLInfo nextStageXMLInfo = StageXMLInfoMap.getStageXMLInfo((byte) (stageXMLInfo.getId() + 1));

							if (nextStageXMLInfo == null) {
								// 到达顶级段位
								roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint);
								roleInfo.setCompetitionTime(System.currentTimeMillis());

								map.put("competitionValue", roleInfo.getCompetitionValue());
								map.put("competitionTime", roleInfo.getCompetitionTime());

								isChangeValue = true;
							} else {
								// 升级段位
								roleInfo.setCompetitionStage(nextStageXMLInfo.getId());
								TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_KFJJC_LV, roleInfo.getCompetitionStage(), roleInfo);
								roleInfo.setCompetitionValue((int) (nextStageXMLInfo.getValue() * GameValue.COMPETITION_RISE_VALUE_PERCENT));
								roleInfo.setCompetitionTime(System.currentTimeMillis());

								if ((roleInfo.getStageAward() & nextStageXMLInfo.getStageBit()) == 0) {
									// 未发放奖励
									roleInfo.setStageAward(roleInfo.getStageAward() | nextStageXMLInfo.getStageBit());
									map.put("stageAward", roleInfo.getStageAward());
								}

								map.put("competitionStage", roleInfo.getCompetitionStage());
								map.put("competitionValue", roleInfo.getCompetitionValue());
								map.put("competitionTime", roleInfo.getCompetitionTime());

								isChangeValue = true;
							}

							roleLoadInfo.setStageWinTimes((byte) 0);
							roleLoadInfo.setStageLoseTimes((byte) 0);
							roleLoadInfo.setStageState((byte) 0);

							map.put("stageWinTimes", roleLoadInfo.getStageWinTimes());
							map.put("stageLoseTimes", roleLoadInfo.getStageLoseTimes());
							map.put("stageState", roleLoadInfo.getStageState());
						}
					} else {
						// 未到达晋级战积分或已达到顶级段位,胜利增加积分,失败扣除积分
						if (fightEndInfo.getWinner() == 2) {
							// 胜利,增加积分
							if (StageXMLInfoMap.getStageXMLInfo((byte) (stageXMLInfo.getId() + 1)) == null) {
								// 已达到顶级段位,积分不封顶
								if (isDoubleValue()) {
									roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint * 2);
								} else {
									roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint);
								}

								roleInfo.setCompetitionTime(System.currentTimeMillis());
								map.put("competitionValue", roleInfo.getCompetitionValue());
								map.put("competitionTime", roleInfo.getCompetitionTime());

								isChangeValue = true;
							} else {
								// 一般段位
								if (isDoubleValue()) {
									roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint * 2 > stageXMLInfo.getValue() ? stageXMLInfo.getValue() : roleInfo.getCompetitionValue() + winPoint
											* 2);
								} else {
									roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint > stageXMLInfo.getValue() ? stageXMLInfo.getValue() : roleInfo.getCompetitionValue() + winPoint);
								}

								roleInfo.setCompetitionTime(System.currentTimeMillis());
								map.put("competitionValue", roleInfo.getCompetitionValue());
								map.put("competitionTime", roleInfo.getCompetitionTime());

								isChangeValue = true;
							}
						} else {
							// 失败,扣除积分
							if (vipPlus > 0 && vipPlus >= roleInfo.getCompetitionStage()) {
								// VIP 不扣除积分
							} else {
								if (roleInfo.getCompetitionValue() <= 0) {
									// 积分过少，回到上一段位
									if (stageXMLInfo.getId() == GameValue.XIAO_WEI_1_NO) {
										// 校尉1，不能再降
										roleInfo.setCompetitionValue((int) 0);
										roleInfo.setCompetitionTime(System.currentTimeMillis());
										map.put("competitionValue", roleInfo.getCompetitionValue());
										map.put("competitionTime", roleInfo.getCompetitionTime());

										isChangeValue = true;
									} else {
										// 回上一段位
										StageXMLInfo previousStageXMLInfo = StageXMLInfoMap.getStageXMLInfo((byte) (stageXMLInfo.getId() - 1));
										roleInfo.setCompetitionStage(previousStageXMLInfo.getId());
										TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_KFJJC_LV, roleInfo.getCompetitionStage(), roleInfo);
										roleInfo.setCompetitionValue((int) (previousStageXMLInfo.getValue() * GameValue.COMPETITION_DOWN_VALUE_PERCENT));
										roleInfo.setCompetitionTime(System.currentTimeMillis());
										map.put("competitionStage", roleInfo.getCompetitionStage());
										map.put("competitionValue", roleInfo.getCompetitionValue());
										map.put("competitionTime", roleInfo.getCompetitionTime());

										isChangeValue = true;
									}
								} else {
									// 积分大于0，扣除积分
									roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() - losePoint < 0 ? 0 : roleInfo.getCompetitionValue() - losePoint);
									roleInfo.setCompetitionTime(System.currentTimeMillis());
									map.put("competitionValue", roleInfo.getCompetitionValue());
									map.put("competitionTime", roleInfo.getCompetitionTime());

									isChangeValue = true;
								}
							}
						}
					}
				} else {
					// 低于校尉1,只增加积分
					if (fightEndInfo.getWinner() == 2) {
						// 胜利,增加积分
						if (isDoubleValue()) {
							roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint * 2);
						} else {
							roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() + winPoint);
						}

						roleInfo.setCompetitionTime(System.currentTimeMillis());
						map.put("competitionValue", roleInfo.getCompetitionValue());
						map.put("competitionTime", roleInfo.getCompetitionTime());

						isChangeValue = true;

						if (roleInfo.getCompetitionValue() >= stageXMLInfo.getValue()) {
							// 积分到达,升级段位
							roleInfo.setCompetitionStage((byte) (stageXMLInfo.getId() + 1));
							TitleService.achieveTitleCheck(GameValue.APPELLATION_TYPE_KFJJC_LV, roleInfo.getCompetitionStage(), roleInfo);
							roleInfo.setCompetitionValue(roleInfo.getCompetitionValue() - stageXMLInfo.getValue());
							roleInfo.setCompetitionTime(System.currentTimeMillis());

							StageXMLInfo nextStageXMLInfo = StageXMLInfoMap.getStageXMLInfo((byte) (stageXMLInfo.getId() + 1));

							if ((roleInfo.getStageAward() & nextStageXMLInfo.getStageBit()) == 0) {
								// 未发放奖励
								roleInfo.setStageAward(roleInfo.getStageAward() | nextStageXMLInfo.getStageBit());
								map.put("stageAward", roleInfo.getStageAward());
							}

							map.put("competitionStage", roleInfo.getCompetitionStage());
							map.put("competitionValue", roleInfo.getCompetitionValue());
							map.put("competitionTime", roleInfo.getCompetitionTime());
						}
					}
				}

				if (map.size() > 0) {
					map.put("id", roleInfo.getId());

					if (map.get("winTimes") == null) {
						map.put("winTimes", roleLoadInfo.getWinTimes());
					}

					if (map.get("loseTimes") == null) {
						map.put("loseTimes", roleLoadInfo.getLoseTimes());
					}

					if (map.get("competitionValue") == null) {
						map.put("competitionValue", roleInfo.getCompetitionValue());
					}

					if (map.get("competitionStage") == null) {
						map.put("competitionStage", roleInfo.getCompetitionStage());
					}

					if (map.get("stageWinTimes") == null) {
						map.put("stageWinTimes", roleLoadInfo.getStageWinTimes());
					}

					if (map.get("stageLoseTimes") == null) {
						map.put("stageLoseTimes", roleLoadInfo.getStageLoseTimes());
					}

					if (map.get("competitionTime") == null) {
						map.put("competitionTime", roleInfo.getCompetitionTime());
					}

					if (map.get("stageState") == null) {
						map.put("stageState", roleLoadInfo.getStageState());
					}

					if (map.get("stageAward") == null) {
						map.put("stageAward", roleInfo.getStageAward());
					}

					// 更新角色段位积分信息
					CompetitionDao.getInstance().updateRolesCompetitionValue(map);
				}

				if (isChangeValue) {
					// 全局排行
					FightCompetitionRankList.addSort(roleInfo);
				}

				if (fightEndInfo.getWinner() == 2) {
					// 胜利添加翻牌奖励
					List<BattlePrize> fpPrizeList = new ArrayList<BattlePrize>();
					ArenaMgtService.dealDropInfo(ActionType.action352.getType(), roleInfo, new ArrayList<BattlePrize>(), fpPrizeList,false);

					resp.setFpPrize(fpPrizeList);
					resp.setFpPrizeNum(fpPrizeList.size());
				}

				resp.setCompetitionStage(roleInfo.getCompetitionStage());
				resp.setCompetitionValue(roleInfo.getCompetitionValue());

				// 任务检测-红点检测
				boolean isRedQuest = QuestService.checkQuest(roleInfo, ActionType.action352.getType(), null, true, false);
				boolean isRed = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, RedPointMgtService.LISTENING_ARENA_FIGHT_END);

				// 红点推送
				if (isRedQuest || isRed) {
					RedPointMgtService.pop(roleInfo.getId());
				}

				if (roleInfo.getLoginStatus() == 1) {
					// 登录状态, 发送竞技场结算数据
					IoSession session = ServerMap.getServerSession(ServerName.GATE_SERVER_NAME + "-" + roleInfo.getGateServerId());

					if (session != null && session.isConnected()) {
						org.epilot.ccf.core.protocol.Message gameMessage = new org.epilot.ccf.core.protocol.Message();
						GameMessageHead head = new GameMessageHead();
						head.setMsgType(Command.FIGHT_END_COMPETITION_RESP);
						head.setUserID0((int) roleInfo.getId());
						gameMessage.setHeader(head);
						gameMessage.setBody(resp);
						session.write(gameMessage);
					}
				}

				competitiveLog.setAfterRank(FightCompetitionRankList.getRoleIndex(roleInfo) + 1);
				competitiveLog.setAfterStage(roleInfo.getCompetitionStage());
				competitiveLog.setAfterStageValue(roleInfo.getCompetitionValue());
				competitiveLog.setBattleResult(fightEndInfo.getWinner());
			}
		}

		// 记录日志
		try{
			GameLogService.insertCompetitiveLog(competitiveLog);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 检查是否处于双倍积分时段
	 * 
	 * @return	boolean true-双倍积分时段 false-非双倍积分时段
	 */
	private boolean isDoubleValue() {
		return !MutualService.getMutualService().notMatchTime(GameValue.COMPETITION_DOUBLE_START_TIME, GameValue.COMPETITION_DOUBLE_END_TIME);
	}
}
