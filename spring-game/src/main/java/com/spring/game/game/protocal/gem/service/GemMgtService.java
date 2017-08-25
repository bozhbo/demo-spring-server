package com.snail.webgame.game.protocal.gem.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import com.snail.webgame.game.cache.FightGemInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightInfo;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.core.GameLogService;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.FightGemDAO;
import com.snail.webgame.game.dao.typehandler.IntegerListTypeHandler;
import com.snail.webgame.game.info.FightGemInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.fight.fightend.BattlePrize;
import com.snail.webgame.game.protocal.gem.buy.BuyGemResp;
import com.snail.webgame.game.protocal.gem.prize.GetGemPrizeReq;
import com.snail.webgame.game.protocal.gem.prize.GetGemPrizeResp;
import com.snail.webgame.game.protocal.gem.query.QueryGemResp;
import com.snail.webgame.game.protocal.gem.reset.ResetGemResp;
import com.snail.webgame.game.protocal.gem.sweep.SweepGemResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.quest.service.QuestService;
import com.snail.webgame.game.protocal.redPoint.service.RedPointMgtService;
import com.snail.webgame.game.xml.cache.PlayXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.PlayXMLBattle;
import com.snail.webgame.game.xml.info.PlayXMLInfo;

public class GemMgtService {

	private FightGemDAO fightGemDAO = FightGemDAO.getInstance();

