package com.snail.webgame.game.protocal.mine.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.MineInfoMap;
import com.snail.webgame.game.cache.RoleClubInfoMap;
import com.snail.webgame.game.cache.RoleClubMemberInfoMap;
import com.snail.webgame.game.cache.RoleFriendMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.dao.MineDAO;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.MineHelpRole;
import com.snail.webgame.game.info.MineInfo;
import com.snail.webgame.game.info.MinePrize;
import com.snail.webgame.game.info.MineRole;
import com.snail.webgame.game.info.RoleClubInfo;
import com.snail.webgame.game.info.RoleClubMemberInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.info.log.MineFightLog;
import com.snail.webgame.game.protocal.challenge.service.ChallengeService;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.fight.startFight.StartFightPosInfo;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.hero.service.HeroService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.mine.buy.BuyMineNumResp;
import com.snail.webgame.game.protocal.mine.collect.CollectMineResp;
import com.snail.webgame.game.protocal.mine.deploy.MineDeployReq;
import com.snail.webgame.game.protocal.mine.deploy.MineDeployResp;
import com.snail.webgame.game.protocal.mine.detail.QueryMineDetailReq;
import com.snail.webgame.game.protocal.mine.detail.QueryMineDetailResp;
import com.snail.webgame.game.protocal.mine.drop.DropMineReq;
import com.snail.webgame.game.protocal.mine.drop.DropMineResp;
import com.snail.webgame.game.protocal.mine.getPrize.MineGetPrizeReq;
import com.snail.webgame.game.protocal.mine.getPrize.MineGetPrizeResp;
import com.snail.webgame.game.protocal.mine.help.MineHelpReq;
import com.snail.webgame.game.protocal.mine.help.MineHelpResp;
import com.snail.webgame.game.protocal.mine.hero.QueryMineHeroReq;
import com.snail.webgame.game.protocal.mine.invite.InviteMineHelpReq;
import com.snail.webgame.game.protocal.mine.invite.InviteMineHelpResp;
import com.snail.webgame.game.protocal.mine.loot.LootMineResp;
import com.snail.webgame.game.protocal.mine.query.MineDefendLogRe;
import com.snail.webgame.game.protocal.mine.query.MineInfoRe;
import com.snail.webgame.game.protocal.mine.query.MineRoleRe;
import com.snail.webgame.game.protocal.mine.query.QueryMineResp;
import com.snail.webgame.game.protocal.mine.scene.QuerySceneMineResp;
import com.snail.webgame.game.protocal.mine.scene.SceneMineRe;
import com.snail.webgame.game.protocal.push.service.PushMgrService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.scene.cache.MapRoleInfoMap;
import com.snail.webgame.game.protocal.scene.info.MapRolePointInfo;
import com.snail.webgame.game.protocal.scene.queryOtherAI.OtherHeroInfo;
import com.snail.webgame.game.protocal.scene.queryOtherAI.QueryOtherAIResp;
import com.snail.webgame.game.protocal.scene.sceneRefre.SceneRefreService;
import com.snail.webgame.game.protocal.scene.sys.SceneService1;
import com.snail.webgame.game.xml.cache.FuncOpenXMLInfoMap;
import com.snail.webgame.game.xml.cache.GoldBuyXMLInfoMap;
import com.snail.webgame.game.xml.cache.MineXMLInfoMap;
import com.snail.webgame.game.xml.info.FuncOpenXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLInfo;
import com.snail.webgame.game.xml.info.GoldBuyXMLPro;
import com.snail.webgame.game.xml.info.MineXMLInfo;
import com.snail.webgame.game.xml.info.PushXMLInfo;

public class MineMgtService {

