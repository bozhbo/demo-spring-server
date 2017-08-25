package com.snail.webgame.game.protocal.challenge.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snail.webgame.game.cache.ChallengeBattleInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.DateUtil;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.ChallengeBattleDAO;
import com.snail.webgame.game.info.ChallengeBattleInfo;
import com.snail.webgame.game.info.ChallengeUpdateInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.BattleDetailRe;
import com.snail.webgame.game.protocal.challenge.queryBattleDetail.ChapterDetailRe;
import com.snail.webgame.game.protocal.challenge.refresh.RefreshBattlesResp;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.xml.cache.ChallengeBattleXmlInfoMap;
import com.snail.webgame.game.xml.cache.GWXMLInfoMap;
import com.snail.webgame.game.xml.cache.PropBagXMLMap;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetail;
import com.snail.webgame.game.xml.info.ChallengeBattleXmlInfo.BattleDetailPoint;
import com.snail.webgame.game.xml.info.DropXMLInfo;
import com.snail.webgame.game.xml.info.GWXMLInfo;

public class ChallengeService {

	/**
	 * 战斗后刷新副本信息
	 * 
	 * @param roleInfo 玩家信息
	 * @param challengeBattleInfo 副本信息
	 * @param forother 是否全部刷新
	 * @param flag 0-普通刷新 1-升级刷新
	 */
	public static void refreshBattles(RoleInfo roleInfo, ChallengeBattleInfo info, boolean forother, int flag) {
		List<BattleDetailRe> list = new ArrayList<BattleDetailRe>();
		List<String> newList = new ArrayList<String>();
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (forother) {
			if(flag == 0){
				List<ChallengeUpdateInfo> updateList = new ArrayList<ChallengeUpdateInfo>();
				// 玩家可打的副本信息
				List<BattleDetail> details = ChallengeBattleXmlInfoMap.getTotals();
				if (details != null) {
					for (BattleDetail detail : details) {
						ChallengeUpdateInfo updateInfo = new ChallengeUpdateInfo();
						if (mainHero == null || mainHero.getHeroLevel() < detail.getUnLockLv()) {
							continue;
						}
						ChallengeBattleInfo battle = ChallengeBattleInfoMap.getBattleInfo(roleInfo.getId(), (byte)detail.getChapterType(),
								 detail.getChapterNo(), detail.getBattleNo());
						if (battle == null) {
							if(roleInfo.getRoleLoadInfo().getChallengeOpen() != 1){
								int condCheck = AbstractConditionCheck.checkCondition(roleInfo,detail.getConds());
								if (condCheck == 1) {
									// 检测副本开启(时间,次数限制)
									newChallengeAdd(roleInfo, list, detail);
									newList.add(detail.getBattleNo()+"");
								}
							} else {
								newChallengeAdd(roleInfo, list, detail);
								newList.add(detail.getBattleNo()+"");
							}
						} else {
							// 有时间,次数的副本,防止玩家到了更新时间后,一直未重新登录而引起的不能刷新新副本
							ChallengeService.oldChallengeAdd(roleInfo, list, battle, detail,updateInfo);
							if(updateInfo.getId() > 0)
							{
								updateList.add(updateInfo);
							}
						}
					}
					
					if(updateList.size() > 0)
					{
						ChallengeBattleDAO.getInstance().updateChallengeAttackNumBatch(updateList);
					}
				}
			} else if(flag == 1) {
				if(mainHero != null){
					ArrayList<BattleDetail> battleList = ChallengeBattleXmlInfoMap.getBattleLevel(mainHero.getHeroLevel());
					if(battleList != null && battleList.size() > 0){
						List<BattleDetailRe> roleBattle = roleInfo.getBattleList();
						if(roleBattle != null && roleBattle.size() > 0){
							list.addAll(roleBattle);
						}
						List<String> newBattle = roleInfo.getNewBattleList();
						if(newBattle != null && newBattle.size() > 0){
							newList.addAll(newBattle);
						}
						for(BattleDetail detail : battleList){
							if(roleInfo.getRoleLoadInfo().getChallengeOpen() != 1){
								int condCheck = AbstractConditionCheck.checkCondition(roleInfo,detail.getConds());
								if (condCheck == 1) {
									// 检测副本开启(时间,次数限制)
									newChallengeAdd(roleInfo, list, detail);
									newList.add(detail.getBattleNo()+"");
								}
							} else {
								newChallengeAdd(roleInfo, list, detail);
								newList.add(detail.getBattleNo()+"");
							}
						}
					}
				}
			}
		} else {
			if (info != null) {
				list.add(new BattleDetailRe(info.getChallengeType(), (short)info.getChapterNo(), info.getBattleId(), info.getStar(), info
						.getCanFightNum(), info.getFightTime().getTime(),(byte)1, (byte)info.getGoldNum()));
			}
		}
		if (list.size() > 0 || newList.size() > 0) {
			RefreshBattlesResp resp = new RefreshBattlesResp();
			resp.setResult(1);
			resp.setCount(list.size());
			resp.setList(list);
			String newBattle = "";
			if(newList != null && newList.size() > 0){
				for(String battle : newList){
					if(newBattle.length() <= 0){
						newBattle = battle;
					} else {
						newBattle = newBattle + "," + battle;
					}
				}
				roleInfo.setNewBattleList(newList);
			}
			resp.setBattle(newBattle);
			roleInfo.setBattleList(list);
			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CHALLENGE_BATTLE, resp);
		}
	}

	/**
	 * 检测副本是否有时间,次数限制
	 * @param roleId 玩家ID
	 * @param info 副本信息
	 * @param battleDetail
	 * @return
	 */
	public static int checkChallengeTimeNum(long roleId, ChallengeBattleInfo info, BattleDetail battleDetail) {

		int canFightNum = battleDetail.getBattleNum();

		if (battleDetail.getBattleNum() != -1 && !battleDetail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有星期,有次数限制
			int weekDay = DateUtil.getWeekDay_Hour(1);
			int hour = DateUtil.getWeekDay_Hour(2);

			// 在开启时间内(某副本周三五点开启,周三五点后玩家未登录,周四5点前上线,该副本仍有效)
			if (!((battleDetail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= battleDetail
					.getBattleRefreshTime()) || (battleDetail.getBattleWeekDay().indexOf(
					((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= battleDetail.getBattleRefreshTime()))) {
				return ErrorCode.START_FIGHT_ERROR_2;
			}
			if (info != null) {
				if (info.getCanFightNum() <= 0) {
					return ErrorCode.START_FIGHT_ERROR_1;
				}
			} else {
				if (canFightNum <= 0) {
					return ErrorCode.START_FIGHT_ERROR_1;
				}
			}
		} else if (battleDetail.getBattleNum() != -1 && battleDetail.getBattleWeekDay().equalsIgnoreCase("-1")) {
			// 有次数,无星期限制
			if (info != null) {
				if (info.getCanFightNum() <= 0) {
					return ErrorCode.START_FIGHT_ERROR_1;
				}
			} else {
				if (canFightNum <= 0) {
					return ErrorCode.START_FIGHT_ERROR_1;
				}
			}
		} else if (battleDetail.getBattleNum() == -1 && !battleDetail.getBattleWeekDay().equalsIgnoreCase("-1")) {
			// 有星期,无次数限制
			int weekDay = DateUtil.getWeekDay_Hour(1);
			int hour = DateUtil.getWeekDay_Hour(2);

			if (!((battleDetail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= battleDetail
					.getBattleRefreshTime()) || (battleDetail.getBattleWeekDay().indexOf(
					((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= battleDetail.getBattleRefreshTime()))) {
				return ErrorCode.START_FIGHT_ERROR_2;
			}
		}
		return 1;
	}

	/**
	 * 新副本开启时间次数检测
	 * @param roleInfo
	 * @param list
	 * @param detail
	 */
	public static void newChallengeAdd(RoleInfo roleInfo, List<BattleDetailRe> list, BattleDetail detail) {

		int weekDay = DateUtil.getWeekDay_Hour(1);
		int hour = DateUtil.getWeekDay_Hour(2);

		int canFightNum = detail.getBattleNum();
		Timestamp fightTime = new Timestamp(0);
		
		if (detail.getBattleNum() != -1 && !detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有星期,有次数限制
			// 在开启时间内(某副本周三五点开启,周三五点后玩家未登录,周四5点前上线,该副本仍有效)
			if (!((detail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= detail.getBattleRefreshTime()) || (detail
					.getBattleWeekDay().indexOf(((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= detail
					.getBattleRefreshTime()))) {
				return;
			}
			list.add(new BattleDetailRe(detail.getChapterType(), (short)detail.getChapterNo(), detail.getBattleNo(), "", canFightNum,
					fightTime.getTime(), (byte)0, (byte)0));

		} else if (detail.getBattleNum() != -1 && detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有次数,无星期限制
			list.add(new BattleDetailRe(detail.getChapterType(), (short)detail.getChapterNo(), detail.getBattleNo(), "", canFightNum, 0,(byte)0, (byte)0));

		} else if (detail.getBattleNum() == -1 && !detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有星期,无次数限制
			if (!((detail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= detail.getBattleRefreshTime()) || (detail
					.getBattleWeekDay().indexOf(((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= detail
					.getBattleRefreshTime()))) {
				return;
			}
			list.add(new BattleDetailRe(detail.getChapterType(), (short)detail.getChapterNo(), detail.getBattleNo(), "", -1, 0, (byte)0, (byte)0));

		} else {
			// 无星期,无次数限制
			list.add(new BattleDetailRe(detail.getChapterType(),(short)detail.getChapterNo(), detail.getBattleNo(), "", -1, 0,(byte)0, (byte)0));

		}
	}

	/**
	 * 已开启副本次数,时间,次数限制检测
	 * @param roleInfo
	 * @param list
	 * @param info
	 * @param detail
	 */
	public static void oldChallengeAdd(RoleInfo roleInfo, List<BattleDetailRe> list, ChallengeBattleInfo info, BattleDetail detail,ChallengeUpdateInfo updateInfo) {

		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		if (mainHero == null || mainHero.getHeroLevel() < detail.getUnLockLv()) {
			return;
		}
		int weekDay = DateUtil.getWeekDay_Hour(1);
		int hour = DateUtil.getWeekDay_Hour(2);

		if (detail.getBattleNum() != -1 && !detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有星期,有次数限制
			// 在开启时间内(某副本周三五点开启,周三五点后玩家未登录,周四5点前上线,该副本仍有效)
			if (!((detail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= detail.getBattleRefreshTime()) || (detail
					.getBattleWeekDay().indexOf(((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= detail
					.getBattleRefreshTime()))) {

				/*if (!ChallengeBattleDAO.getInstance().updateChallengeAttackNum(info.getId(), 0, new Timestamp(System.currentTimeMillis()))) {
					return;
				}*/

				
				info.setCanFightNum(0);
				
				updateInfo.setId(info.getId());
				updateInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
				updateInfo.setFightNum(0);
				updateInfo.setGoldNum(0);

				return;
			} else {
				// 副本重置时间到了
				if (!DateUtil.isSameDay(info.getFightTime().getTime(), System.currentTimeMillis())
						&& info.getCanFightNum() != detail.getBattleNum()) {
					/*if (!ChallengeBattleDAO.getInstance()
							.updateChallengeAttackNum(info.getId(), detail.getBattleNum(), info.getFightTime())) {
						return;
					}*/

					info.setCanFightNum(detail.getBattleNum());
					info.setGoldNum(0);
					/*if(ChallengeBattleDAO.getInstance().updateGoldBuy(info.getId(), 0)){
						info.setGoldNum(0);
					}*/
					
					updateInfo.setId(info.getId());
					updateInfo.setFightTime(info.getFightTime());
					updateInfo.setFightNum(detail.getBattleNum());
					updateInfo.setGoldNum(0);
				}
			}
		} else if (detail.getBattleNum() != -1 && detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有次数,无星期限制(精英副本)
			if (hour >= detail.getBattleRefreshTime()
					&& !DateUtil.isSameDay(info.getFightTime().getTime(), System.currentTimeMillis())) {
				// 精英副本刷新次数时间到了
				/*if (!ChallengeBattleDAO.getInstance().updateChallengeAttackNum(info.getId(), detail.getBattleNum(), info.getFightTime())
						&& info.getCanFightNum() != detail.getBattleNum()) {
					return;
				}*/

				info.setCanFightNum(detail.getBattleNum());
				info.setGoldNum(0);
				/*if(ChallengeBattleDAO.getInstance().updateGoldBuy(info.getId(), 0)){
					info.setGoldNum(0);
				}*/
				
				updateInfo.setId(info.getId());
				updateInfo.setFightTime(info.getFightTime());
				updateInfo.setFightNum(detail.getBattleNum());
				updateInfo.setGoldNum(0);
			}
		} else if (detail.getBattleNum() == -1 && !detail.getBattleWeekDay().equalsIgnoreCase("-1")) {

			// 有星期,无次数限制
			if (!((detail.getBattleWeekDay().indexOf(weekDay + "") > -1 && hour >= detail.getBattleRefreshTime()) || (detail
					.getBattleWeekDay().indexOf(((weekDay - 1) == 0 ? 7 : (weekDay - 1)) + "") > -1 && hour <= detail
					.getBattleRefreshTime()))) {

				/*if (!ChallengeBattleDAO.getInstance().updateChallengeAttackNum(info.getId(), 0, new Timestamp(System.currentTimeMillis()))) {
					return;
				}*/

				info.setCanFightNum(0);
				info.setGoldNum(0);
				/*if(ChallengeBattleDAO.getInstance().updateGoldBuy(info.getId(), 0)){
					info.setGoldNum(0);
				}*/

				updateInfo.setId(info.getId());
				updateInfo.setFightTime(new Timestamp(System.currentTimeMillis()));
				updateInfo.setFightNum(0);
				updateInfo.setGoldNum(0);
				
				return;
			}
		} else {
			// 无星期,无次数限制
			info.setCanFightNum(-1);
		}
		BattleDetailRe battleDetail = new BattleDetailRe(info.getChallengeType(), (short)info.getChapterNo(), info.getBattleId(), info.getStar(), info
				.getCanFightNum(), info.getFightTime().getTime(),(byte)1,(byte)info.getGoldNum());
		if(!list.contains(battleDetail))
		{
			list.add(battleDetail);
		}
	}
	
	/**
	 * 获取副本
	 * @param roleInfo
	 * @return
	 */
	public static List<BattleDetailRe> getBattleList(RoleInfo roleInfo, List<String> newList)
	{
		List<BattleDetailRe> battleList = new ArrayList<BattleDetailRe>();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if(roleLoadInfo == null){
			return null;
		}
		
		List<ChallengeUpdateInfo> updateList = new ArrayList<ChallengeUpdateInfo>();
		HeroInfo mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
		// 玩家可打的副本信息
		List<BattleDetail> details = ChallengeBattleXmlInfoMap.getTotals();
		if (details != null) {
			for (BattleDetail detail : details) {
				ChallengeUpdateInfo updateInfo = new ChallengeUpdateInfo();
				if (mainHero == null || mainHero.getHeroLevel() < detail.getUnLockLv()) {
					continue;
				}
				ChallengeBattleInfo battle = ChallengeBattleInfoMap.getBattleInfo(roleInfo.getId(), (byte)detail.getChapterType(),
						detail.getChapterNo(), detail.getBattleNo());
				if (battle == null) {
					// 玩家可打的副本信息
					if(roleLoadInfo.getChallengeOpen() != 1) {
						//新副本
						if(detail.getConds() != null && detail.getConds().size() > 0) {
							int condCheck = AbstractConditionCheck.checkCondition(roleInfo,detail.getConds());
							if (condCheck == 1) {
								ChallengeService.newChallengeAdd(roleInfo, battleList, detail);
								newList.add(detail.getBattleNo()+"");
							}
						} else {
							//新副本
							ChallengeService.newChallengeAdd(roleInfo, battleList, detail);
							newList.add(detail.getBattleNo()+"");
						}
					} else {
						//新副本
						ChallengeService.newChallengeAdd(roleInfo, battleList, detail);
						newList.add(detail.getBattleNo()+"");
					}
				} else {
					// 有时间,次数的副本,防止玩家到了更新时间后,一直未重新登录而引起的不能刷新新副本
					ChallengeService.oldChallengeAdd(roleInfo, battleList, battle, detail,updateInfo);
					if(updateInfo.getId() > 0)
					{
						updateList.add(updateInfo);
					}
				}
			}
			
			if(updateList.size() > 0)
			{
				ChallengeBattleDAO.getInstance().updateChallengeAttackNumBatch(updateList);
			}
		}
		//加入用户缓存
		roleInfo.setBattleList(battleList);
		roleInfo.setNewBattleList(newList);
		return battleList;
	}
	
	/**
	 * 获取副本宝箱 - 1-普通 2-精英
	 * @param roleInfo
	 * @return
	 */
	public static Map<Integer, List<Integer>> getPrizeMap(RoleInfo roleInfo, byte chaperType) {
		// <chapterNo,List<prizeNo>>
		Map<Integer, List<Integer>> prizeMap = new HashMap<Integer, List<Integer>>();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null) {
			if(chaperType == 1){ //普通
				// 1-2,1-2,2-3,4-2
				String challengePrizeStr = roleLoadInfo.getChallengePrize();
				if (challengePrizeStr != null && challengePrizeStr.length() > 0) {
					String[] prizes = challengePrizeStr.split(",");
					if (prizes != null && prizes.length > 0) {
						List<Integer> list = null;
						for (String prize : prizes) {
							if (prize.length() > 0) {
								String[] prizeNos = prize.split("-");
								if (prizeNos != null && prizeNos.length > 0) {
									int chapterNo = Integer.parseInt(prizeNos[0]);
									int prizeNo = Integer.parseInt(prizeNos[1]);
									list = prizeMap.get(chapterNo);
									if (list == null) {
										list = new ArrayList<Integer>();
										list.add(prizeNo);
										prizeMap.put(chapterNo, list);
									}
									if (!list.contains(prizeNo)) {
										list.add(prizeNo);
									}
								}
							}
						}
					}
				}
			} else if(chaperType == 2){ //精英
				// 1-2,1-2,2-3,4-2
				String challengePrizeStr = roleLoadInfo.getChallengePrize2();
				if (challengePrizeStr != null && challengePrizeStr.length() > 0) {
					String[] prizes = challengePrizeStr.split(",");
					if (prizes != null && prizes.length > 0) {
						List<Integer> list = null;
						for (String prize : prizes) {
							if (prize.length() > 0) {
								String[] prizeNos = prize.split("-");
								if (prizeNos != null && prizeNos.length > 0) {
									int chapterNo = Integer.parseInt(prizeNos[0]);
									int prizeNo = Integer.parseInt(prizeNos[1]);
									list = prizeMap.get(chapterNo);
									if (list == null) {
										list = new ArrayList<Integer>();
										list.add(prizeNo);
										prizeMap.put(chapterNo, list);
									}
									if (!list.contains(prizeNo)) {
										list.add(prizeNo);
									}
								}
							}
						}
					}
				}
			}
			
		}
		return prizeMap;
	}

	/**
	 * 获取副本宝箱str
	 * @param prizeMap
	 * @return
	 */
	public static String getPrizeStr(Map<Integer, List<Integer>> prizeMap) {
		StringBuffer result = new StringBuffer();
		for (int chapterNo : prizeMap.keySet()) {
			List<Integer> list = prizeMap.get(chapterNo);
			for (int prizeNo : list) {
				if (result.length() > 0) {
					result.append(",");
				}
				result.append(chapterNo);
				result.append("-");
				result.append(prizeNo);
			}
		}
		return result.toString();
	}

	/**
	 * 获取ChapterDetailRe
	 * @param chapterNo
	 * @param prizeNos
	 * @return
	 */
	public static ChapterDetailRe getChapterDetailRe(byte chapterType, int chapterNo, List<Integer> prizeNos) {
		ChapterDetailRe re = new ChapterDetailRe();
		if (prizeNos != null && prizeNos.size() > 0) {
			re.setChapterType(chapterType);
			re.setChapterNo((short) chapterNo);
			StringBuilder resStr = new StringBuilder();
			for (int prizeNo : prizeNos) {
				if (resStr.length() > 0) {
					resStr.append(",");
				}
				resStr.append(prizeNo);
			}
			re.setReserve(resStr.toString());
		}
		return re;
	}
	
	/**
	 * 计算物品掉落
	 * @param battleDetail
	 * @param sweep
	 * @return
	 */
	public static List<DropInfo> checkDrop(BattleDetail battleDetail, byte sweep) {
		List<DropInfo> dropList = new ArrayList<DropInfo>();
		HashMap<Integer, BattleDetailPoint> pointMap = battleDetail.getPointsMap();
		for (BattleDetailPoint point : pointMap.values()) {
			String NPC = point.getGw();
			if (NPC != null && NPC.length() > 0) {
				dropList.addAll(checkDrop(NPC, sweep));
			}
		}
		return dropList;
	}

	/**
	 * 获取gw 掉落
	 * @param gwNo
	 * @param sweep
	 * @return
	 */
	public static List<DropInfo> checkDrop(String gwNo, byte sweep) {
		List<DropInfo> dropList = new ArrayList<DropInfo>();
		GWXMLInfo gwXmlInfo = GWXMLInfoMap.getNPCGWXMLInfo(gwNo);
		if (gwXmlInfo != null) {
			List<DropXMLInfo> list = null;
			for (String bag : gwXmlInfo.getDropMap().values()) {
				list = PropBagXMLMap.getPropBagXMLListbyStr(bag);
				if (list != null && list.size() > 0) {
					// 计算
					ItemService.getDropXMLInfo(null, list, dropList, sweep);
				}
			}
		}
		return dropList;
	}
	
}
