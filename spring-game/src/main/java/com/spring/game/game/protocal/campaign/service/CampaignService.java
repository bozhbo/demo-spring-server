package com.snail.webgame.game.protocal.campaign.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.FightCampaignInfoMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.FightType;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.HeroProType;
import com.snail.webgame.game.common.HeroPropertyInfo;
import com.snail.webgame.game.common.HeroRecord;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.core.SceneService;
import com.snail.webgame.game.dao.FightCampaignDAO;
import com.snail.webgame.game.info.FightCampaignBattle;
import com.snail.webgame.game.info.FightCampaignHero;
import com.snail.webgame.game.info.FightCampaignInfo;
import com.snail.webgame.game.info.HeroImageInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.info.RoleLoadInfo;
import com.snail.webgame.game.protocal.campaign.query.CampaignBattleRe;
import com.snail.webgame.game.protocal.campaign.query.CampaignDeployRe;
import com.snail.webgame.game.protocal.campaign.query.CampaignHeroRe;
import com.snail.webgame.game.protocal.fight.service.FightService;
import com.snail.webgame.game.protocal.hero.service.HeroRecordService;
import com.snail.webgame.game.protocal.rank.service.RankInfo;
import com.snail.webgame.game.protocal.rank.service.RankService;
import com.snail.webgame.game.xml.cache.CampaignXMLInfoMap;
import com.snail.webgame.game.xml.cache.RandomNameXMLMap;
import com.snail.webgame.game.xml.info.CampaignXMLBattle;
import com.snail.webgame.game.xml.info.CampaignXMLInfo;

public class CampaignService {

	private static Logger logger = LoggerFactory.getLogger("logs");

	/**
	 * 初始化关卡血量，战斗力
	 * @param roleInfo
	 */
	public static void loginInitCampaignProperty(RoleInfo roleInfo) {
		FightCampaignInfo info = FightCampaignInfoMap.getFightCampaignInfo(roleInfo.getId());
		if (info == null) {
			return;
		}
		Map<Integer, FightCampaignBattle> battleMap = info.getBattleMap();
		if (battleMap == null || battleMap.size() <= 0) {
			return;
		}
		for (FightCampaignBattle battle : battleMap.values()) {
			CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
			if (xmlInfo == null) {
				continue;
			}
			CampaignXMLBattle xmlBattle = xmlInfo.getBattles().get(battle.getBattleNo());
			if (xmlBattle == null) {
				continue;
			}
			float rate = xmlBattle.getAve();
			// 不是本人镜像
			if (battle.getDefendRoleId() != battle.getRoleId()) {
				rate = 1;
			}
			Map<Byte, HeroRecord> deployMap = battle.getFightDeployMap();
			if (deployMap != null) {
				for (HeroRecord record : deployMap.values()) {
					HeroRecordService.getHeroRecordFightValue(deployMap, record, rate);
				}
			}
		}
	}

	/**
	 * 初始化活动数据
	 * @param roleInfo
	 * @return
	 */
	public static FightCampaignInfo initFightCampaignInfo(RoleInfo roleInfo) {
		FightCampaignInfo info = new FightCampaignInfo();
		info.setRoleId(roleInfo.getId());
		info.setResetNum(0);
		info.setLastResetTime(null);
		info.setBuyNum(0);
		info.setBuyResetNum(0);
		info.setLastBuyTime(null);

		info.setLastFightBattleNo(0);
		info.setLastFightResult(0);
		info.setHeroMap(resetHeroMap(roleInfo, info));
		if (info.getHeroMap() == null) {
			if (logger.isErrorEnabled()) {
				logger.error("init FightCampaign HeroMap info errpr! roldId=" + roleInfo.getId());
			}
			return null;
		}
		info.setBattleMap(resetBattleMap(roleInfo));
		if (info.getBattleMap() == null) {
			if (logger.isErrorEnabled()) {
				logger.error("init FightCampaign battleMap info errpr! roldId=" + roleInfo.getId());
			}
			return null;
		}

		return info;
	}

