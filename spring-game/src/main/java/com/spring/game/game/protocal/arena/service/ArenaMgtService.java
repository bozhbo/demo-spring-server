package com.snail.webgame.game.protocal.arena.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.snail.webgame.game.cache.FightArenaInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameFlag;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.fightdata.FightArmyDataInfo;
import com.snail.webgame.game.common.fightdata.FightSideData;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.ConditionType;
import com.snail.webgame.game.condtion.conds.EnergyCond;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.FightArenaDAO;
import com.snail.webgame.game.info.FightArenaInfo;
import com.snail.webgame.game.info.FightDeployInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.arena.match.MatchArenaResp;
import com.snail.webgame.game.protocal.arena.query.ArenaRe;
import com.snail.webgame.game.protocal.arena.query.QueryArenaResp;
import com.snail.webgame.game.protocal.arena.rank.RankArenaReq;
import com.snail.webgame.game.protocal.arena.rank.RankArenaResp;
import com.snail.webgame.game.protocal.arena.reset.ResetArenaResp;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.fightdeploy.service.FightDeployService;
import com.snail.webgame.game.protocal.hero.service.HeroProService;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.push.service.PushMgrService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.xml.cache.ArenaXMLInfoMap;
import com.snail.webgame.game.xml.info.PushXMLInfo;

public class ArenaMgtService {

	private static FightArenaDAO fightArenaDAO = FightArenaDAO.getInstance();