	/**
	 * 查询刷新宝石活动信息
	 * @param roleId
	 * @return
	 */
	public QueryGemResp queryGem(Integer roleId) {
		QueryGemResp resp = new QueryGemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_QUERY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleId);
			if (fightGemInfo == null) {
				fightGemInfo = new FightGemInfo();
				fightGemInfo.setRoleId(roleId);
				if (fightGemDAO.insertFightGemInfo(fightGemInfo)) {
					FightGemInfoMap.addFightGemInfo(fightGemInfo);
				} else {
					resp.setResult(ErrorCode.GEM_QUERY_ERROR_2);
					return resp;
				}
			}
			PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.GEM_QUERY_ERROR_3);
				return resp;
			}

			resp.setResult(1);
			resp.setFightNum(xmlInfo.getChallengeTimes() - fightGemInfo.getFightNum());
			resp.setResetNum(fightGemInfo.getCurrResetNum());
			resp.setResetLimit(fightGemInfo.getCurrResetLimit());
			resp.setBuyNum(fightGemInfo.getCurrBuyNum());
			resp.setLastFightBattleNo(fightGemInfo.getLastFightBattleNo());
			resp.setLastFightResult(fightGemInfo.getLastFightResult());
			resp.setMaxFightBattleNo(fightGemInfo.getMaxFightBattleNo());
			resp.setPrizeBattleNos(IntegerListTypeHandler.getString(GemService.getPrizeBattleNos(fightGemInfo)));
			return resp;
		}
	}

	/**
	 * 重置宝石活动
	 * @param roleId
	 * @return
	 */
	public ResetGemResp resetGem(int roleId) {
		ResetGemResp resp = new ResetGemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_RESET_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.GEM_QUERY_ERROR_2);
				return resp;
			}
			FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleId);
			if (fightGemInfo == null) {
				resp.setResult(ErrorCode.GEM_RESET_ERROR_3);
				return resp;
			}
			int resetNum = fightGemInfo.getCurrResetNum();
			int resetLimit = fightGemInfo.getCurrResetLimit();
			if (resetNum >= resetLimit) {
				resp.setResult(ErrorCode.GEM_RESET_ERROR_4);
				return resp;
			}
			int currResetNum = resetNum + 1;
			Timestamp lastGemResetTime = new Timestamp(System.currentTimeMillis());
			if (fightGemDAO.updateResetNum(fightGemInfo.getId(), 0, currResetNum, lastGemResetTime, 0, 0)) {
				fightGemInfo.setFightNum(0);
				fightGemInfo.setResetNum(currResetNum);
				fightGemInfo.setLastResetTime(lastGemResetTime);
				fightGemInfo.setLastFightResult(0);
				fightGemInfo.setLastFightBattleNo(0);
			} else {
				resp.setResult(ErrorCode.GEM_RESET_ERROR_5);
				return resp;
			}
			resp.setResult(1);
			resp.setFightNum(xmlInfo.getChallengeTimes() - fightGemInfo.getFightNum());
			resp.setResetNum(fightGemInfo.getCurrResetNum());
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_EXPERIENCE_1);
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action281.getType(), "");
			return resp;
		}
	}

	/**
	 * 购买重置次数
	 * @param roleId
	 * @return
	 */
	public BuyGemResp buyGem(int roleId) {
		BuyGemResp resp = new BuyGemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_BUY_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleId);
			if (fightGemInfo == null) {
				resp.setResult(ErrorCode.GEM_BUY_ERROR_2);
				return resp;
			}

			resp.setResult(1);
			resp.setResetLimit(fightGemInfo.getCurrResetLimit());
			resp.setBuyNum(fightGemInfo.getCurrBuyNum());
			// 红点监听
			RedPointMgtService.check2PopRedPoint(roleId, null, true, GameValue.RED_POINT_TYPE_EXPERIENCE_1);
			return resp;
		}
	}

	/**
	 * 扫荡
	 * @param roleId
	 * @return
	 */
	public SweepGemResp sweepGem(int roleId) {
		SweepGemResp resp = new SweepGemResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_SWRRP_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleId);
			if (fightGemInfo == null) {
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_2);
				return resp;
			}
			PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_3);
				return resp;
			}
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_7);
				return resp;
			}

			int beginBattleNo = fightGemInfo.getLastFightBattleNo();
			int lastFightResult = fightGemInfo.getLastFightResult();// 1-胜 2-败
			if (lastFightResult == 2) {
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_4);
				return resp;
			} else if (lastFightResult == 1) {
				beginBattleNo += 1;
			} else if (lastFightResult == 0) {
				beginBattleNo = xmlInfo.getFristBattleNo();
			}
			int maxFightBattleNo = fightGemInfo.getMaxFightBattleNo();
			if (maxFightBattleNo < beginBattleNo) {
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_5);
				return resp;
			}

			if (fightGemDAO.updateFightResult(fightGemInfo.getId(), 1, maxFightBattleNo)) {
				fightGemInfo.setLastFightResult(1);
				fightGemInfo.setLastFightBattleNo(maxFightBattleNo);
			} else {
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_6);
				return resp;
			}
			List<DropInfo> addList = new ArrayList<DropInfo>();

			List<DropXMLInfo> prizeXmls = null;
			PlayXMLBattle battleXMLInfo = null;
			for (int no = beginBattleNo; no <= maxFightBattleNo; no++) {
				battleXMLInfo = xmlInfo.getBattles().get(no);
				if (battleXMLInfo != null) {
					prizeXmls = PropBagXMLMap.getPropBagXMLListbyStr(battleXMLInfo.getDropBag());
					if (prizeXmls != null) {
						ItemService.getDropXMLInfo(roleInfo, prizeXmls, addList);
					}
				}
			}

			List<BattlePrize> dropList = new ArrayList<BattlePrize>();
			int result = ItemService.addPrize(ActionType.action283.getType(), roleInfo, addList, null,
					null,null,null,
					null,null,dropList,
					null,null,true);
			if (result != 1) 
			{
				resp.setResult(result);
				return resp;
			}

			resp.setResult(1);
			resp.setLastFightBattleNo(fightGemInfo.getLastFightBattleNo());
			resp.setLastFightResult(fightGemInfo.getLastFightResult());
			if (dropList != null && dropList.size() > 0) {
				resp.setPrizeNum(dropList.size());
				resp.setPrize(dropList);
			}
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action283.getType(), "");
			return resp;
		}
	}

	/**
	 * 领取关卡奖励
	 * @param roleId
	 * @param req
	 * @return
	 */
	public GetGemPrizeResp getGemPrize(int roleId, GetGemPrizeReq req) {
		GetGemPrizeResp resp = new GetGemPrizeResp();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(roleId);
		if (roleInfo == null) {
			resp.setResult(ErrorCode.GEM_PRIZE_ERROR_1);
			return resp;
		}
		synchronized (roleInfo) {
			FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleId);
			if (fightGemInfo == null) {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_2);
				return resp;
			}
			
			//判断背包是否足够
			int checkItem = ItemService.addItemAndEquipCheck(roleInfo);
			if(checkItem != 1){
				resp.setResult(ErrorCode.GEM_SWRRP_ERROR_8);
				return resp;
			}
			
			int battleNo = req.getBattleNo();
			PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
			if (xmlInfo == null) {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_3);
				return resp;
			}
			PlayXMLBattle battleXMLInfo = xmlInfo.getBattles().get(battleNo);
			if (battleXMLInfo == null) {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_4);
				return resp;
			}
			if (fightGemInfo.getPrizeBattleNos() != null && fightGemInfo.getPrizeBattleNos().contains(battleNo)) {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_5);
				return resp;
			}
			if (fightGemInfo.getLastFightResult() == 1) {
				if (fightGemInfo.getLastFightBattleNo() < battleNo) {
					resp.setResult(ErrorCode.GEM_PRIZE_ERROR_6);
					return resp;
				}
			} else if (fightGemInfo.getLastFightResult() == 2) {
				if (fightGemInfo.getLastFightBattleNo() <= battleNo) {
					resp.setResult(ErrorCode.GEM_PRIZE_ERROR_7);
					return resp;
				}
			} else {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_7);
				return resp;
			}

			String dropBag = battleXMLInfo.getCaseDropBag();
			if (dropBag == null || dropBag.length() <= 0) {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_8);
				return resp;
			}
			List<Integer> prizes = null;
			if (fightGemInfo.getPrizeBattleNos() == null) {
				prizes = new ArrayList<Integer>();
			} else {
				prizes = new ArrayList<Integer>(fightGemInfo.getPrizeBattleNos());
			}
			prizes.add(battleNo);
			if (fightGemDAO.updatePrizeBattleNos(fightGemInfo.getId(), prizes)) {
				fightGemInfo.setPrizeBattleNos(prizes);
			} else {
				resp.setResult(ErrorCode.GEM_PRIZE_ERROR_9);
				return resp;
			}

			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLListbyStr(dropBag);
			List<BattlePrize> getPropList = new ArrayList<BattlePrize>();
			if (list != null) {
				int result = ItemService.addPrizeForPropBag(ActionType.action286.getType(), roleInfo, list,null, 
						getPropList,null,null, null, false);
				
				if (result != 1) {
					resp.setResult(result);
					return resp;
				}
			}

			resp.setResult(1);
			resp.setPrizeBattleNos(IntegerListTypeHandler.getString(GemService.getPrizeBattleNos(fightGemInfo)));
			if (getPropList != null && getPropList.size() > 0) {
				resp.setPrizeNum(getPropList.size());
				resp.setPrize(getPropList);
			}
			
			// 任务
			boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action182.getType(), null, true, false);
			boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, GameValue.RED_POINT_STONE_COMPOSE);
			//红点推送
			if(isRedPointQuest || isRedPointMonth){
				RedPointMgtService.pop(roleInfo.getId());
			}
			
			GameLogService.insertPlayActionLog(roleInfo, ActionType.action286.getType(), req.getBattleNo()+"");
			return resp;
		}
	}

	/**
	 * 宝石活动开始处理
	 * @param roleInfo
	 * @param fightInfo
	 * @return
	 */
	public static int dealFightStart(RoleInfo roleInfo, FightInfo fightInfo) {
		FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleInfo.getId());
		if (fightGemInfo == null) {
			return ErrorCode.GEM_FIGHT_START_ERROR_1;
		}
		PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
		if (xmlInfo == null) {
			return ErrorCode.GEM_FIGHT_START_ERROR_2;
		}
		int currFightBattleNo = NumberUtils.toInt(fightInfo.getDefendStr());
		PlayXMLBattle battle = xmlInfo.getBattles().get(currFightBattleNo);
		if (battle == null) {
			return ErrorCode.GEM_FIGHT_START_ERROR_3;
		}
		int fightNum = fightGemInfo.getFightNum();
		int lastFightResult = fightGemInfo.getLastFightResult();// 1-胜 2-败
		int LastFightBattleNo = fightGemInfo.getLastFightBattleNo();

		if (lastFightResult == 1) {
			if (LastFightBattleNo == xmlInfo.getLastBattleNo()) {
				return ErrorCode.GEM_FIGHT_START_ERROR_8;
			}
			if (battle.getBattleCondition() != 0 && battle.getBattleCondition() != LastFightBattleNo) {
				return ErrorCode.GEM_FIGHT_START_ERROR_4;
			}
		} else if (lastFightResult == 2) {
			if (fightNum >= xmlInfo.getChallengeTimes()) {
				return ErrorCode.GEM_FIGHT_START_ERROR_5;
			}
			if (battle.getBattleCondition() != 0 && currFightBattleNo != LastFightBattleNo) {
				return ErrorCode.GEM_FIGHT_START_ERROR_6;
			}
		}
		return 1;
	}

	/**
	 * 宝石活动结果处理
	 * @param fightResult // 1-胜 2-败
	 * @param roleInfo
	 * @param fightInfo
	 * @param getResourceNum 资源变动
	 * @param getPrizeMum 道具变动
	 * @param addEquipIds 装备变动（掉装备）
	 * @return
	 */
	public static int dealFightEnd(int fightResult, RoleInfo roleInfo, FightInfo fightInfo, List<BattlePrize> prizeList) {
		FightGemInfo fightGemInfo = FightGemInfoMap.getFightGemInfo(roleInfo.getId());
		if (fightGemInfo == null) {
			return ErrorCode.GEM_FIGHT_END_ERROR_1;
		}
		PlayXMLInfo xmlInfo = PlayXMLInfoMap.getPlayXMLInfo(PlayXMLInfo.PLAY_TYPE_1);
		if (xmlInfo == null) {
			return ErrorCode.GEM_FIGHT_END_ERROR_2;
		}
		int currFightBattleNo = NumberUtils.toInt(fightInfo.getStartRespDefendStr());
		PlayXMLBattle battle = xmlInfo.getBattles().get(currFightBattleNo);
		if (battle == null) {
			return ErrorCode.GEM_FIGHT_END_ERROR_3;
		}
		if (fightResult == 1) {
			int maxFightBattleNo = fightGemInfo.getMaxFightBattleNo();
			if (currFightBattleNo > maxFightBattleNo) {
				if (FightGemDAO.getInstance().updateFightResult(fightGemInfo.getId(), fightResult, currFightBattleNo,
						currFightBattleNo)) {
					fightGemInfo.setLastFightResult(fightResult);
					fightGemInfo.setLastFightBattleNo(currFightBattleNo);
					fightGemInfo.setMaxFightBattleNo(currFightBattleNo);
				} else {
					return ErrorCode.GEM_FIGHT_END_ERROR_4;
				}
			} else {
				if (FightGemDAO.getInstance().updateFightResult(fightGemInfo.getId(), fightResult, currFightBattleNo)) {
					fightGemInfo.setLastFightResult(fightResult);
					fightGemInfo.setLastFightBattleNo(currFightBattleNo);
				} else {
					return ErrorCode.GEM_FIGHT_END_ERROR_5;
				}
			}
		} else {
			int currFightNum = fightGemInfo.getFightNum() + 1;
			if (FightGemDAO.getInstance().updateFightFault(fightGemInfo.getId(), fightResult, currFightBattleNo,
					currFightNum)) {
				fightGemInfo.setLastFightResult(fightResult);
				fightGemInfo.setLastFightBattleNo(currFightBattleNo);
				fightGemInfo.setFightNum(currFightNum);
			} else {
				return ErrorCode.GEM_FIGHT_END_ERROR_5;
			}
		}
		SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_GEM, "");

		if (fightResult == 1) {
			String dropBag = battle.getDropBag();
			List<DropXMLInfo> list = PropBagXMLMap.getPropBagXMLList(dropBag);
			if (list != null) {
				int check = ItemService.addPrizeForPropBag(ActionType.action285.getType(), roleInfo,list,null,
						prizeList, null,null,null,false);
				
				if (check != 1) {
					return check;
				}
			}
		}

		// 任务检测
		boolean isRedPointQuest = QuestService.checkQuest(roleInfo, ActionType.action285.getType(), null, true, false);
		boolean isRedPointMonth = RedPointMgtService.check2PopRedPoint(roleInfo.getId(), null, false, GameValue.RED_POINT_STONE_COMPOSE);
		//红点推送
		if(isRedPointQuest || isRedPointMonth){
			RedPointMgtService.pop(roleInfo.getId());
		}
		// 日志
		String actValue = currFightBattleNo + "-" + fightResult;
		GameLogService.insertPlayActionLog(roleInfo, ActionType.action285.getType(), actValue);
		return 1;
	}
}