	/**
	 * 重置活动
	 * @param roleInfo
	 * @param info
	 * @return
	 */
	public static int resetFightCampaignInfo(RoleInfo roleInfo, FightCampaignInfo info) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_1;
		}
		
		int reviceNum = 0;
		int resetNum = info.getCurrResetNum() + 1;
		Timestamp lastResetTime = new Timestamp(System.currentTimeMillis());
		int lastFightBattleNo = 0;
		int lastFightResult = 0;
		int hisFightBattleNo = info.getLastFightBattleNo();
		int hisFightResult = info.getLastFightResult();

		Map<Integer, FightCampaignBattle> newBattleMap = resetBattleMap(roleInfo);
		if (newBattleMap == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_4;
		}

		Map<Integer, FightCampaignHero> newHeroMap = resetHeroMap(roleInfo, info);
		if (newHeroMap == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_5;
		}

		// 武将变动
		Map<Integer, FightCampaignHero> insertOrUpdateHeros = new HashMap<Integer, FightCampaignHero>();
		Map<Integer, Integer> delHeroIds = new HashMap<Integer, Integer>();

		// 关卡变动
		Map<Integer, FightCampaignBattle> insertOrUpdateBattles = new HashMap<Integer, FightCampaignBattle>();
		Map<Integer, Integer> delBattleIds = new HashMap<Integer, Integer>();
		
		// 雇佣镜像删除 <heroId,id>
		Map<Integer, Integer> delImageIds = new HashMap<Integer, Integer>();

		// 检测出武将变动
		getFightCampaignHeroUpdateForReset(info, newHeroMap, insertOrUpdateHeros, delHeroIds);

		// 检测出关卡变动
		getFightCampaignBattleUpdate(info, newBattleMap, insertOrUpdateBattles, delBattleIds);
		
		// 检测镜像变动
		getFightCampaignImageDel(roleLoadInfo, delImageIds);

		// 更新变动
		if (FightCampaignDAO.getInstance().updateFightCampaignResetNum(info.getId(), reviceNum, resetNum,
				lastResetTime, lastFightBattleNo, lastFightResult, hisFightBattleNo, hisFightResult,
				insertOrUpdateHeros, delHeroIds, insertOrUpdateBattles, delBattleIds, delImageIds)) {
			info.setReviceNum(reviceNum);
			info.setResetNum(resetNum);
			info.setLastResetTime(lastResetTime);
			info.setLastFightBattleNo(lastFightBattleNo);
			info.setLastFightResult(lastFightResult);
			info.setHisFightBattleNo(hisFightBattleNo);
			info.setHisFightResult(hisFightResult);

			info.setHeroMap(newHeroMap);
			info.setBattleMap(newBattleMap);
			
			for (int heroId : delImageIds.keySet()) {
				roleLoadInfo.removeHeroImageInfo(FightType.FIGHT_TYPE_6, heroId);
			}

		} else {
			return ErrorCode.CAMPAIGN_RESET_ERROR_6;
		}
		return 1;
	}

	/**
	 * gm 重置活动
	 * @param roleInfo
	 * @param info
	 * @return
	 */
	public static int resetFightCampaignInfobyGm(RoleInfo roleInfo, FightCampaignInfo info) {
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_1;
		}
		
		int reviceNum = 0;
		int lastFightBattleNo = 0;
		int lastFightResult =  0;

		int hisFightBattleNo = info.getLastFightBattleNo();
		int hisFightResult = info.getLastFightResult();
		Map<Integer, FightCampaignBattle> newBattleMap = resetBattleMap(roleInfo);
		if (newBattleMap == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_4;
		}

		Map<Integer, FightCampaignHero> newHeroMap = resetHeroMap(roleInfo, info);
		if (newHeroMap == null) {
			return ErrorCode.CAMPAIGN_RESET_ERROR_5;
		}

		// 武将变动
		Map<Integer, FightCampaignHero> insertOrUpdateHeros = new HashMap<Integer, FightCampaignHero>();
		Map<Integer, Integer> delHeroIds = new HashMap<Integer, Integer>();

		// 关卡变动
		Map<Integer, FightCampaignBattle> insertOrUpdateBattles = new HashMap<Integer, FightCampaignBattle>();
		Map<Integer, Integer> delBattleIds = new HashMap<Integer, Integer>();
		
		// 雇佣镜像删除 <heroId,id>
		Map<Integer, Integer> delImageIds = new HashMap<Integer, Integer>();

		// 检测出武将变动
		getFightCampaignHeroUpdateForReset(info, newHeroMap, insertOrUpdateHeros, delHeroIds);

		// 检测出关卡变动
		getFightCampaignBattleUpdate(info, newBattleMap, insertOrUpdateBattles, delBattleIds);
		
		// 检测镜像变动
		getFightCampaignImageDel(roleLoadInfo, delImageIds);

		// 更新变动
		if (FightCampaignDAO.getInstance().updateFightCampaignResetNumbyGm(info.getId(), reviceNum, lastFightBattleNo,
				lastFightResult, hisFightBattleNo, hisFightResult, insertOrUpdateHeros, delHeroIds,
				insertOrUpdateBattles, delBattleIds, delImageIds)) {
			info.setReviceNum(reviceNum);
			info.setLastFightBattleNo(lastFightBattleNo);
			info.setLastFightResult(lastFightResult);
			info.setHisFightBattleNo(hisFightBattleNo);
			info.setHisFightResult(hisFightResult);

			info.setHeroMap(newHeroMap);
			info.setBattleMap(newBattleMap);
			
			for (int heroId : delImageIds.keySet()) {
				roleLoadInfo.removeHeroImageInfo(FightType.FIGHT_TYPE_6, heroId);
			}

			SceneService.sendRoleRefreshMsg(roleInfo.getId(), SceneService.REFRESH_TYPE_CAMPAIGN, "");
		} else {
			return ErrorCode.CAMPAIGN_RESET_ERROR_6;
		}
		return 1;
	}

	/**
	 * 重置关卡镜像信息和战死残血和上阵的英雄信息<heroId , info>
	 * @param roleInfo
	 * @param info
	 * @return
	 */
	public static Map<Integer, FightCampaignHero> resetHeroMap(RoleInfo roleInfo, FightCampaignInfo info) {
		Map<Integer, FightCampaignHero> heroMap = new HashMap<Integer, FightCampaignHero>();
		List<HeroInfo> list = HeroInfoMap.getFightDeployHero(roleInfo.getId());
		if (list != null) {
			FightCampaignHero cahero = null;
			for (HeroInfo heroInfo : list) {
				cahero = new FightCampaignHero();
				cahero.setRoleId(heroInfo.getRoleId());
				cahero.setHeroId(heroInfo.getId());
				cahero.setDeployPos(heroInfo.getDeployStatus());
				cahero.setHeroStatus(FightCampaignHero.HERO_STATUS_1);
				cahero.setCutHp(0);
				heroMap.put(cahero.getHeroId(), cahero);
			}
		}
		return heroMap;
	}

	/**
	 * 重置关卡镜像信息
	 * @param roleInfo
	 * @return
	 */
	public static Map<Integer, FightCampaignBattle> resetBattleMap(RoleInfo roleInfo) {
		Map<Integer, FightCampaignBattle> result = new HashMap<Integer, FightCampaignBattle>();
		CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (xmlInfo == null) {
			return null;
		}
		List<RankInfo> sortFightValuelist = new ArrayList<RankInfo>();
		sortFightValuelist.addAll(RankService.getFightValueRank());
		if (sortFightValuelist.size() <= 0) {
			return null;
		}

		FightCampaignBattle battle = null;
		for (CampaignXMLBattle battleXml : xmlInfo.getBattles().values()) {
			battle = new FightCampaignBattle();
			battle.setRoleId(roleInfo.getId());
			battle.setBattleNo(battleXml.getNo());
			battle.setIsGetPrize(FightCampaignBattle.GET_PRIZE_STATUS_0);

			// 随机生成关卡战斗信息
			getFightCampaignRecord(roleInfo, battleXml, battle, sortFightValuelist);
			result.put(battle.getBattleNo(), battle);
		}
		return result;
	}

	/**
	 * 随机生成关卡战斗信息
	 * @param roleInfo
	 * @param battle
	 * @return
	 */
	private static void getFightCampaignRecord(RoleInfo roleInfo, CampaignXMLBattle battle,
			FightCampaignBattle caBattle, List<RankInfo> sortFightValuelist) {
		double minFightValue = roleInfo.getFightMaxValue() * (1 + battle.getMin() / 100.0);
		double maxFightValue = roleInfo.getFightMaxValue() * (1 + battle.getMax() / 100.0);

		int defendRoleId = roleInfo.getId();
		// 根据战斗力范围获取值,没有时向下取
		int index = getDefendRoleIndexbyRandom(minFightValue, maxFightValue, sortFightValuelist);
		if (index != -1) {
			RankInfo resut = sortFightValuelist.get(index);
			if (resut != null && RoleInfoMap.getRoleInfo(resut.getRoleId()) != null) {
				defendRoleId = resut.getRoleId();
				// 移除选中的，防止重复
				sortFightValuelist.remove(index);
			}
		}

		Map<Byte, HeroRecord> deploy = new HashMap<Byte, HeroRecord>();
		HeroInfo heroInfo = null;
		HeroRecord record = null;
		int mainHeroSex = 0;// 主武将sex
		for (byte pos = 1; pos <= GameValue.FIGHT_ARMY_LIMIT; pos++) {
			heroInfo = HeroInfoMap.getFightDeployHero(defendRoleId, pos);
			if (heroInfo != null) {
				if (pos == HeroInfo.DEPLOY_TYPE_MAIN) {
					HeroXMLInfo heroXMLInfo = HeroXMLInfoMap.getHeroXMLInfo(heroInfo.getHeroNo());
					if (heroXMLInfo != null) {
						mainHeroSex = heroXMLInfo.getSex();
					}
				}

				// 根据武将生成镜像
				record = HeroRecordService.getDeployHeroRecord(heroInfo, pos);
				if (record != null) {
					record.setId(defendRoleId * 100 + pos);// 唯一Id
					deploy.put(record.getDeployStatus(), record);
				}
			}
		}
		caBattle.setFightDeployMap(deploy);
		RoleInfo defendRole = RoleInfoMap.getRoleInfo(defendRoleId);
		if (defendRole != null && defendRole.getId() != roleInfo.getId()) {
			caBattle.setDefendRoleId(defendRole.getId());
			caBattle.setDefendRoleName(defendRole.getRoleName());
			caBattle.setFightValue(defendRole.getFightValue());
		} else {
			caBattle.setDefendRoleId(roleInfo.getId());
			if (mainHeroSex == HeroXMLInfo.SEX_MALE) {
				caBattle.setDefendRoleName(RandomNameXMLMap.randomMaleName());
			} else {
				caBattle.setDefendRoleName(RandomNameXMLMap.randomFemaleName());
			}
			int fightValue = RandomUtil.getRandom((int) minFightValue, (int) maxFightValue);
			caBattle.setFightValue(fightValue);
		}
	}

	/**
	 * 根据战斗力范围获取值,没有时向下取
	 * @param minFightValue
	 * @param maxFightValue
	 * @return
	 */
	private static int getDefendRoleIndexbyRandom(double minFightValue, double maxFightValue,
			List<RankInfo> sortFightValuelist) {
		if(sortFightValuelist == null || sortFightValuelist.size() <= 0){
			return -1;
		}
		
		double min = Math.min(minFightValue, maxFightValue);
		double max = Math.max(minFightValue, maxFightValue);

		double maxRoleFightValue = sortFightValuelist.get(0).getFightValue();
		double minRoleFightValue = sortFightValuelist.get(sortFightValuelist.size() - 1).getFightValue();

		if (minRoleFightValue > max) {
			return sortFightValuelist.size() - 1;
		}
		if (maxRoleFightValue < min) {
			return 0;
		}
		if (min < minRoleFightValue) {
			min = minRoleFightValue;
		}
		if (max > maxRoleFightValue) {
			max = maxRoleFightValue;
		}
		int minIndex = RankService.getRoleIndexbyShellSort(sortFightValuelist, max);
		int maxIndex = RankService.getRoleIndexbyShellSort(sortFightValuelist, min);
		if (minIndex != -1 && maxIndex != -1) {
			return RandomUtil.getRandom(minIndex, maxIndex);
		}	
		return -1;
	}

	/**
	 * 获取非上阵的战斗力最大的英雄
	 * @param roleId
	 * @param exitHeroIds
	 * @return
	 */
	public static HeroInfo getMaxFightValueHero(int roleId, List<Integer> exitHeroIds) {
		Map<Integer, HeroInfo> heroMap = HeroInfoMap.getHeroByRoleId(roleId);
		int heroId = 0;
		int fightValue = 0;
		if (heroMap != null) {
			for (HeroInfo heroInfo : heroMap.values()) {
				if (heroInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_COMM && !exitHeroIds.contains(heroInfo.getId())) {
					if (heroInfo.getFightValue() > fightValue) {
						fightValue = heroInfo.getFightValue();
						heroId = heroInfo.getId();
					}
				}
			}
		}
		if (heroId != 0) {
			return heroMap.get(heroId);
		}
		return null;
	}

	/**
	 * 开始战斗阵形调整
	 * @param info 老阵形信息
	 * @param changes 调整后的武将阵形信息
	 * @param insertOrUpdate 添加修改的武将
	 * @param delIds 删除的武将
	 * @param isReset
	 */
	public static void getFightCampaignHeroUpdate(FightCampaignInfo info, Map<Integer, FightCampaignHero> changes,
			Map<Integer, FightCampaignHero> insertOrUpdate, Map<Integer, Integer> delIds) {
		if (insertOrUpdate == null) {
			logger.info("param insertOrUpdate is null");
			return;
		}
		Map<Integer, FightCampaignHero> olds = info.getHeroMap();
		if (olds != null) {
			FightCampaignHero change = null;
			for (FightCampaignHero hero : olds.values()) {
				change = changes.get(hero.getHeroId());
				if (hero.getDeployPos() != HeroInfo.DEPLOY_TYPE_COMM && change == null) {
					// 原来上阵的下阵
					change = (FightCampaignHero) hero.clone();
					change.setDeployPos(HeroInfo.DEPLOY_TYPE_COMM);
					// 新加的变动
					insertOrUpdate.put(change.getHeroId(), change);
				}
			}
			FightCampaignHero old = null;
			HeroInfo heroInfo = null;
			for (FightCampaignHero newCH : changes.values()) {
				old = olds.get(newCH.getHeroId());
				if (old == null) {
					// 新上阵的英雄
					heroInfo = HeroInfoMap.getHeroInfo(newCH.getRoleId(), newCH.getHeroId());
					if (heroInfo == null) {
						continue;
					}
					// 新加的变动
					insertOrUpdate.put(newCH.getHeroId(), newCH);
				} else {
					if (old.getDeployPos() == newCH.getDeployPos()) {
						// 位置不变

					} else {
						insertOrUpdate.put(newCH.getHeroId(), newCH);
					}
				}
			}
		} else {
			insertOrUpdate.putAll(changes);
		}
	}

	/**
	 * 开始战斗阵形调整
	 * @param info
	 * @param changes
	 * @param insertOrUpdate
	 * @param delIds
	 */
	public static void getFightCampaignHeroImageUpdate(RoleLoadInfo roleLoadInfo, Map<Integer, HeroImageInfo> changes,
			Map<Integer, HeroImageInfo> insertOrUpdate) {
		if (insertOrUpdate == null) {
			logger.info("param insertOrUpdate is null");
			return;
		}
		Map<Integer, HeroImageInfo> olds = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
		if (olds != null) {
			HeroImageInfo change = null;
			for (HeroImageInfo hero : olds.values()) {
				change = changes.get(hero.getHeroId());
				if (hero.getDeployStatus() != HeroInfo.DEPLOY_TYPE_COMM && change == null) {
					// 原来上阵的下阵
					change = (HeroImageInfo) hero.clone();
					change.setDeployStatus(HeroInfo.DEPLOY_TYPE_COMM);
					// 新加的变动
					insertOrUpdate.put(change.getHeroId(), change);
				}
			}
			HeroImageInfo old = null;
			for (HeroImageInfo newCH : changes.values()) {
				old = olds.get(newCH.getHeroId());
				if (old == null) {
					// 新加的变动
					insertOrUpdate.put(newCH.getHeroId(), newCH);
				} else {
					if (old.getDeployStatus() == newCH.getDeployStatus()) {
						// 位置不变

					} else {
						insertOrUpdate.put(newCH.getHeroId(), newCH);
					}
				}
			}
		} else {
			insertOrUpdate.putAll(changes);
		}
	}
	
	/**
	 * 重置阵形调整
	 * @param info 老阵形信息
	 * @param changes 调整后的武将阵形信息
	 * @param insertOrUpdate 添加修改的武将
	 * @param delIds 删除的武将
	 */
	public static void getFightCampaignHeroUpdateForReset(FightCampaignInfo info,
			Map<Integer, FightCampaignHero> changes, Map<Integer, FightCampaignHero> insertOrUpdate,
			Map<Integer, Integer> delIds) {
		if (insertOrUpdate == null) {
			logger.info("param insertOrUpdate is null");
			return;
		}
		if (delIds == null) {
			logger.info("param delIds is null");
			return;
		}
		Map<Integer, FightCampaignHero> olds = info.getHeroMap();
		if (olds != null) {
			for (FightCampaignHero hero : olds.values()) {
				// 重置时 删除老的
				if (!delIds.containsKey(hero.getHeroId())) {
					delIds.put(hero.getHeroId(), hero.getId());
				}
			}
		}

		for (FightCampaignHero hero : changes.values()) {
			// 新加的变动
			insertOrUpdate.put(hero.getHeroId(), hero);
		}
	}

	/**
	 * 关卡阵形变动
	 * @param info 当前的战斗对手信息
	 * @param changes 调整后的战斗对手信息
	 * @param insertOrUpdate 新增修改的武将信息
	 * @param delIds 删除的武将信息
	 */
	private static void getFightCampaignBattleUpdate(FightCampaignInfo info, Map<Integer, FightCampaignBattle> changes,
			Map<Integer, FightCampaignBattle> insertOrUpdate, Map<Integer, Integer> delIds) {
		if (insertOrUpdate == null) {
			logger.info("param insertOrUpdate is null");
			return;
		}
		if (delIds == null) {
			logger.info("param delIds is null");
			return;
		}
		Map<Integer, FightCampaignBattle> olds = info.getBattleMap();
		FightCampaignBattle change = null;
		if (olds != null) {
			for (FightCampaignBattle battle : olds.values()) {
				change = changes.get(battle.getBattleNo());
				if (change == null) {
					delIds.put(battle.getBattleNo(), battle.getId());
				} else {
					change.setId(battle.getId());
					insertOrUpdate.put(battle.getBattleNo(), change);
				}
			}
		}

		for (FightCampaignBattle battle : changes.values()) {
			change = olds.get(battle.getBattleNo());
			if (change == null) {
				insertOrUpdate.put(battle.getBattleNo(), battle);
			}
		}
	}
	
	/**
	 * 获取镜像数据
	 * @param roleLoadInfo
	 * @param delImageIds
	 */
	private static void getFightCampaignImageDel(RoleLoadInfo roleLoadInfo, Map<Integer, Integer> delImageIds) {
		if (delImageIds == null) {
			logger.info("param insertOrUpdate is null");
			return;
		}
		Map<Integer, HeroImageInfo> olds = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
		if (olds != null) {
			for (HeroImageInfo battle : olds.values()) {
				delImageIds.put(battle.getHeroId(),battle.getId());
			}
		}
	}

	/**
	 * 获取我方宝物活动武将信息
	 * @param info
	 * @param heroIdStr
	 * @return
	 */
	public static List<CampaignHeroRe> getCampaignHeros(RoleInfo roleInfo,FightCampaignInfo info, String heroIdStr) {
		List<CampaignHeroRe> list = new ArrayList<CampaignHeroRe>();
		Map<Integer, FightCampaignHero> heroMap = info.getHeroMap();
		Map<Byte, HeroRecord> recordMap = getAttackHeroRecordMap(roleInfo, info);

		if (heroIdStr != null && heroIdStr.length() > 0) {
			String[] heroIds = heroIdStr.split(",");
			FightCampaignHero hero = null;
			CampaignHeroRe re = null;
			for (String heroId : heroIds) {
				int id = NumberUtils.toInt(heroId);
				hero = heroMap.get(id);
				if (hero != null) {
					re = getCampaignHeroRe(recordMap, hero, true);
					if (re != null) {
						list.add(re);
					}
				} else {
					RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
					if (roleLoadInfo != null) {
						HeroImageInfo heroImage = roleLoadInfo.getHeroImageInfo(FightType.FIGHT_TYPE_6, id);
						if(heroImage != null){
							re = getCampaignHeroRe(recordMap, heroImage);
							if (re != null) {
								list.add(re);
							}				
						}
					}
				}
			}
		} else {
			CampaignHeroRe re = null;

			for (FightCampaignHero hero : heroMap.values()) {
				re = getCampaignHeroRe(recordMap, hero, false);
				if (re != null) {
					list.add(re);
				}
			}
			RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
			if (roleLoadInfo != null) {
				Map<Integer, HeroImageInfo> val = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
				if(val!=null){
					for (HeroImageInfo hero : val.values()) {
						re = getCampaignHeroRe(recordMap, hero);
						if (re != null) {
							list.add(re);
						}				
					}
				}
			}
		}
		return list;
	}

	private static CampaignHeroRe getCampaignHeroRe(Map<Byte, HeroRecord> recordMap, FightCampaignHero hero, boolean change) {
		if (!change && hero.getDeployPos() == HeroInfo.DEPLOY_TYPE_COMM
				&& hero.getHeroStatus() == FightCampaignHero.HERO_STATUS_1 && hero.getCutHp() == 0) {
			return null;
		}

		CampaignHeroRe re = new CampaignHeroRe();
		re.setHeroId((int) hero.getHeroId());
		re.setDeployPos(hero.getDeployPos());
		re.setHeroStatus(hero.getHeroStatus());
		re.setCutHp(hero.getCutHp());
		HeroPropertyInfo pro = getFightCampaignHeroPro(recordMap, hero);
		if(pro != null){
			re.setTotalHp(pro.getHp());
		}
		return re;
	}
	
	private static CampaignHeroRe getCampaignHeroRe(Map<Byte, HeroRecord> recordMap, HeroImageInfo hero) {
		if (hero.getHireType() == FightType.FIGHT_TYPE_6.getValue()) {
			CampaignHeroRe re = new CampaignHeroRe();
			re.setHeroId((int) hero.getHeroId());
			re.setDeployPos((byte)hero.getDeployStatus());
			re.setHeroStatus((byte)hero.getHeroStatus());
			re.setCutHp(hero.getCutHp());
			HeroPropertyInfo pro = getHeroImageInfoPro(recordMap, hero);
			if(pro != null){
				re.setTotalHp(pro.getHp());
			}
			return re;
		}
		return null;
	}

	public static List<CampaignBattleRe> getCampaignBattles(FightCampaignInfo info, String battleNoStr) {
		List<CampaignBattleRe> list = new ArrayList<CampaignBattleRe>();
		Map<Integer, FightCampaignBattle> battleMap = info.getBattleMap();
		RoleInfo roleInfo = RoleInfoMap.getRoleInfo(info.getRoleId());
		
		if (battleNoStr != null && battleNoStr.length() > 0) {
			String[] battleNos = battleNoStr.split(",");
			FightCampaignBattle battle = null;
			for (String battleNo : battleNos) {
				int no = NumberUtils.toInt(battleNo);
				battle = battleMap.get(no);
				if (battle != null) {
					list.add(getCampaignBattleRe(roleInfo, battle));
				}
			}
		} else {
			for (FightCampaignBattle battle : battleMap.values()) {
				list.add(getCampaignBattleRe(roleInfo, battle));
			}
		}

		return list;
	}

	public static CampaignBattleRe getCampaignBattleRe(RoleInfo roleInfo, FightCampaignBattle battle) {
		CampaignXMLInfo xmlInfo = CampaignXMLInfoMap.getCampaignXMLInfo(CampaignXMLInfo.CAMPAIGN_TYPE_1);
		if (xmlInfo == null) {
			return null;
		}
		CampaignXMLBattle xmlBattle = xmlInfo.getBattles().get(battle.getBattleNo());
		if (xmlBattle == null) {
			return null;
		}
		float rate = xmlBattle.getAve();
		// 不是本人镜像
		if (battle.getDefendRoleId() != battle.getRoleId()) {
			rate = 1;
		}

		CampaignBattleRe re = new CampaignBattleRe();
		re.setBattleNo(battle.getBattleNo());
		re.setIsGetPrize(battle.getIsGetPrize());
		re.setDefendRoleName(battle.getDefendRoleName());

		Map<Byte, HeroRecord> deployMap = battle.getFightDeployMap();
		if (deployMap != null) {
			for (HeroRecord record : deployMap.values()) {
				CampaignDeployRe deployRe = new CampaignDeployRe();
				deployRe.setHeroNo(record.getHeroNo());
				deployRe.setDeployPos(record.getDeployStatus());
				deployRe.setHeroLevel(record.getHeroLevel());
				deployRe.setStar(record.getStar());
				deployRe.setQuality(record.getQuality());

				if (record.getTotalHp() == 0 || record.getFightValue() == 0) {
					HeroRecordService.getHeroRecordFightValue(deployMap, record, rate);
				}
				deployRe.setHp(record.getTotalHp());
				deployRe.setFightValue(record.getFightValue());

				if (battle.getDefendRoleId() == battle.getRoleId() && battle.getFightValue() != 0) {
					// 是本人镜像
					deployRe.setFightValue((int) (battle.getFightValue() / deployMap.size()));
				}

				int currHp = 0;
				if (record.getHeroStatus() == 1) {
					// 0-战死 1-残血
					currHp = deployRe.getHp() - record.getCutHp();
					if (currHp < 0) {
						currHp = 0;
					}
				}
				deployRe.setCurrHp(currHp);
				re.getHeros().add(deployRe);
			}
		}
		re.setSize(re.getHeros().size());
		return re;
	}

	/**
	 * 获取战斗攻击方镜像
	 * @param roleInfo
	 * @param info
	 * @return
	 */
	public static Map<Byte, HeroRecord> getAttackHeroRecordMap(RoleInfo roleInfo, FightCampaignInfo info) {
		Map<Integer, FightCampaignHero> heroMap = info.getHeroMap();
		if (heroMap == null) {
			return null;
		}
		HeroInfo heroInfo = null;
		List<Integer> jbHeroNoList = new ArrayList<Integer>();
		for (FightCampaignHero hero : heroMap.values()) {
			heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), hero.getHeroId());
			if (heroInfo == null) {
				return null;
			}
			if (hero.getHeroStatus() != 0 && hero.getDeployPos() > GameValue.FIGHT_ARMY_LIMIT) {
				jbHeroNoList.add(heroInfo.getHeroNo());
			}
		}
		HeroRecord heroRecord = null;
		Map<Byte, HeroRecord> recordMap = new HashMap<Byte, HeroRecord>();
		RoleLoadInfo roleLoadInfo = roleInfo.getRoleLoadInfo();
		if (roleLoadInfo != null) {
			Map<Integer, HeroImageInfo> val = roleLoadInfo.getHeroImageMapbyFightType(FightType.FIGHT_TYPE_6);
			if (val != null) {
				for (HeroImageInfo imageInfo : val.values()) {
					if (imageInfo.getHeroStatus() != 0 && imageInfo.getDeployStatus() > GameValue.FIGHT_ARMY_LIMIT) {
						jbHeroNoList.add(heroInfo.getHeroNo());
					} else {
						heroRecord = HeroRecordService.getHeroRecord(imageInfo, (byte) imageInfo.getDeployStatus());
						if (heroRecord == null) {
							continue;
						}
						if (imageInfo.getDeployStatus() == HeroInfo.DEPLOY_TYPE_MAIN) {
							heroRecord.setJbHeroNoList(jbHeroNoList);
						}
						recordMap.put((byte) imageInfo.getDeployStatus(), heroRecord);
					}
				}
			}
		}

		for (FightCampaignHero hero : heroMap.values()) {
			if (hero.getDeployPos() != HeroInfo.DEPLOY_TYPE_COMM) {
				heroInfo = HeroInfoMap.getHeroInfo(roleInfo.getId(), hero.getHeroId());
				if (heroInfo == null) {
					return null;
				}
				heroRecord = HeroRecordService.getDeployHeroRecord(heroInfo, hero.getDeployPos());
				if (heroRecord == null) {
					continue;
				}
				heroRecord.setCutHp(hero.getCutHp());
				heroRecord.setHeroStatus(hero.getHeroStatus());
				if (hero.getDeployPos() == HeroInfo.DEPLOY_TYPE_MAIN) {
					heroRecord.setJbHeroNoList(jbHeroNoList);
				}
				recordMap.put(hero.getDeployPos(), heroRecord);
			}
		}
		return recordMap;
	}

	private static HeroPropertyInfo getHeroImageInfoPro(Map<Byte, HeroRecord> recordMap, HeroImageInfo imageInfo) {
		if(recordMap == null || imageInfo == null){
			return null;
		}	
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(FightType.FIGHT_TYPE_6);
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(FightType.FIGHT_TYPE_6);
		HeroRecord record = null;
		if (imageInfo.getDeployStatus() > HeroInfo.DEPLOY_TYPE_COMM
				&& imageInfo.getDeployStatus() <= GameValue.FIGHT_ARMY_LIMIT) {
			record = recordMap.get(imageInfo.getDeployStatus());
		} else {
			record = HeroRecordService.getHeroRecord(imageInfo, imageInfo.getDeployStatus());
		}
		if (record != null) {
			return HeroRecordService.recalHeroRecordTotalPro(recordMap, record, mainRate, otherRate);
		}
		return null;
	}

	private static HeroPropertyInfo getFightCampaignHeroPro(Map<Byte, HeroRecord> recordMap, FightCampaignHero hero) {
		if(recordMap == null || hero == null){
			return null;
		}	
		Map<HeroProType, Double> mainRate = FightService.getMainHeroRate(FightType.FIGHT_TYPE_6);
		Map<HeroProType, Double> otherRate = FightService.getOtherHeroRate(FightType.FIGHT_TYPE_6);
		HeroRecord record = null;
		if (hero.getDeployPos() > HeroInfo.DEPLOY_TYPE_COMM && hero.getDeployPos() <= GameValue.FIGHT_ARMY_LIMIT) {
			record = recordMap.get(hero.getDeployPos());
		} else {
			HeroInfo heroInfo = HeroInfoMap.getHeroInfo(hero.getRoleId(), hero.getHeroId());
			if (heroInfo != null) {
				record = HeroRecordService.getHeroRecord(heroInfo, hero.getDeployPos());
			}
		}
		if (record != null) {
			return HeroRecordService.recalHeroRecordTotalPro(recordMap, record, mainRate, otherRate);
		}
		return null;
	}
}