	/**
	 * 查询排名战列表
	 * @param roleId
	 * @return
	 */
	public QueryArenaResp queryArena(int roleId) {
		QueryArenaResp resp = new QueryArenaResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ARENA_QUERY_ERROR_1);
			return resp;
		}
		FightArenaInfo arenaInfo = null;
		synchronized (roleInfo) {
			arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleId);
			if (arenaInfo == null) {
				// 没有参加过的添加竞技场信息
				arenaInfo = ArenaService.createFightArena(roleInfo);
				if (fightArenaDAO.insertFightArena(arenaInfo)) {
					FightArenaInfoMap.addFightArenaInfo(arenaInfo);
				} else {
					resp.setResult(ErrorCode.ARENA_QUERY_ERROR_2);
					return resp;
				}
			}

			// 匹配战斗用户
			int result = ArenaService.recalMatchs(arenaInfo);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
			// 红点监听
			roleInfo.setArenaLogCheckTime(System.currentTimeMillis());
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_ARENA_CHALLENGED);
		}

		resp.setResult(1);
		resp.setRoleId(roleId);
		resp.setMaxPlace(arenaInfo.getMaxPlace());
		resp.setPlace(arenaInfo.getPlace());
		resp.setResetTime(arenaInfo.getResetTime());

		List<ArenaRe> list = ArenaService.getMatchs(arenaInfo);
		resp.setList(list);
		resp.setCount(list.size());
		return resp;
	}

	/**
	 * 最强王者排名
	 * @param roleId
	 * @param req
	 * @return
	 */
	public RankArenaResp rankArena(int roleId, RankArenaReq req) {
		RankArenaResp resp = new RankArenaResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ARENA_RANK_ERROR_1);
			return resp;
		}
		List<ArenaRe> list = new ArrayList<ArenaRe>();
		int page = req.getArenaPage();
		int startPlace = (page - 1) * GameValue.PER_ARENA_PAGE_NUM + 1;
		int endPlace = page * GameValue.PER_ARENA_PAGE_NUM;

		for (int place = startPlace; place <= endPlace; place++) {
			FightArenaInfo info = FightArenaInfoMap.getFightArenaInfobyPlace(place);
			if (info != null) {
				list.add(ArenaService.getArenaRe(info));
			}
		}
		resp.setResult(1);
		resp.setList(list);
		resp.setCount(list.size());
		return resp;
	}

	/**
	 * 匹配用户
	 * @param roleId
	 * @return
	 */
	public MatchArenaResp matchArena(int roleId) {
		MatchArenaResp resp = new MatchArenaResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ARENA_MATCH_ERROR_1);
			return resp;
		}
		FightArenaInfo arenaInfo = null;
		synchronized (roleInfo) {
			arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleId);
			if (arenaInfo == null) {
				resp.setResult(ErrorCode.ARENA_MATCH_ERROR_2);
				return resp;
			}
			// 匹配战斗用户
			int result = ArenaService.recalMatchs(arenaInfo);
			if (result != 1) {
				resp.setResult(result);
				return resp;
			}
		}

		List<ArenaRe> list = ArenaService.getMatchs(arenaInfo);
		resp.setList(list);
		resp.setCount(list.size());
		resp.setResult(1);
		return resp;
	}

	/**
	 * 重置 cd 时间
	 * @param roleId
	 * @return
	 */
	public ResetArenaResp resetArenaCD(int roleId) {
		ResetArenaResp resp = new ResetArenaResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.ARENA_BUY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleId);
			if (arenaInfo == null) {
				resp.setResult(ErrorCode.ARENA_BUY_ERROR_2);
				return resp;
			}

			resp.setResult(1);
			resp.setResetTime(arenaInfo.getResetTime());
			return resp;
		}
	}

	/**
	 * 竞技场战斗开始处理
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleInfo.getId());
		if (arenaInfo == null) {
			return ErrorCode.ARENA_FIGHT_START_ERROR_1;
		}
		int arenaId = NumberUtils.toInt(fightInfo.getDefendStr());
		FightArenaInfo fightArena = null;
		for (FightArenaInfo info : arenaInfo.getMatchs()) {
			if (info.getId() == arenaId) {
				fightArena = info;
				break;
			}
		}
		if (fightArena == null) {
			return ErrorCode.ARENA_FIGHT_START_ERROR_4;
		}
		int energy = GameValue.ARENA_FIGHT_COST_ENERGY;
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new EnergyCond(energy));
		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1) {
			return check;
		}

		// 扣精力体力时自动推送
		if (RoleService.subRoleResource(ActionType.action141.getType(), roleInfo, conds , null)) {
			String energyStr = roleInfo.getEnergy() + "," + roleInfo.getLastRecoverEnergyTime().getTime() + ","
					+ roleInfo.getRoleLoadInfo().getTodayBuyEnergyNum() + "," + 1;
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_ENERGY, energyStr);
		} else {
			return ErrorCode.ARENA_FIGHT_START_ERROR_6;
		}

		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(fightInfo.getFightType());
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(fightInfo.getFightType());
		FightSideData defendSide = getDefendFightSideData(fightArena, mainRate, otherRate);
		if (defendSide == null) {
			return ErrorCode.ARENA_FIGHT_START_ERROR_5;
		}

		fightInfo.setFightRoleId(fightArena.getRoleId());
		fightInfo.setFightArenaId(fightArena.getId());
		fightInfo.getFightDataList().add(defendSide);
		fightInfo.setCheckFlag((byte) 1);
		return 1;
	}

	/**
	 * 获取防守方战斗数据
	 * @param fightArena
	 * @return
	 */
	public static FightSideData getDefendFightSideData(FightArenaInfo fightArena,Map<HeroProType, Double> mainRate, Map<HeroProType, Double> otherRate) {
		FightSideData sideDate = new FightSideData();
		sideDate.setSideId(FightType.FIGHT_SIDE_1);
		sideDate.setSideRoleId(fightArena.getRoleId());
		sideDate.setSideName(fightArena.getRoleName());
		sideDate.setArmyInfos(new ArrayList<FightArmyDataInfo>());

		FightArmyDataInfo armyData = null;
		RoleInfo defendRole = null;
		int defendRoleId = fightArena.getRoleId();
		if (defendRoleId != 0) {
			defendRole = RoleInfoMap.getRoleInfo(defendRoleId);
			if (defendRole == null) {
				return null;
			}
			List<FightDeployInfo> fightDeployList = FightDeployService.getRoleFightDeployBy(defendRoleId);
			for (FightDeployInfo info : fightDeployList) {
				HeroInfo heroInfo = HeroInfoMap.getHeroInfo(info.getRoleId(), info.getHeroId());
				if (heroInfo == null) {
					return null;
				}
				armyData = FightService.getFightArmyDatabyHeroInfo(defendRole, heroInfo, info.getDeployPos(),
						sideDate.getSideId(), mainRate, otherRate,
						(byte) 1);
				if (armyData == null) {
					continue;
				}
				if (armyData.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
					armyData.setLookRange(GameValue.ARENA_LOOK_RANGE);
				}
				sideDate.getArmyInfos().add(armyData);
			}

		} else {
			Map<Byte, HeroRecord> fightDeployMap = fightArena.getFightDeployMap();
			for (HeroRecord record : fightDeployMap.values()) {
				armyData = FightService.getFightArmyDatabyHeroRecord(fightDeployMap, record, sideDate.getSideId(),
						GameValue.ARENA_NPC_EQUIP_RATE);
				if (armyData == null) {
					continue;
				}
				HeroPropertyInfo info = ArenaXMLInfoMap.getHeroPro(record.getHeroNo(), fightArena.getInitPlace(),
						HeroProService.getProRate(GameValue.ARENA_NPC_EQUIP_RATE));
				if (info != null) {
					FightService.setFightArmyDataInfo(armyData, info);
				}
				
				if (armyData.getLookRange() < GameValue.ARENA_LOOK_RANGE) {
					armyData.setLookRange(GameValue.ARENA_LOOK_RANGE);
				}
				sideDate.getArmyInfos().add(armyData);
			}
		}
		sideDate.setFightArmyNum(sideDate.getArmyInfos().size());
		if (sideDate.getArmyInfos().size() <= 0) {
			return null;
		}
		return sideDate;
	}

	/**
	 * 处理竞技场战斗结果
	 * @param fightResult 1-胜 2-败
	 * @param fightStartTime
	 * @param roleInfo
	 * @param prizeList
	 * @param fpPrizeList
	 * @return
	 */
	public static int dealFightEnd(int action,int fightResult, RoleInfo roleInfo, FightInfo fightInfo,
			List<BattlePrize> prizeList, List<BattlePrize> fpPrizeList) {
		// 竞技场排名公共锁
		synchronized (GameFlag.OBJ_ARENA) {
			FightArenaInfo arenaInfo = FightArenaInfoMap.getFightArenaInfo(roleInfo.getId());
			if (arenaInfo == null) {
				return ErrorCode.ARENA_FIGHT_END_ERROR_1;
			}

			int lastResult = fightResult;// 1-胜 2-败
			int roleBeforePlace = arenaInfo.getPlace();// 战斗前排名
			int roleBeforeMaxPlace = arenaInfo.getMaxPlace();// 战斗前历史排名

			int defendRoleId = fightInfo.getFightRoleId();
			RoleInfo defendRole = null;
			if (defendRoleId != 0) {
				defendRole = RoleInfoMap.getRoleInfo(defendRoleId);
				if (defendRole == null) {
					return ErrorCode.ARENA_FIGHT_END_ERROR_2;
				}
			}
			int defendArenaId = fightInfo.getFightArenaId();
			FightArenaInfo defendArena = FightArenaInfoMap.getFightArenaInfobyArenaId(defendArenaId);
			if (defendArena == null) {
				return ErrorCode.ARENA_FIGHT_END_ERROR_3;
			}
			int defendBeforePlace = defendArena.getPlace();// 战斗前排名
			int result = changePlace(lastResult, arenaInfo, defendArena);
			if (result != 1) {
				return result;
			}

			int maxPlace = arenaInfo.getMaxPlace();
			int hisPlaceGold = 0;
			if (maxPlace < roleBeforeMaxPlace) {
				// 历史最高排名提升奖励
				hisPlaceGold = (int) ArenaXMLInfoMap.getHisGoldPrize(roleBeforeMaxPlace, maxPlace);
				int result1 = ArenaService.sendMaxPlaceUpReward(roleInfo, hisPlaceGold, roleBeforeMaxPlace, maxPlace);
				if (result1 != 1) {
					return result1;
				}
			}

			// 排名提升提示
			String param = (roleBeforePlace - arenaInfo.getPlace()) + ","
					+ (defendBeforePlace - defendArena.getPlace());
			param += "," + arenaInfo.getPlace() + "," + defendArena.getPlace();
			param += "," + roleBeforeMaxPlace + "," + maxPlace + "," + hisPlaceGold;
			fightInfo.setEndRespDefendStr(param);

			if (fightResult == 1) {
				// 战斗胜利
				int prize = dealDropInfo(action,roleInfo, prizeList, fpPrizeList,true);
				if (prize != 1) {
					return prize;
				}
				// 如果对方不在线而且输了发送推送消息给对方
				if (defendRole != null) {
					PushMgrService.dealOfflinePush(defendRole, PushXMLInfo.PUSH_NO_ARENA);
				}
				
				// 对方排名下降
				ArenaService.sendDefendPalcechanged(defendRole, roleInfo.getRoleName(), defendBeforePlace, defendArena.getPlace());
			}

			// 任务
			QuestService.checkQuest(roleInfo, ActionType.action142.getType(), null, true, true);
			// 红点监听竞技场
			if (defendRole != null) {
				RedPointMgtService.check2PopRedPoint(defendRole.getId(), null, true,
						GameValue.RED_POINT_TYPE_ARENA_CHALLENGED);
			}
			// 添加战斗日志
			ArenaService.addFightArenaLog(fightResult, fightInfo.getFightTime(), arenaInfo, roleBeforePlace,
					defendArena, defendBeforePlace, prizeList, fpPrizeList);
		}

		return 1;
	}

	/**
	 * 交换排名
	 * @param lastResult
	 * @param arenaInfo
	 * @param defendArena
	 * @return
	 */
	private static int changePlace(int lastResult, FightArenaInfo arenaInfo, FightArenaInfo defendArena) {
		int roleBeforePlace = arenaInfo.getPlace();// 战斗前排名
		int defendBeforePlace = defendArena.getPlace();// 战斗前排名
		// 战斗胜利 低排名挑战高排名
		if (lastResult == 1 && roleBeforePlace > defendBeforePlace) {// 交换排名
			// 用户
			int rolePlace = defendBeforePlace;
			int roleMaxPlace = arenaInfo.getMaxPlace();
			if (rolePlace < roleMaxPlace) {
				roleMaxPlace = rolePlace;
			}

			int defendPlace = roleBeforePlace;
			int defendMaxPlace = defendArena.getMaxPlace();
			if (defendPlace < defendMaxPlace) {
				defendMaxPlace = defendPlace;
			}
			if (fightArenaDAO.updateFightArenaPlace(arenaInfo.getId(), rolePlace, roleMaxPlace, defendArena.getId(),
					defendPlace, defendMaxPlace)) {
				arenaInfo.setPlace(rolePlace);
				arenaInfo.setMaxPlace(roleMaxPlace);

				defendArena.setPlace(defendPlace);
				defendArena.setMaxPlace(defendMaxPlace);

				FightArenaInfoMap.changePlace(arenaInfo, defendArena);
			} else {
				return ErrorCode.ARENA_FIGHT_END_ERROR_4;
			}
		}
		return 1;
	}

	/**
	 * 获取翻牌掉落物品
	 * flag false-不掉经验 true-掉经验
	 * @return
	 */
	public static int dealDropInfo(int action,RoleInfo roleInfo, List<BattlePrize> prizeList, List<BattlePrize> fpPrizeList,boolean flag) {
		List<DropInfo> addList = new ArrayList<DropInfo>();
		if(flag)
		{
			int addExp = GameValue.ARENA_FIGHT_FP_EXP_RATE * HeroInfoMap.getMainHeroLv(roleInfo.getId()) + GameValue.ARENA_FIGHT_FP_EXP_VAL;
			addList.add(new DropInfo(ConditionType.TYPE_EXP.getName(), addExp));
		}
		
		String cardBag = GameValue.ARENA_FIGHT_FP_BAG;

		return ItemService.addPrize(ActionType.action142.getType(), roleInfo, addList, cardBag, null, null, null,
				null, null, prizeList, fpPrizeList, null, true);
	}
}