	/**
	 * 查询自己相关矿信息
	 * @param postions
	 * @return
	 */
	public QueryMineResp queryMine(int roleId) {
		QueryMineResp resp = new QueryMineResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.QUERY_MINE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.QUERY_MINE_ERROR_2);
				return resp;
			}
			resp.setMineNum(roleLoadInfo.getTodayMineNum());
			resp.setMineLimit(MineService.getMineLimit(roleLoadInfo));
			resp.setBuyMine(roleLoadInfo.getTodayBuyMine());
		}

		long now = System.currentTimeMillis();
		List<MineInfoRe> list = new ArrayList<MineInfoRe>();
		List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();
		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		for (MineInfo mineInfo : map.values()) {
			synchronized (mineInfo) {
				MineInfoRe re = null;
				for (MineRole mineRole : mineInfo.getRoles().values()) {
					re = MineService.getMineInfoRe(roleInfo, mineInfo, mineRole);
					if (re != null) {
						long endTime = re.getZlDetail().getEndTime();
						if (endTime <= now) {
							endlist.add(re);
						} else {
							list.add(re);
						}
					}
				}
			}
		}

		Map<Integer, MinePrize> prizeMap = MineInfoMap.getMinePrizeMap(roleId);
		if (prizeMap != null && prizeMap.size() > 0) {
			MineInfoRe re = null;
			for (MinePrize prize : prizeMap.values()) {
				re = MineService.getMineInfoRe(roleInfo, prize);
				if (re != null) {
					endlist.add(re);
				}
			}
		}

		List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();
		for (MineFightLog log : roleInfo.getMineLogs()) {
			defendList.add(MineService.getMineDefendLogRe(roleInfo, log));
		}

		// 红点检测
		RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_MINE_TYPE);

		resp.setResult(1);
		resp.setIdStr("");
		resp.setCount(list.size());
		resp.setList(list);

		resp.setSize(endlist.size());
		resp.setEndlist(endlist);

		resp.setDefendCount(defendList.size());
		resp.setDefendList(defendList);
		return resp;

	}

	/**
	 * 刷新大地图矿厂信息
	 * @param postions
	 * @return
	 */
	public QuerySceneMineResp querySceneMine(String mineIdStr) {
		QuerySceneMineResp resp = new QuerySceneMineResp();
		resp.setResult(1);
		resp.setIdStr(mineIdStr);
		List<SceneMineRe> list = MineService.getSceneMineReList(mineIdStr);
		resp.setCount(list.size());
		resp.setList(list);
		return resp;
	}

	/**
	 * 查询矿占领信息
	 * @param req
	 * @return
	 */
	public QueryMineDetailResp queryMineDetail(QueryMineDetailReq req) {
		QueryMineDetailResp resp = new QueryMineDetailResp();
		int mineId = req.getMineId();
		MineInfo mineInfo = MineInfoMap.getMineInfo(mineId);
		if (mineInfo == null) {
			resp.setResult(ErrorCode.QUERY_MINE_DETAIL_ERROR_1);
			return resp;
		}
		synchronized (mineInfo) {
			if (mineInfo.isClosed()) {
				resp.setResult(ErrorCode.QUERY_MINE_DETAIL_ERROR_2);
				return resp;
			}
			List<MineRoleRe> list = new ArrayList<MineRoleRe>();
			MineRoleRe re = null;
			for (MineRole mineRole : mineInfo.getRoles().values()) {
				if (mineRole.isEnd()) {
					continue;
				}
				re = MineService.getMineRoleRe(mineRole);
				if (re != null) {
					list.add(re);
				}
			}

			resp.setResult(1);
			resp.setCount(list.size());
			resp.setList(list);
			return resp;
		}
	}

	/**
	 * 查询可以开采的矿列表
	 * @param roleId
	 * @return
	 */
	public CollectMineResp queryMineCollect(int roleId) {
		CollectMineResp resp = new CollectMineResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.QUERY_MINE_COLLECT_ERROR_1);
			return resp;
		}
		List<SceneMineRe> list = new ArrayList<SceneMineRe>();
		List<MineInfo> total = new ArrayList<MineInfo>();
		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		for (MineInfo mineInfo : map.values()) {
			synchronized (mineInfo) {
				if (mineInfo.isCanCollect() && mineInfo.getMineRoleZLAndHelpbyRoleId(roleId) == null) {
					total.add(mineInfo);
				}
			}
		}
		int getNum = GameValue.QUERY_MINE_COLLECT_SHOW_LIMEIT;
		if (total.size() <= getNum) {
			for (MineInfo mineInfo : total) {
				list.add(MineService.getSceneMineRe(mineInfo));
			}
			Collections.shuffle(list);
		} else {
			int indexs[] = RandomUtil.getRandomData(getNum, total.size());
			for (int index : indexs) {
				MineInfo mineInfo = total.get(index);
				if (mineInfo != null) {
					synchronized (mineInfo) {
						list.add(MineService.getSceneMineRe(mineInfo));
					}
				}
			}
		}

		resp.setResult(1);
		resp.setCount(list.size());
		resp.setList(list);
		return resp;
	}

	/**
	 * 查询可以抢夺的矿列表
	 * @param roleId
	 * @return
	 */
	public LootMineResp queryMineLoot(int roleId) {
		LootMineResp resp = new LootMineResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.QUERY_MINE_LOOT_ERROR_1);
			return resp;
		}
		List<MineInfoRe> list = new ArrayList<MineInfoRe>();
		List<MineRole> total = new ArrayList<MineRole>();
		Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
		for (MineInfo mineInfo : map.values()) {
			synchronized (mineInfo) {
				if (mineInfo.isClosed()) {
					// 开采结束
					continue;
				}
				if (mineInfo.getMineRoleZLAndHelpbyRoleId(roleId) != null) {
					continue;
				}
				for (MineRole mineRole : mineInfo.getRoles().values()) {
					if (mineRole.isCanLoot()) {
						total.add(mineRole);
					}
				}
			}
		}
		int getNum = GameValue.QUERY_MINE_LOOT_SHOW_LIMEIT;
		if (total.size() <= getNum) {
			MineInfo mineInfo = null;
			for (MineRole info : total) {
				mineInfo = MineInfoMap.getMineInfo(info.getMineId());
				if (mineInfo != null) {
					synchronized (mineInfo) {
						MineInfoRe re = MineService.getMineInfoRe(null, mineInfo, info);
						if (re != null) {
							list.add(re);
						}
					}
				}
			}
			Collections.shuffle(list);
		} else {
			int indexs[] = RandomUtil.getRandomData(getNum, total.size());
			MineInfo mineInfo = null;
			for (int index : indexs) {
				MineRole info = total.get(index);
				if (info != null) {
					mineInfo = MineInfoMap.getMineInfo(info.getMineId());
					if (mineInfo != null) {
						synchronized (mineInfo) {
							MineInfoRe re = MineService.getMineInfoRe(null, mineInfo, info);
							if (re != null) {
								list.add(re);
							}
						}
					}
				}
			}
		}

		resp.setResult(1);
		resp.setCount(list.size());
		resp.setList(list);
		return resp;
	}

	/**
	 * 领取矿采集奖励
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MineGetPrizeResp getPrize(int roleId, MineGetPrizeReq req) {
		MineGetPrizeResp resp = new MineGetPrizeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GET_PRIZE_MINE_ERROR_1);
			return resp;
		}
		List<BattlePrize> dropList = new ArrayList<BattlePrize>();
		int mineId = req.getMineId();
		int minePointId = req.getMinePointId();
		MineInfo mineInfo = MineInfoMap.getMineInfo(mineId);
		if (mineInfo != null) {
			int result = getPrize(roleInfo, mineInfo, minePointId, dropList);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
		} else {
			MinePrize minePrize = MineInfoMap.getMinePrize(roleId, minePointId);
			if (minePrize == null) {
				resp.setResult(ErrorCode.GET_PRIZE_MINE_ERROR_2);
				return resp;
			}
			int result = getPrize(roleInfo, minePrize, dropList);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
		}

		resp.setResult(1);
		resp.setMineId(mineId);
		resp.setMinePointId(minePointId);

		resp.setCount(dropList.size());
		resp.setList(dropList);
		return resp;
	}

	/**
	 * 领取矿采集奖励
	 * @param roleInfo
	 * @param mineInfo
	 * @param minePointId
	 * @param dropList
	 * @return
	 */
	public int getPrize(RoleInfo roleInfo, MineInfo mineInfo, int minePointId, List<BattlePrize> dropList) {
		int roleId = roleInfo.getId();
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
		if (xmlInfo == null) {
			return ErrorCode.GET_PRIZE_MINE_ERROR_3;
		}
		List<DropInfo> addList = new ArrayList<DropInfo>();
		synchronized (mineInfo) {
			MineRole mineRole = mineInfo.getMineRole(minePointId);
			if (mineRole == null) {
				return ErrorCode.GET_PRIZE_MINE_ERROR_5;
			}
			if (!mineRole.isEnd()) {
				return ErrorCode.GET_PRIZE_MINE_ERROR_4;
			}
			int out = 0;
			if (mineRole.getRoleId() == roleId) {
				if (mineRole.getStatus() != MineRole.NOT_GET_PRIZE) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_10;
				}
				int dropGold = xmlInfo.getDropGold();
				if (dropGold > 0) {
					addList.add(new DropInfo(ConditionType.TYPE_COIN.getName(), dropGold));
				}
				// 计算占领人产出
				out = MineService.getOut(mineInfo, xmlInfo, mineRole, mineRole.getCreateTime());
			} else {
				MineHelpRole helpRole = mineRole.getHelpRoles(roleId);
				if (helpRole == null) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_6;
				}
				if (helpRole.getStatus() != MineRole.NOT_GET_PRIZE) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_10;
				}
				// 协助人奖励
				out = MineService.getOut(mineInfo, xmlInfo, mineRole, helpRole.getCreateTime());
			}
			if (mineRole.getEndStatus() == MineRole.END_BY_QD) {
				out = out - (int) ((out * GameValue.MINE_RATE_BY_QD) / 100.0);
			} else if (mineRole.getEndStatus() == MineRole.END_BY_FQ) {
				out = out - (int) ((out * GameValue.MINE_RATE_BY_FQ) / 100.0);
			}
			if (out > 0) {
				addList.add(new DropInfo(xmlInfo.getOutType(), out));
			}
			if (checkMineRoleCanDel(mineInfo, xmlInfo, mineRole, roleId)) {
				if (MineDAO.getInstance().delMineRole(mineRole.getId())) {
					mineInfo.removeMineRole(mineRole.getId());
				} else {
					return ErrorCode.GET_PRIZE_MINE_ERROR_7;
				}

			} else {
				if (mineRole.getRoleId() == roleId) {
					if (MineDAO.getInstance().updateMineRoleStatus(mineRole.getId(), MineRole.GET_PRIZE)) {
						mineRole.setStatus(MineRole.GET_PRIZE);
					} else {
						return ErrorCode.GET_PRIZE_MINE_ERROR_8;
					}
				} else {
					MineHelpRole helpRole = mineRole.getHelpRoles(roleId);
					if (helpRole != null) {
						helpRole.setStatus(MineRole.GET_PRIZE);
						if (!MineDAO.getInstance().updateMineRoleHelp(mineRole.getId(), mineRole.getHelpRoles())) {
							return ErrorCode.GET_PRIZE_MINE_ERROR_9;
						}
					} else {
						return ErrorCode.GET_PRIZE_MINE_ERROR_6;
					}
				}
			}
		}

		synchronized (roleInfo) {
			if (addList.size() > 0) {
				int check = ItemService.addPrize(ActionType.action455.getType(), roleInfo, addList, null, null, null,
						null, null, null, dropList, null, null, true);
				if (check != 1) {
					return check;
				}
				// 日志
				GameLogService.insertMineGetLog(roleInfo, ActionType.action455.getType(), dropList);
			}

			// 红点检测
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_MINE_TYPE);
			return 1;
		}
	}

	/**
	 * 领取矿采集奖励
	 * @param roleInfo
	 * @param minePrize
	 * @param dropList
	 * @return
	 */
	public int getPrize(RoleInfo roleInfo, MinePrize minePrize, List<BattlePrize> dropList) {
		int roleId = roleInfo.getId();
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(minePrize.getMineNo());
		if (xmlInfo == null) {
			return ErrorCode.GET_PRIZE_MINE_ERROR_3;
		}
		List<DropInfo> addList = new ArrayList<DropInfo>();
		synchronized (minePrize) {
			int out = 0;
			if (minePrize.getRoleId() == roleId) {
				if (minePrize.getStatus() != MineRole.NOT_GET_PRIZE) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_10;
				}
				int dropGold = xmlInfo.getDropGold();
				if (dropGold > 0) {
					addList.add(new DropInfo(ConditionType.TYPE_COIN.getName(), dropGold));
				}
				// 计算占领人产出
				long startTime = minePrize.getCreateTime().getTime();
				long endTime = minePrize.getCurrEndTime();
				out = MineService.getOut(xmlInfo, startTime, endTime);
			} else {
				MineHelpRole helpRole = minePrize.getHelpRoles(roleId);
				if (helpRole == null) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_6;
				}
				if (helpRole.getStatus() != MineRole.NOT_GET_PRIZE) {
					return ErrorCode.GET_PRIZE_MINE_ERROR_10;
				}
				// 协助人奖励
				long startTime = helpRole.getCreateTime().getTime();
				long endTime = minePrize.getCurrEndTime();
				out = MineService.getOut(xmlInfo, startTime, endTime);
			}
			if (minePrize.getEndStatus() == MineRole.END_BY_QD) {
				out = out - (int) ((out * GameValue.MINE_RATE_BY_QD) / 100.0);
			} else if (minePrize.getEndStatus() == MineRole.END_BY_FQ) {
				out = out - (int) ((out * GameValue.MINE_RATE_BY_FQ) / 100.0);
			}
			if (out > 0) {
				addList.add(new DropInfo(xmlInfo.getOutType(), out));
			}
			if (checkMinePrizeCanDel(minePrize, xmlInfo, roleId)) {
				if (MineDAO.getInstance().deleteMinePrize(minePrize.getId())) {
					MineInfoMap.removeMinePrize(roleId, minePrize.getMinePointId());
				} else {
					return ErrorCode.GET_PRIZE_MINE_ERROR_7;
				}

			} else {
				if (minePrize.getRoleId() == roleId) {
					if (MineDAO.getInstance().updateMinePrizeStatus(minePrize.getId(), MineRole.GET_PRIZE)) {
						minePrize.setStatus(MineRole.GET_PRIZE);
						MineInfoMap.removeMinePrize(roleId, minePrize.getMinePointId());
					} else {
						return ErrorCode.GET_PRIZE_MINE_ERROR_8;
					}
				} else {
					MineHelpRole helpRole = minePrize.getHelpRoles(roleId);
					if (helpRole != null) {
						helpRole.setStatus(MineRole.GET_PRIZE);
						if (MineDAO.getInstance().updateMinePrizeHelp(minePrize.getId(), minePrize.getHelpRoles())) {
							MineInfoMap.removeMinePrize(roleId, minePrize.getMinePointId());
						} else {
							return ErrorCode.GET_PRIZE_MINE_ERROR_9;
						}
					} else {
						return ErrorCode.GET_PRIZE_MINE_ERROR_6;
					}
				}
			}
		}
		synchronized (roleInfo) {
			if (addList.size() > 0) {
				int check = ItemService.addPrize(ActionType.action455.getType(), roleInfo, addList, null, null, null,
						null, null, null, dropList, null, null, true);
				if (check != 1) {
					return check;
				}
				// 日志
				GameLogService.insertMineGetLog(roleInfo, ActionType.action455.getType(), dropList);
			}

			// 红点检测
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_MINE_TYPE);
			return 1;
		}
	}

	/**
	 * 检测矿点其他人是否已经领取奖励
	 * @param mineInfo
	 * @param xmlInfo
	 * @param mineRole
	 * @param exRoleId
	 * @param exRoleId
	 * @return
	 */
	private boolean checkMineRoleCanDel(MineInfo mineInfo, MineXMLInfo xmlInfo, MineRole mineRole, int exRoleId) {
		if (exRoleId != mineRole.getRoleId()) {
			if (mineRole.getStatus() == MineRole.NOT_GET_PRIZE
					&& MineService.checkHavingOut(mineInfo, xmlInfo, mineRole, mineRole.getCreateTime(), null)) {
				return false;
			}
		}
		for (MineHelpRole help : mineRole.getHelpRoles().values()) {
			if (exRoleId == help.getRoleId()) {
				continue;
			}
			if (help.getStatus() == MineRole.NOT_GET_PRIZE
					&& MineService.checkHavingOut(mineInfo, xmlInfo, mineRole, help.getCreateTime(), null)) {
				return false;
			}
		}

		return true;
	}

	private boolean checkMinePrizeCanDel(MinePrize minePrize, MineXMLInfo xmlInfo, int exRoleId) {
		if (exRoleId != minePrize.getRoleId()) {
			if (minePrize.getStatus() == MineRole.NOT_GET_PRIZE
					&& MineService.checkHavingOut(minePrize, xmlInfo, minePrize.getCreateTime(), null)) {
				return false;
			}
		}
		for (MineHelpRole help : minePrize.getHelpRoles().values()) {
			if (exRoleId == help.getRoleId()) {
				continue;
			}
			if (help.getStatus() == MineRole.NOT_GET_PRIZE
					&& MineService.checkHavingOut(minePrize, xmlInfo, help.getCreateTime(), null)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 查询防守阵形
	 * @param req
	 * @return
	 */
	public QueryOtherAIResp queryMineHero(QueryMineHeroReq req) {
		QueryOtherAIResp resp = new QueryOtherAIResp();
		int mineId = req.getMineId();
		int roleId = req.getRoleId();
		int helpRoleId = req.getHelpRoleId();

		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_1);
			return resp;
		}
		RoleInfo roleInfo = null;
		Map<Integer, Integer> heroMap = null;
		synchronized (info) {
			MineRole mineRole = info.getMineRoleZLbyRoleId(roleId);
			if (mineRole == null) {
				resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_2);
				return resp;
			}
			if (helpRoleId != 0) {
				MineHelpRole helpRole = mineRole.getHelpRoles(helpRoleId);
				if (helpRole == null) {
					resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_3);
					return resp;
				}
				roleInfo = RoleInfoMap.getRoleInfo(helpRoleId);
				if (roleInfo == null) {
					resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_4);
					return resp;
				}
				heroMap = helpRole.getHeroMap();
			} else {
				roleInfo = RoleInfoMap.getRoleInfo(roleId);
				if (roleInfo == null) {
					resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_5);
					return resp;
				}
				heroMap = mineRole.getHeroMap();
			}
		}

		synchronized (roleInfo) {
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
			if (mainHero == null) {
				resp.setResult(ErrorCode.QUERY_MINE_HERO_ERROR_6);
				return resp;
			}

			resp.setResult(1);
			resp.setOtherRoleId(roleInfo.getId());
			resp.setOtherRolaName(roleInfo.getRoleName());
			resp.setHeroInfo(HeroService.getHeroDetailRe(mainHero));
			resp.setFightNum(roleInfo.getFightValue());

			List<OtherHeroInfo> heroList = new ArrayList<OtherHeroInfo>();
			if (heroMap != null) {
				HeroInfo heroInfo = null;
				OtherHeroInfo otherHero = null;
				for (int pos : heroMap.keySet()) {
					if (pos != HeroInfo.DEPLOY_TYPE_MAIN) {
						int heroId = heroMap.get(pos);
						heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
						if (heroInfo != null) {
							otherHero = new OtherHeroInfo();
							otherHero.setHeroNo(heroInfo.getHeroNo());
							otherHero.setStar((byte) heroInfo.getStar());
							otherHero.setQuality((byte) heroInfo.getQuality());
							otherHero.setPosition((byte) pos);
							heroList.add(otherHero);
						}
					}
				}
			}

			resp.setHeroCount(heroList.size());
			resp.setHeroList(heroList);
			if (roleInfo.getClubId() > 0) {
				RoleClubInfo roleClubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
				if (roleClubInfo != null) {
					resp.setClubName(roleClubInfo.getClubName());
				}
			}
			return resp;
		}
	}

	/**
	 * 放弃矿占领（协助）
	 * @param roleId
	 * @param req
	 * @return
	 */
	public DropMineResp dropMine(int roleId, DropMineReq req) {
		DropMineResp resp = new DropMineResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.DROP_MINE_ERROR_1);
			return resp;
		}
		int mineId = req.getMineId();
		int zlRoleId = req.getRoleId();
		MineInfo mineInfo = MineInfoMap.getMineInfo(mineId);
		if (mineInfo == null) {
			resp.setResult(ErrorCode.DROP_MINE_ERROR_2);
			return resp;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
		if (xmlInfo == null) {
			resp.setResult(ErrorCode.DROP_MINE_ERROR_3);
			return resp;
		}
		Set<Integer> roles = new HashSet<Integer>();
		roles.add(roleInfo.getId());
		synchronized (mineInfo) {
			if (mineInfo.isClosed()) {
				resp.setResult(ErrorCode.DROP_MINE_ERROR_4);
				return resp;
			}
			MineRole mineRole = mineInfo.getMineRoleZLbyRoleId(zlRoleId);
			if (mineRole == null) {
				resp.setResult(ErrorCode.DROP_MINE_ERROR_5);
				return resp;
			}
			roles.add(zlRoleId);
			roles.addAll(mineRole.getHelpRoles().keySet());
			if (zlRoleId == roleId) {
				Timestamp endTime = new Timestamp(System.currentTimeMillis());
				if (MineService.checkHavingOut(mineInfo, xmlInfo, mineRole, mineRole.getCreateTime(), endTime)) {
					if (MineDAO.getInstance().updateMineRoleEndTime(mineRole.getId(), endTime, MineRole.END_BY_FQ)) {
						mineRole.setEndTime(endTime);
						mineRole.setEndStatus(MineRole.END_BY_FQ);
					} else {
						resp.setResult(ErrorCode.DROP_MINE_ERROR_7);
						return resp;
					}
					// 协助人变化
					refreshDropMine(mineInfo, mineRole, roleInfo);
				} else {
					if (MineDAO.getInstance().delMineRole(mineRole.getId())) {
						mineInfo.removeMineRole(mineRole.getId());
					} else {
						resp.setResult(ErrorCode.DROP_MINE_ERROR_8);
						return resp;
					}
					// 协助人变化
					refreshDropMine(mineInfo, mineRole.getHelpRoles(), roleInfo);
				}

				// 推送大地图矿占领数量改变，和协助人变化
				Set<Integer> changeIds = new HashSet<Integer>();
				changeIds.add(mineId);
				MineService.refreshSceneMine(changeIds, null);

			} else {
				// 不能放弃协助
				resp.setResult(ErrorCode.DROP_MINE_ERROR_6);
				return resp;
			}
		}
		for (int id : roles) {
			// 红点检测
			RedPointMgtService.check2PopRedPoint(id, null, true, RedPointMgtService.LISTENING_MINE_TYPE);
		}
		resp.setResult(1);
		return resp;
	}

	/**
	 * 占有人放弃矿后 协助人变化推送
	 * @param roleInfo
	 * @param info
	 */
	private static void refreshDropMine(MineInfo info, MineRole mineRole, RoleInfo zlRole) {
		QueryMineResp resp = null;
		MineFightLog log = null;
		RoleInfo helpRole = null;
		if (zlRole != null) {
			// 推送占领变化
			resp = new QueryMineResp();
			resp.setResult(1);
			if (zlRole.getRoleLoadInfo() != null) {
				resp.setMineNum(zlRole.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(zlRole.getRoleLoadInfo()));
				resp.setBuyMine(zlRole.getRoleLoadInfo().getTodayBuyMine());
			}
			resp.setIdStr(info.getId() + "");
			List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();
			MineInfoRe re = MineService.getMineInfoRe(zlRole, info, mineRole);
			if (re != null) {
				endlist.add(re);
			}
			resp.setSize(endlist.size());
			resp.setEndlist(endlist);
			SceneRefreService.sendRoleRefreshMsg(zlRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		for (MineHelpRole mineHelpRole : mineRole.getHelpRoles().values()) {
			helpRole = RoleInfoMap.getRoleInfo(mineHelpRole.getRoleId());
			if (helpRole != null) {
				log = getMineFightLog(info, zlRole, helpRole.getId());
				helpRole.addMineFightLog(log);

				resp = new QueryMineResp();
				resp.setResult(1);
				if (helpRole.getRoleLoadInfo() != null) {
					resp.setMineNum(helpRole.getRoleLoadInfo().getTodayMineNum());
					resp.setMineLimit(MineService.getMineLimit(helpRole.getRoleLoadInfo()));
					resp.setBuyMine(helpRole.getRoleLoadInfo().getTodayBuyMine());
				}
				resp.setIdStr(info.getId() + "");
				List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();
				MineInfoRe re = MineService.getMineInfoRe(helpRole, info, mineRole);
				if (re != null) {
					endlist.add(re);
				}
				resp.setSize(endlist.size());
				resp.setEndlist(endlist);

				List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();
				defendList.add(MineService.getMineDefendLogRe(helpRole, log));
				resp.setDefendCount(defendList.size());
				resp.setDefendList(defendList);
				SceneRefreService.sendRoleRefreshMsg(helpRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
			}
		}
	}

	/**
	 * 占有人放弃矿后 协助人变化推送
	 * @param info
	 * @param helpRoles
	 * @param zlRole
	 */
	private static void refreshDropMine(MineInfo info, Map<Integer, MineHelpRole> helpRoles, RoleInfo zlRole) {
		QueryMineResp resp = null;
		MineFightLog log = null;
		RoleInfo helpRole = null;
		if (zlRole != null) {
			// 推送占领变化
			resp = new QueryMineResp();
			resp.setResult(1);
			if (zlRole.getRoleLoadInfo() != null) {
				resp.setMineNum(zlRole.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(zlRole.getRoleLoadInfo()));
				resp.setBuyMine(zlRole.getRoleLoadInfo().getTodayBuyMine());
			}
			resp.setIdStr(info.getId() + "");
			SceneRefreService.sendRoleRefreshMsg(zlRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		for (MineHelpRole mineHelpRole : helpRoles.values()) {
			helpRole = RoleInfoMap.getRoleInfo(mineHelpRole.getRoleId());
			if (helpRole != null) {
				log = getMineFightLog(info, zlRole, helpRole.getId());
				helpRole.addMineFightLog(log);
				resp = new QueryMineResp();
				resp.setResult(1);
				if (helpRole.getRoleLoadInfo() != null) {
					resp.setMineNum(helpRole.getRoleLoadInfo().getTodayMineNum());
					resp.setMineLimit(MineService.getMineLimit(helpRole.getRoleLoadInfo()));
					resp.setBuyMine(helpRole.getRoleLoadInfo().getTodayBuyMine());
				}
				resp.setIdStr(info.getId() + "");
				List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();
				defendList.add(MineService.getMineDefendLogRe(helpRole, log));
				resp.setDefendCount(defendList.size());
				resp.setDefendList(defendList);
				SceneRefreService.sendRoleRefreshMsg(helpRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
			}
		}
	}

	/**
	 * 邀请协助防守
	 * @param roleId
	 * @param req
	 * @return
	 */
	public InviteMineHelpResp inviteMineHelp(int roleId, InviteMineHelpReq req) {
		InviteMineHelpResp resp = new InviteMineHelpResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_1);
			return resp;
		}
		int mineId = req.getMineId();
		int helpId = req.getHelpId();
		MineInfo mineInfo = MineInfoMap.getMineInfo(mineId);
		if (mineInfo == null) {
			resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_2);
			return resp;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(mineInfo.getMineNo());
		if (xmlInfo == null) {
			resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_3);
			return resp;
		}
		synchronized (mineInfo) {
			if (mineInfo.isClosed()) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_4);
				return resp;
			}
			MineRole mineRole = mineInfo.getMineRoleZLbyRoleId(roleId);
			if (mineRole == null) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_5);
				return resp;
			}
			if (mineRole.getHelpRoles().size() >= xmlInfo.getGuardNum()) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_6);
				return resp;
			}
			if (helpId != 0 && mineInfo.getMineRoleZLAndHelpbyRoleId(helpId) != null) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_7);
				return resp;
			}
		}
		if (helpId == 0) {
			// 所在工会
			RoleClubInfo clubInfo = RoleClubInfoMap.getRoleClubInfoByClubId(roleInfo.getClubId());
			if (clubInfo == null) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_8);
				return resp;
			}
			// 发送邀请
			Map<Integer, RoleClubMemberInfo> members = RoleClubMemberInfoMap.getRoleClubMemberMap(clubInfo.getId());
			RoleInfo memberRole = null;
			for (RoleClubMemberInfo member : members.values()) {
				if (member.getRoleId() != roleId) {
					memberRole = RoleInfoMap.getRoleInfo(member.getRoleId());
					if (memberRole != null
							&& FuncOpenXMLInfoMap.getFuncOpenHeroLevel(FuncOpenXMLInfo.MINE_NO) <= HeroInfoMap
									.getMainHeroLv(member.getRoleId())) {
						MineService.sendClubHelpInvite(roleInfo, memberRole, mineId, xmlInfo);
					}
				}
			}
		} else {
			// 好友
			if (!RoleFriendMap.getRoleFriendIdSet(roleId).contains(helpId)) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_9);
				return resp;
			}
			RoleInfo friendRole = RoleInfoMap.getRoleInfo(helpId);
			if (friendRole == null) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_10);
				return resp;
			}
			if (FuncOpenXMLInfoMap.getFuncOpenHeroLevel(FuncOpenXMLInfo.MINE_NO) > HeroInfoMap.getMainHeroLv(friendRole
					.getId())) {
				resp.setResult(ErrorCode.INVITE_MINE_HELP_ERROR_11);
				return resp;
			}
			// 发送邀请
			MineService.sendFriendHelpInvite(roleInfo, friendRole, mineId, xmlInfo);
		}
		resp.setResult(1);
		resp.setMineId(mineId);
		resp.setHelpId(helpId);
		return resp;
	}

	/**
	 * 检测同意协助防守
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MineHelpResp mineHelp(int roleId, MineHelpReq req) {
		MineHelpResp resp = new MineHelpResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_1);
			return resp;
		}
		if (FuncOpenXMLInfoMap.getFuncOpenHeroLevel(FuncOpenXMLInfo.MINE_NO) > HeroInfoMap.getMainHeroLv(roleInfo
				.getId())) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_10);
			return resp;
		}
		int zlNum = MineInfoMap.getZLAndHelpNum(roleInfo.getId());
		if (zlNum >= roleInfo.getMineLimit()) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_2);
			return resp;
		}

		int mineId = req.getMineId();
		int zlRoleId = req.getRoleId();
		RoleInfo zlRole = RoleInfoMap.getRoleInfo(zlRoleId);
		if (zlRole == null) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_1);
			return resp;
		}
		boolean isfriendOrClubMem = false;
		if(!isfriendOrClubMem && zlRole.getClubId() != 0 && zlRole.getClubId() == roleInfo.getClubId()){
			isfriendOrClubMem = true;
		}
		if (!isfriendOrClubMem && RoleFriendMap.getRoleFriendIdSet(zlRoleId).contains(roleId)) {
			isfriendOrClubMem = true;
		}		
		if(!isfriendOrClubMem){
			resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_10);
			return resp;
		}
		
		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_3);
			return resp;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(info.getMineNo());
		if (xmlInfo == null) {
			resp.setResult(ErrorCode.MINE_HELP_ERROR_4);
			return resp;
		}
		if (req.getAction() == 1) {
			// 检测上阵阵形
			int checkPos = checkChgPosInfo(roleInfo, req.getChgPosInfos());
			if (checkPos != 1) {
				resp.setResult(checkPos);
				return resp;
			}
		}

		MineRole mineRole = null;
		synchronized (info) {
			if (info.isClosed()) {
				resp.setResult(ErrorCode.MINE_HELP_ERROR_5);
				return resp;
			}
			mineRole = info.getMineRoleZLAndHelpbyRoleId(roleId);
			if (mineRole != null) {
				resp.setResult(ErrorCode.MINE_HELP_ERROR_6);
				return resp;
			}
			mineRole = info.getMineRoleZLbyRoleId(zlRoleId);
			if (mineRole == null) {
				resp.setResult(ErrorCode.MINE_HELP_ERROR_7);
				return resp;
			}
			if (mineRole.getHelpRoles().size() >= xmlInfo.getGuardNum()) {
				resp.setResult(ErrorCode.MINE_HELP_ERROR_8);
				return resp;
			}
			// 同意协助防守
			if (req.getAction() == 1) {
				Map<Integer, MineHelpRole> helpRoles = new HashMap<Integer, MineHelpRole>(mineRole.getHelpRoles());
				MineHelpRole help = new MineHelpRole();
				help.setRoleId(roleId);
				help.setHelpPos(mineRole.getNewHelpPos());
				help.setCreateTime(new Timestamp(System.currentTimeMillis()));
				help.setStatus(0);
				help.setHeroMap(getHeroMap(req.getChgPosInfos()));
				helpRoles.put(roleId, help);

				if (MineDAO.getInstance().updateMineRoleHelp(mineRole.getId(), helpRoles)) {
					mineRole.setHelpRoles(helpRoles);
				} else {
					resp.setResult(ErrorCode.MINE_HELP_ERROR_9);
					return resp;
				}
				// 推送占领人其他协防人变化
				refreshHelpMine(roleInfo, info, mineRole);
			}
		}

		if (req.getAction() == 0) {
			long now = System.currentTimeMillis();
			List<MineInfoRe> list = new ArrayList<MineInfoRe>();
			Map<Integer, MineInfo> map = MineInfoMap.getMineMap();
			for (MineInfo mineInfo : map.values()) {
				synchronized (mineInfo) {
					MineInfoRe re = null;
					for (MineRole roleMine : mineInfo.getRoles().values()) {
						re = MineService.getMineInfoRe(roleInfo, mineInfo, roleMine);
						if (re != null) {
							long endTime = re.getZlDetail().getEndTime();
							if (endTime > now) {
								list.add(re);
							}
						}
					}
				}

			}
			resp.setCount(list.size());
			resp.setList(list);
			resp.setMineInfo(MineService.getMineInfoRe(zlRole, info, mineRole));
		} else {
			resp.setMineInfo(MineService.getMineInfoRe(roleInfo, info, mineRole));
		}

		resp.setResult(1);
		resp.setAction(req.getAction());
		return resp;
	}

	/**
	 * 改动防守阵形
	 * @param roleId
	 * @param req
	 * @return
	 */
	public MineDeployResp mineDeploy(int roleId, MineDeployReq req) {
		MineDeployResp resp = new MineDeployResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_1);
			return resp;
		}
		int mineId = req.getMineId();
		int zlRoleId = req.getRoleId();
		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_2);
			return resp;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(info.getMineNo());
		if (xmlInfo == null) {
			resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_3);
			return resp;
		}
		// 获取角色矿上阵武将
		Set<Integer> heroIdList = MineInfoMap.getMineHerosbyRoleId(roleInfo.getId());
		synchronized (info) {
			if (info.isClosed()) {
				resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_5);
				return resp;
			}
			MineRole mineRole = info.getMineRoleZLbyRoleId(zlRoleId);
			if (mineRole == null) {
				resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_6);
				return resp;
			}
			if (mineRole.isEnd()) {
				resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_6);
				return resp;
			}
			if (zlRoleId == roleId) {
				if (mineRole.getHeroMap() != null) {
					heroIdList.removeAll(mineRole.getHeroMap().values());
				}
				int checkPos = checkChgPosInfo(roleInfo, heroIdList, req.getChgPosInfos());
				if (checkPos != 1) {
					resp.setResult(checkPos);
					return resp;
				}
				Map<Integer, Integer> heroMap = getHeroMap(req.getChgPosInfos());
				if (MineDAO.getInstance().updateMineRoleHero(mineRole.getId(), heroMap)) {
					mineRole.setHeroMap(heroMap);
				} else {
					resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_7);
					return resp;
				}
			} else {
				MineHelpRole helpRole = mineRole.getHelpRoles(roleId);
				if (helpRole == null) {
					resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_8);
					return resp;
				}
				if (helpRole.getHeroMap() != null) {
					heroIdList.removeAll(helpRole.getHeroMap().values());
				}
				int checkPos = checkChgPosInfo(roleInfo, heroIdList, req.getChgPosInfos());
				if (checkPos != 1) {
					resp.setResult(checkPos);
					return resp;
				}
				helpRole.setHeroMap(getHeroMap(req.getChgPosInfos()));
				if (!MineDAO.getInstance().updateMineRoleHelp(mineRole.getId(), mineRole.getHelpRoles())) {
					resp.setResult(ErrorCode.MINE_DEPLOY_ERROR_9);
					return resp;
				}
			}
			// 推送占领人其他协防人变化
			refreshHelpMine(roleInfo, info, mineRole);

			resp.setResult(1);
			resp.setMineInfo(MineService.getMineInfoRe(roleInfo, info, mineRole));
			return resp;
		}
	}

	/**
	 * 协防后推送推送占领人其他协防人变化
	 * @param fightResult
	 * @param roleInfo
	 * @param info
	 * @param zlRoleId
	 * @param helpRoles
	 */
	private static void refreshHelpMine(RoleInfo roleInfo, MineInfo info, MineRole mineRole) {
		QueryMineResp resp = null;
		// 占领人
		RoleInfo zlRole = RoleInfoMap.getRoleInfo(mineRole.getRoleId());
		if (zlRole != null && zlRole.getId() != roleInfo.getId()) {
			resp = new QueryMineResp();
			resp.setResult(1);
			if (zlRole.getRoleLoadInfo() != null) {
				resp.setMineNum(zlRole.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(zlRole.getRoleLoadInfo()));
				resp.setBuyMine(zlRole.getRoleLoadInfo().getTodayBuyMine());
			}
			resp.setIdStr("");
			List<MineInfoRe> list = new ArrayList<MineInfoRe>();
			MineInfoRe re = MineService.getMineInfoRe(zlRole, info, mineRole);
			if (re != null) {
				list.add(re);
			}

			resp.setCount(list.size());
			resp.setList(list);
			SceneRefreService.sendRoleRefreshMsg(zlRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		RoleInfo helpRole = null;
		for (MineHelpRole mineHelpRole : mineRole.getHelpRoles().values()) {
			helpRole = RoleInfoMap.getRoleInfo(mineHelpRole.getRoleId());
			if (helpRole != null && mineHelpRole.getRoleId() != roleInfo.getId()) {
				resp = new QueryMineResp();
				resp.setResult(1);
				if (helpRole.getRoleLoadInfo() != null) {
					resp.setMineNum(helpRole.getRoleLoadInfo().getTodayMineNum());
					resp.setMineLimit(MineService.getMineLimit(helpRole.getRoleLoadInfo()));
					resp.setBuyMine(helpRole.getRoleLoadInfo().getTodayBuyMine());
				}
				resp.setIdStr("");
				List<MineInfoRe> list = new ArrayList<MineInfoRe>();
				MineInfoRe re = MineService.getMineInfoRe(helpRole, info, mineRole);
				if (re != null) {
					list.add(re);
				}
				resp.setCount(list.size());
				resp.setList(list);

				SceneRefreService.sendRoleRefreshMsg(helpRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
			}
		}
	}

	/**
	 * 购买抢夺次数
	 * @param roleId
	 * @return
	 */
	public BuyMineNumResp buyMineNum(int roleId) {
		BuyMineNumResp resp = new BuyMineNumResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.MINE_BUY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo == null) {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_2);
				return resp;
			}
			GoldBuyXMLInfo xmlInfo = GoldBuyXMLInfoMap.getGoldBuyXMLInfo(GoldBuyXMLInfo.TYPE_MINE_BUY);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_3);
				return resp;
			}
			int buyNum = roleLoadInfo.getTodayBuyMine();
			if (xmlInfo.getFixed() == 0 && buyNum >= xmlInfo.getMaxBuyNum()) {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_4);
				return resp;
			}
			GoldBuyXMLPro pro = null;
			if (xmlInfo.getFixed() == 0) {
				// 固定次数
				pro = xmlInfo.getGoldBuyXMLPro(buyNum + 1);
			} else {
				// 无限次数，超过最后一次后价格不递增，按照最后一次的价格来
				if (buyNum >= xmlInfo.getMaxBuyNum()) {
					pro = xmlInfo.getGoldBuyXMLPro(xmlInfo.getMaxBuyNum());
				} else {
					pro = xmlInfo.getGoldBuyXMLPro(buyNum + 1);
				}
			}
			if (pro == null) {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_5);
				return resp;
			}
			List<AbstractConditionCheck> conditions = new ArrayList<AbstractConditionCheck>();
			conditions.addAll(pro.getConditions());
			conditions.add(new CoinCond(pro.getGold()));

			int check = AbstractConditionCheck.checkCondition(roleInfo, conditions);
			if (check != 1) {
				resp.setResult(check);
				return resp;
			}
			conditions.clear();
			conditions.add(new CoinCond(pro.getGold()));
			if (RoleService.subRoleResource(ActionType.action457.getType(), roleInfo, conditions, null)) {
				resp.setSourceType((byte) ConditionType.TYPE_COIN.getType());
				resp.setSourceChange(-pro.getGold());
			} else {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_6);
				return resp;
			}

			Timestamp lastBuyMineTime = new Timestamp(System.currentTimeMillis());
			if (RoleDAO.getInstance().updateRoleMinebuyNum(roleId, buyNum + 1, lastBuyMineTime)) {
				roleLoadInfo.setBuyMine(buyNum + 1);
				roleLoadInfo.setLastBuyMineTime(lastBuyMineTime);
			} else {
				resp.setResult(ErrorCode.MINE_BUY_ERROR_7);
				return resp;
			}

			resp.setResult(1);
			resp.setMineNum(roleLoadInfo.getTodayMineNum());
			resp.setMineLimit(MineService.getMineLimit(roleLoadInfo));
			resp.setBuyMine(roleLoadInfo.getTodayBuyMine());
			return resp;
		}

	}

	/**
	 * 查看防守记录
	 * @param roleId
	 */
	public void lookMineLog(int roleId) {
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			return;
		}
		synchronized (roleInfo) {
			roleInfo.setLastLookLogTime(new Timestamp(System.currentTimeMillis()));
			// 红点检测
			RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, true, RedPointMgtService.LISTENING_MINE_TYPE);
		}
	}

	/**
	 * 战斗开始处理
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new EnergyCond(GameValue.MINE_FIGHT_ENERGY_COST));
		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1) {
			return check;
		}

		int zlNum = MineInfoMap.getZLAndHelpNum(roleInfo.getId());
		if (zlNum >= roleInfo.getMineLimit()) {
			return ErrorCode.MINE_FIGHT_START_ERROR_1;
		}
		int checkPos = checkChgPosInfo(roleInfo, fightInfo.getChgPosInfos());
		if (checkPos != 1) {
			return checkPos;
		}

		FightSideData attackSide = getAttackFightSideData(roleInfo, fightInfo);
		if (attackSide == null) {
			return ErrorCode.MINE_FIGHT_START_ERROR_2;
		}
		fightInfo.getFightDataList().add(attackSide);

		String defendStr = fightInfo.getDefendStr().trim();
		int mineId = NumberUtils.toInt(defendStr.split(",")[0]);
		int zlRoleId = NumberUtils.toInt(defendStr.split(",")[1]);
		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			return ErrorCode.MINE_FIGHT_START_ERROR_3;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(info.getMineNo());
		if (xmlInfo == null) {
			return ErrorCode.MINE_FIGHT_START_ERROR_4;
		}
		int action = 0;
		synchronized (info) {
			if (info.isClosed()) {
				return ErrorCode.MINE_FIGHT_START_ERROR_5;
			}
			if (info.getMineRoleZLAndHelpbyRoleId(roleInfo.getId()) != null) {
				return ErrorCode.MINE_FIGHT_START_ERROR_6;
			}
			if (zlRoleId != 0) {
				MineRole mineRole = info.getMineRoleZLbyRoleId(zlRoleId);
				if (mineRole == null) {
					return ErrorCode.MINE_FIGHT_START_ERROR_7;
				}
				if (!mineRole.isCanLoot()) {
					return ErrorCode.MINE_FIGHT_START_ERROR_8;
				}
				int checkFightNum = checkLootInFight(roleInfo, fightInfo.getFightType(), info, mineRole);
				if (checkFightNum != 1) {
					return checkFightNum;
				}
				if (!MineService.checkMineNum(roleInfo)) {
					return ErrorCode.MINE_FIGHT_START_ERROR_11;
				}
				int mineNum = roleInfo.getRoleLoadInfo().getTodayMineNum();
				Timestamp lastMineTime = new Timestamp(System.currentTimeMillis());
				if (RoleDAO.getInstance().updateRoleMineNum(roleInfo.getId(), mineNum + 1, lastMineTime)) {
					roleInfo.getRoleLoadInfo().setMineNum(mineNum + 1);
					roleInfo.getRoleLoadInfo().setLastMineTime(lastMineTime);
				} else {
					return ErrorCode.MINE_FIGHT_START_ERROR_12;
				}

				addDefendFightSideData(fightInfo, mineRole);
				action = ActionType.action451.getType();
			} else {
				if (!info.isCanCollect()) {
					return ErrorCode.MINE_FIGHT_START_ERROR_9;
				}
				int checkFightNum = checkCollectInFight(roleInfo, fightInfo.getFightType(), info, xmlInfo);
				if (checkFightNum != 1) {
					return checkFightNum;
				}
				fightInfo.setStartRespDefendStr(xmlInfo.getGWNo());
				action = ActionType.action453.getType();
			}
			info.addFightInfo(fightInfo);
		}

		if (!RoleService.subRoleResource(action, roleInfo, conds, null)) {
			return ErrorCode.MINE_FIGHT_START_ERROR_10;
		}

		MapRolePointInfo pointInfo = MapRoleInfoMap.getMapPointInfo((int) roleInfo.getId());
		if (pointInfo != null) {
			pointInfo.setStatus((byte) 1);

			// 大地图上战斗，广播给可见自己的其它人
			SceneService1.brocastRolePointStatus(pointInfo, roleInfo);
		}
		return 1;
	}

	/**
	 * 检测上阵阵形
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	private static int checkChgPosInfo(RoleInfo roleInfo, List<StartFightPosInfo> chgPosInfos) {
		// 获取角色矿上阵武将
		Set<Integer> heroIdList = MineInfoMap.getMineHerosbyRoleId(roleInfo.getId());
		return checkChgPosInfo(roleInfo, heroIdList, chgPosInfos);
	}

	/**
	 * 检测上阵阵形
	 * @param roleInfo
	 * @param heroIdList 已上阵阵形
	 * @param chgPosInfos
	 * @return
	 */
	private static int checkChgPosInfo(RoleInfo roleInfo, Set<Integer> heroIdList, List<StartFightPosInfo> chgPosInfos) {
		if (chgPosInfos != null) {
			HeroInfo heroInfo = null;
			List<Integer> heroIds = new ArrayList<Integer>();// 验证英雄id
			List<Byte> deployPoss = new ArrayList<Byte>();// 验证布阵位置
			int otherHeroNum = 0;// 验证至少上阵一个副将
			for (StartFightPosInfo re : chgPosInfos) {
				byte deployPos = re.getDeployPos();
				int heroId = re.getHeroId();
				if (deployPos <= 0) {
					return ErrorCode.CHECK_MINE_POS_ERROR_1;
				}
				if (deployPoss.contains(deployPos)) {
					// 判断上阵位置是否重复
					return ErrorCode.CHECK_MINE_POS_ERROR_2;
				} else {
					deployPoss.add(deployPos);// 记录heroId
				}
				heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), heroId);
				if (heroInfo == null) {
					return ErrorCode.CHECK_MINE_POS_ERROR_3;
				}
				if (deployPos != HeroInfo.DEPLOY_TYPE_MAIN) {
					if (heroIdList.contains(heroId)) {
						return ErrorCode.CHECK_MINE_POS_ERROR_4;
					}
				}
				if (heroIds.contains(heroId)) {
					// 判断heroId是否重复
					return ErrorCode.CHECK_MINE_POS_ERROR_5;
				} else {
					heroIds.add(heroId);// 记录heroId
				}
				if (!FightDeployService.checkDeployPosOpen(roleInfo, deployPos)) {
					return ErrorCode.CHECK_MINE_POS_ERROR_6;
				}
				if (deployPos > HeroInfo.DEPLOY_TYPE_MAIN && deployPos <= GameValue.FIGHT_ARMY_LIMIT) {
					otherHeroNum++;
				}
			}
			if (otherHeroNum <= 0) {
				// 没有上阵武将
				return ErrorCode.CHECK_MINE_POS_ERROR_7;
			}
		} else {
			return ErrorCode.CHECK_MINE_POS_ERROR_8;
		}

		return 1;
	}

	/**
	 * 检测开采战斗中数量
	 * @param roleInfo
	 * @param fightType
	 * @param info
	 * @param xmlInfo
	 * @return
	 */
	private static int checkCollectInFight(RoleInfo roleInfo, FightType fightType, MineInfo info, MineXMLInfo xmlInfo) {
		Map<Integer, FightInfo> fightMap = info.getFightMap();
		int inFight = fightMap.size();
		if (info.getZlSize() + inFight >= xmlInfo.getMaxMiners()) {
			return ErrorCode.CHECK_MINE_COLLECT_ERROR_1;
		}
		return 1;
	}

	/**
	 * 检测开采战斗中数量
	 * @param roleInfo
	 * @param fightType
	 * @param info
	 * @param xmlInfo
	 * @return
	 */
	private static int checkLootInFight(RoleInfo roleInfo, FightType fightType, MineInfo info, MineRole mineRole) {
		Map<Integer, FightInfo> fightMap = info.getFightMap();
		for (FightInfo fightInfo : fightMap.values()) {
			if (fightInfo.getRoleId() == roleInfo.getId()) {
				continue;
			}
			if (fightInfo.getFightType() != fightType) {
				continue;
			}
			String defendStr = fightInfo.getDefendStr().trim();
			int fmineId = NumberUtils.toInt(defendStr.split(",")[0]);
			int fzlRoleId = NumberUtils.toInt(defendStr.split(",")[1]);
			if (fmineId == mineRole.getMineId() && fzlRoleId == mineRole.getRoleId()) {
				return ErrorCode.CHECK_MINE_LOOT_ERROR_1;
			}
		}
		return 1;
	}

	/**
	 * 获取攻击方战斗数据
	 * @param info
	 * @return
	 */
	private static FightSideData getAttackFightSideData(RoleInfo roleInfo, FightInfo fightInfo) {
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());

		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_0);
		sideDate.setSideRoleId(roleInfo.getId());
		sideDate.setSideName(roleInfo.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());

		Map<Byte, HeroRecord> recordMap = MineService
				.getHeroRecordMap(roleInfo, getHeroMap(fightInfo.getChgPosInfos()));
		if (recordMap == null) {
			return null;
		}
		FightArmyDataInfo armyData = null;
		for (HeroRecord record : recordMap.values()) {
			armyData = FightService.getFightArmyDatabyHeroRecord(recordMap, record, sideDate.getSideId(), mainRate,
					otherRate);
			if (armyData == null) {
				continue;
			}
			sideDate.getArmyInfos().add(armyData);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		return sideDate;
	}

	/**
	 * 添加战斗数据
	 * @param fightInfo
	 * @param mineRole
	 * @return
	 */
	private static int addDefendFightSideData(FightInfo fightInfo, MineRole mineRole) {
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());

		RoleInfo role = RoleInfoMap.getRoleInfo(mineRole.getRoleId());
		if (role == null) {
			return ErrorCode.GET_FIGHT_DATA_ERROR_1;
		}
		FightSideData defendSide = getDefendFightSideData(role, mineRole.getHeroMap(), mainRate, otherRate);
		if (defendSide == null) {
			return ErrorCode.GET_FIGHT_DATA_ERROR_2;
		}
		fightInfo.getFightDataList().add(defendSide);

		List<MineHelpRole> list = mineRole.getHelpRoleListbySort();
		for (MineHelpRole helpRole : list) {
			role = RoleInfoMap.getRoleInfo(helpRole.getRoleId());
			if (role == null) {
				return ErrorCode.GET_FIGHT_DATA_ERROR_3;
			}
			defendSide = getDefendFightSideData(role, helpRole.getHeroMap(), mainRate, otherRate);
			if (defendSide == null) {
				return ErrorCode.GET_FIGHT_DATA_ERROR_4;
			}
			fightInfo.getFightDataList().add(defendSide);
		}
		return 1;
	}

	/**
	 * 获取防守方战斗数据
	 * @param fightArena
	 * @return
	 */
	private static FightSideData getDefendFightSideData(RoleInfo roleInfo, Map<Integer, Integer> heroMap,
			Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate) {
		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_1);
		sideDate.setSideRoleId(roleInfo.getId());
		sideDate.setSideName(roleInfo.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());

		Map<Byte, HeroRecord> recordMap = MineService.getHeroRecordMap(roleInfo, heroMap);
		if (recordMap == null) {
			return null;
		}
		FightArmyDataInfo armyData = null;
		for (HeroRecord record : recordMap.values()) {
			armyData = FightService.getFightArmyDatabyHeroRecord(recordMap, record, sideDate.getSideId(), mainRate,
					otherRate);
			if (armyData == null) {
				continue;
			}
			if (armyData.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
				armyData.setLookRange(GameValue.ARENA_LOOK_RANGE);
			}
			sideDate.getArmyInfos().add(armyData);
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		return sideDate;
	}

	/**
	 * 处理战斗结果
	 * @param action
	 * @param fightResult 1-胜 2-败
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightEnd(int action, int fightResult, RoleInfo roleInfo, FightInfo fightInfo,
			List<BattlePrize> dropList) {
		String defendStr = fightInfo.getDefendStr().trim();
		int mineId = NumberUtils.toInt(defendStr.split(",")[0]);
		int zlRoleId = NumberUtils.toInt(defendStr.split(",")[1]);
		MineInfo info = MineInfoMap.getMineInfo(mineId);
		if (info == null) {
			return ErrorCode.MINE_FIGHT_END_ERROR_1;
		}
		MineXMLInfo xmlInfo = MineXMLInfoMap.getMineXMLInfo(info.getMineNo());
		if (xmlInfo == null) {
			return ErrorCode.MINE_FIGHT_END_ERROR_2;
		}
		MineRole mineRole = null;
		Set<Integer> roles = new HashSet<Integer>();
		roles.add(roleInfo.getId());
		synchronized (info) {
			if (zlRoleId != 0) {
				mineRole = info.getMineRoleZLbyRoleId(zlRoleId);
				if (mineRole == null) {
					return ErrorCode.MINE_FIGHT_END_ERROR_3;
				}
				RoleInfo zlRole = RoleInfoMap.getRoleInfo(zlRoleId);
				if (zlRole == null) {
					return ErrorCode.MINE_FIGHT_END_ERROR_4;
				}
				roles.add(zlRoleId);
				roles.addAll(mineRole.getHelpRoles().keySet());
				MineRole newMineRole = null;
				if (fightResult == 1) {
					newMineRole = new MineRole(mineId, roleInfo.getId(), getHeroMap(fightInfo.getChgPosInfos()));
					Timestamp endTime = new Timestamp(System.currentTimeMillis());
					if (MineDAO.getInstance().zlMineRoles(mineRole.getId(), endTime, MineRole.END_BY_QD, newMineRole)) {
						mineRole.setEndTime(endTime);
						mineRole.setEndStatus(MineRole.END_BY_QD);

						info.addMineRole(newMineRole);
					} else {
						return ErrorCode.MINE_FIGHT_END_ERROR_5;
					}
				}

				// 抢夺后推送攻守双方矿变化
				refreshLootMine(fightResult, roleInfo, info, xmlInfo, mineRole, newMineRole);
				// 获取奖励
				if (fightResult == 1) {
					int getNum = (int) ((MineService.getOut(info, xmlInfo, mineRole, null) * GameValue.MINE_RATE_BY_QD) / 100.0);
					for (MineHelpRole helpRole : mineRole.getHelpRoles().values()) {
						getNum += (int) ((MineService.getOut(info, xmlInfo, mineRole, helpRole.getCreateTime()) * GameValue.MINE_RATE_BY_QD) / 100.0);
					}
					if (getNum > 0) {
						List<DropInfo> addList = new ArrayList<DropInfo>();
						addList.add(new DropInfo(xmlInfo.getOutType(), getNum));
						int result = ItemService.addPrize(action, roleInfo, addList, null, null, null, null, null,
								null, dropList, null, null, true);
						if (result != 1) {
							return result;
						}
					}
					PushMgrService.dealOfflinePush(zlRole, PushXMLInfo.PUSH_NO_MINE);
				}

			} else {
				if (fightResult == 1) {
					// 开采成功
					mineRole = new MineRole(mineId, roleInfo.getId(), getHeroMap(fightInfo.getChgPosInfos()));
					if (MineDAO.getInstance().insertMineRole(mineRole)) {
						info.addMineRole(mineRole);
					} else {
						return ErrorCode.MINE_FIGHT_END_ERROR_6;
					}
					String gwNo = fightInfo.getStartRespDefendStr();
					List<DropInfo> addList = ChallengeService.checkDrop(gwNo, (byte) 0);
					if (addList != null && addList.size() > 0) {
						int result = ItemService.addPrize(action, roleInfo, addList, null, null, null, null, null,
								null, dropList, null, null, true);
						if (result != 1) {
							return result;
						}
					}

					// 推送大地图矿占领数量改变
					Set<Integer> changeIds = new HashSet<Integer>();
					changeIds.add(mineId);
					MineService.refreshSceneMine(changeIds, roleInfo.getId());
				}
				// 推送自己矿变化
				refreshCollectMine(fightResult, roleInfo, info, mineRole);
			}
			// 清除战斗缓存
			info.removeFightInfo(roleInfo.getId());
		}

		// 任务检测
		QuestService.checkQuest(roleInfo, action, null, true, true);

		for (int id : roles) {
			// 红点检测
			RedPointMgtService.check2PopRedPoint(id, null, true, RedPointMgtService.LISTENING_MINE_TYPE);
		}
		return 1;
	}

	/**
	 * 获取防守阵形
	 * @param fightInfo
	 * @return
	 */
	private static Map<Integer, Integer> getHeroMap(List<StartFightPosInfo> chgPosInfos) {
		Map<Integer, Integer> heroMap = new HashMap<Integer, Integer>();
		if (chgPosInfos != null) {
			for (StartFightPosInfo pos : chgPosInfos) {
				heroMap.put((int) pos.getDeployPos(), pos.getHeroId());
			}
		}
		return heroMap;
	}

	/**
	 * 开采成功推送自己矿变化
	 * @param roleInfo
	 * @param info
	 */
	private static void refreshCollectMine(int fightResult, RoleInfo roleInfo, MineInfo info, MineRole mineRole) {
		if (fightResult == 1) {
			QueryMineResp resp = new QueryMineResp();
			resp.setResult(1);
			if (roleInfo.getRoleLoadInfo() != null) {
				resp.setMineNum(roleInfo.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(roleInfo.getRoleLoadInfo()));
				resp.setBuyMine(roleInfo.getRoleLoadInfo().getTodayBuyMine());
			}
			resp.setIdStr("");
			List<MineInfoRe> list = new ArrayList<MineInfoRe>();
			MineInfoRe re = MineService.getMineInfoRe(roleInfo, info, mineRole);
			if (re != null) {
				list.add(re);
			}
			resp.setCount(list.size());
			resp.setList(list);
			// 战斗后切换场景重新请求，不需要推送刷新
			// SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),
			// SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		MineFightLog log = getMineFightLog(fightResult, null, info, roleInfo, 0, 0);
		GameLogService.insertMineFightLog(log);
	}

	/**
	 * 抢夺后推送攻守双方矿变化
	 * @param fightResult
	 * @param roleInfo
	 * @param info
	 * @param zlRoleId
	 * @param helpRoles
	 */
	private static void refreshLootMine(int fightResult, RoleInfo roleInfo, MineInfo info, MineXMLInfo xmlInfo,
			MineRole oldMineRole, MineRole newMineRole) {
		QueryMineResp resp = null;
		MineFightLog log = null;
		if (fightResult == 1 && newMineRole != null) {
			// 抢夺成功 推送自己矿
			resp = new QueryMineResp();
			resp.setResult(1);
			if (roleInfo.getRoleLoadInfo() != null) {
				resp.setMineNum(roleInfo.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(roleInfo.getRoleLoadInfo()));
				resp.setBuyMine(roleInfo.getRoleLoadInfo().getTodayBuyMine());
			}
			resp.setIdStr("");
			List<MineInfoRe> list = new ArrayList<MineInfoRe>();
			MineInfoRe re = MineService.getMineInfoRe(roleInfo, info, newMineRole);
			if (re != null) {
				list.add(re);
			}
			resp.setCount(list.size());
			resp.setList(list);
			// 战斗后切换场景重新请求，不需要推送刷新
			// SceneRefreService.sendRoleRefreshMsg(roleInfo.getId(),
			// SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		// 原占领人
		RoleInfo zlRole = RoleInfoMap.getRoleInfo(oldMineRole.getRoleId());
		if (zlRole != null) {
			int getNum = (int) ((MineService.getOut(info, xmlInfo, oldMineRole, null) * GameValue.MINE_RATE_BY_QD) / 100.0);
			log = getMineFightLog(fightResult, roleInfo, info, zlRole, 0, getNum);
			zlRole.addMineFightLog(log);
			resp = new QueryMineResp();
			resp.setResult(1);
			if (zlRole.getRoleLoadInfo() != null) {
				resp.setMineNum(zlRole.getRoleLoadInfo().getTodayMineNum());
				resp.setMineLimit(MineService.getMineLimit(zlRole.getRoleLoadInfo()));
				resp.setBuyMine(zlRole.getRoleLoadInfo().getTodayBuyMine());
			}
			if (fightResult == 1) {
				resp.setIdStr(info.getId() + "");
				List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();
				MineInfoRe re = MineService.getMineInfoRe(zlRole, info, oldMineRole);
				if (re != null) {
					endlist.add(re);
				}
				resp.setSize(endlist.size());
				resp.setEndlist(endlist);
			}
			List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();
			defendList.add(MineService.getMineDefendLogRe(zlRole, log));
			resp.setDefendCount(defendList.size());
			resp.setDefendList(defendList);
			SceneRefreService.sendRoleRefreshMsg(zlRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
		}
		RoleInfo helpRole = null;
		for (MineHelpRole mineHelpRole : oldMineRole.getHelpRoles().values()) {
			helpRole = RoleInfoMap.getRoleInfo(mineHelpRole.getRoleId());
			if (helpRole != null) {
				int getNum = (int) ((MineService.getOut(info, xmlInfo, oldMineRole, mineHelpRole.getCreateTime()) * GameValue.MINE_RATE_BY_QD) / 100.0);
				log = getMineFightLog(fightResult, roleInfo, info, zlRole, helpRole.getId(), getNum);
				helpRole.addMineFightLog(log);
				resp = new QueryMineResp();
				resp.setResult(1);
				if (helpRole.getRoleLoadInfo() != null) {
					resp.setMineNum(helpRole.getRoleLoadInfo().getTodayMineNum());
					resp.setMineLimit(MineService.getMineLimit(helpRole.getRoleLoadInfo()));
					resp.setBuyMine(helpRole.getRoleLoadInfo().getTodayBuyMine());
				}
				if (fightResult == 1) {
					resp.setIdStr(info.getId() + "");
					List<MineInfoRe> endlist = new ArrayList<MineInfoRe>();
					MineInfoRe re = MineService.getMineInfoRe(helpRole, info, oldMineRole);
					if (re != null) {
						endlist.add(re);
					}
					resp.setSize(endlist.size());
					resp.setEndlist(endlist);
				}
				List<MineDefendLogRe> defendList = new ArrayList<MineDefendLogRe>();
				defendList.add(MineService.getMineDefendLogRe(helpRole, log));
				resp.setDefendCount(defendList.size());
				resp.setDefendList(defendList);
				SceneRefreService.sendRoleRefreshMsg(helpRole.getId(), SceneRefreService.REFRESH_TYPE_ROLE_MINE, resp);
			}
		}
		// 保存日志
		GameLogService.insertMineFightLog(log);
	}

	/**
	 * 获取开采，抢夺日志
	 * @param fightResult
	 * @param attackRole
	 * @param info
	 * @param zlRoleId
	 * @param helpRoleId
	 * @return
	 */
	private static MineFightLog getMineFightLog(int fightResult, RoleInfo attackRole, MineInfo info, RoleInfo zlRole,
			int helpRoleId, int getNum) {
		MineFightLog log = new MineFightLog();
		log.setPosition(info.getPosition());
		log.setMineNo(info.getMineNo());
		if (zlRole != null) {
			log.setRoleId(zlRole.getId());
			log.setRoleName(zlRole.getRoleName());
			log.setAccount(zlRole.getAccount());
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(zlRole);
			if (mainHero != null) {
				log.setRoleHeroNo(mainHero.getHeroNo());
				log.setRoleLevel(mainHero.getHeroLevel());
			}
		}
		log.setHelpRoleId(helpRoleId);

		if (attackRole != null) {
			log.setAttackRoleId(attackRole.getId());
			log.setAttackRoleName(attackRole.getRoleName());
			log.setAttackAccount(attackRole.getAccount());
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(attackRole);
			if (mainHero != null) {
				log.setAttackRoleHeroNo(mainHero.getHeroNo());
				log.setAttackRoleLevel(mainHero.getHeroLevel());
			}
		}

		log.setGetNum(getNum);
		log.setFightResult((byte) fightResult);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		return log;
	}

	/**
	 * 获取放弃日志
	 * @param info
	 * @param zlRole
	 * @param helpRoleId
	 * @return
	 */
	private static MineFightLog getMineFightLog(MineInfo info, RoleInfo zlRole, int helpRoleId) {
		MineFightLog log = new MineFightLog();
		log.setPosition(info.getPosition());
		log.setMineNo(info.getMineNo());
		if (zlRole != null) {
			log.setRoleId(zlRole.getId());
			log.setRoleName(zlRole.getRoleName());
			log.setAccount(zlRole.getAccount());
			HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(zlRole);
			if (mainHero != null) {
				log.setRoleHeroNo(mainHero.getHeroNo());
				log.setRoleLevel(mainHero.getHeroLevel());
			}
		}
		log.setHelpRoleId(helpRoleId);

		log.setFightResult((byte) 3);
		log.setTime(new Timestamp(System.currentTimeMillis()));
		return log;
	}
}
