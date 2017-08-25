package com.snail.webgame.game.protocal.snatch.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snail.webgame.game.cache.BagItemMap;
import com.snail.webgame.game.cache.HeroInfoMap;
import com.snail.webgame.game.cache.RoleInfoMap;
import com.snail.webgame.game.cache.SnatchPatchMap;
import com.snail.webgame.game.common.ActionType;
import com.snail.webgame.game.common.ErrorCode;
import com.snail.webgame.game.common.GameValue;
import com.snail.webgame.game.common.fightdata.DropInfo;
import com.snail.webgame.game.common.util.RandomUtil;
import com.snail.webgame.game.common.xml.cache.HeroXMLInfoMap;
import com.snail.webgame.game.common.xml.info.HeroXMLInfo;
import com.snail.webgame.game.condtion.AbstractConditionCheck;
import com.snail.webgame.game.condtion.conds.CoinCond;
import com.snail.webgame.game.dao.RoleDAO;
import com.snail.webgame.game.info.BagItemInfo;
import com.snail.webgame.game.info.HeroInfo;
import com.snail.webgame.game.info.RoleInfo;
import com.snail.webgame.game.protocal.item.service.ItemService;
import com.snail.webgame.game.protocal.rolemgt.service.RoleService;
import com.snail.webgame.game.protocal.snatch.getRivalList.RivalListRe;
import com.snail.webgame.game.xml.cache.RandomNameXMLMap;
import com.snail.webgame.game.xml.cache.SnatchMap;
import com.snail.webgame.game.xml.info.SnatchInfo;

public class SnatchService {
	private static RoleDAO roleDAO = RoleDAO.getInstance();

	/**
	 * 开启安全模式
	 * @param roleInfo
	 * @return
	 */
	public static int openSafeMode(RoleInfo roleInfo) {
		long safeModeLeftTimeMillis = roleInfo.getSafeModeEndTime() - System.currentTimeMillis();
		if (safeModeLeftTimeMillis > 0) {
			return ErrorCode.SNATCH_SAFEMODE_1;
		}
		// 校验金子
		List<AbstractConditionCheck> conds = new ArrayList<AbstractConditionCheck>();
		conds.add(new CoinCond(GameValue.SAFEMODE_COST_GOLD));
		int check = AbstractConditionCheck.checkCondition(roleInfo, conds);
		if (check != 1) {
			return check;
		}
		// 扣除金子
		if (!RoleService.subRoleResource(ActionType.action361.getType(), roleInfo, conds , null)) {
			return ErrorCode.SNATCH_SAFEMODE_2;
		}
		// 修改安全时间
		long safeModeEndTime = System.currentTimeMillis() + GameValue.SAFEMODE_OPEN_TIME;
		if (!roleDAO.updateSafeModeEndTime(roleInfo.getId(), safeModeEndTime)) {
			return ErrorCode.SNATCH_SAFEMODE_3;
		}
		roleInfo.setSafeModeEndTime(safeModeEndTime);
		return 1;
	}

	/**
	 * 关闭安全模式
	 * @param roleInfo
	 * @return
	 */
	public static int closeSafeMode(RoleInfo roleInfo) {
		long safeModeLeftTimeMillis = roleInfo.getSafeModeEndTime() - System.currentTimeMillis();
		if (safeModeLeftTimeMillis <= 0) {
			return ErrorCode.SNATCH_SAFEMODE_4;
		}
		if (!roleDAO.updateSafeModeEndTime(roleInfo.getId(), 0)) {
			return ErrorCode.SNATCH_SAFEMODE_5;
		}
		roleInfo.setSafeModeEndTime(0);
		return 1;
	}

	/**
	 * 检测是否可以抢夺宝石
	 * @param roleInfo
	 * @param stoneNo
	 * @return
	 */
	public static int checkCanlootStone(RoleInfo roleInfo, SnatchInfo snatchInfo) {
		// 不是该神兵类型的第一个就必须有该类碎片的其中一种才能抢夺
		if (snatchInfo.getNo() != 1) {
			List<SnatchInfo> patchList = SnatchMap.getByPropNo(snatchInfo.getPropNo());
			if (patchList == null) {
				return ErrorCode.SNATCH_LOOT_10;
			}
			boolean patchExist = false;
			for (SnatchInfo info : patchList) {
				if (BagItemMap.checkBagItemNum(roleInfo, info.getPatchNo(), 1)) {
					patchExist = true;
					break;
				}
			}
			if (!patchExist) {
				return ErrorCode.SNATCH_LOOT_10;
			}
		}
		return 1;
	}

	/**
	 * 获取玩家挑战的人员列表
	 * @param role
	 * @param stoneNo
	 * @param maxSize
	 * @return
	 */
	public static List<RivalListRe> getRoleRivalList(RoleInfo role, int stoneNo, int maxSize) {
		List<RivalListRe> rivalList = new ArrayList<RivalListRe>();
		if (maxSize <= 0) {
			return rivalList;
		}
		int mainHeroLv = HeroInfoMap.getMainHeroLv(role.getId());
		int minLevel = (int) (mainHeroLv - mainHeroLv * 0.2);
		int maxLevel = (int) (mainHeroLv + mainHeroLv * 0.2);
		// 随机取值100 条
		List<Integer> roleIds = SnatchPatchMap.getRadomRivalList(stoneNo, 100);
		if (roleIds != null && roleIds.size() > 0) {
			synchronized (roleIds) {
				RoleInfo roleInfo = null;
				HeroInfo mainHero = null;
				RivalListRe re = null;
				for(int roleId : roleIds){
					roleInfo = RoleInfoMap.getRoleInfo(roleId);
					if (roleInfo != null && !roleInfo.getAccount().equalsIgnoreCase(role.getAccount())
							&& roleInfo.getSafeModeEndTime() < System.currentTimeMillis()) {
						mainHero = HeroInfoMap.getMainHeroInfo(roleInfo);
						if (mainHero != null && mainHero.getHeroLevel() >= minLevel
								&& mainHero.getHeroLevel() <= maxLevel) {
							
							// 剩余一个数量的不显示给玩家
							BagItemInfo itemInfo = BagItemMap.getBagItembyNo(roleInfo, stoneNo);
							if(itemInfo == null || itemInfo.getNum() <= 1)
							{
								continue;
							}
							
							re = new RivalListRe();
							re.setRoleName(roleInfo.getRoleName());
							re.setRoleLevel(mainHero.getHeroLevel());
							re.setHeroNo(mainHero.getHeroNo());
							re.setIsNPC((byte) 0);
							re.setProbabilitybyRate(calcSnatchRate(role.getFightValue(), roleInfo.getFightValue()));
							rivalList.add(re);
							if (rivalList.size() >= maxSize) {
								break;
							}
						}
					}
				}
			}
			roleIds.clear();
		}
		return rivalList;
	}

	/**
	 * 随机生成机器人
	 * @param roleInfo
	 * @param snatchInfo
	 * @param size
	 * @return
	 */
	public static List<RivalListRe> getRivalListbyRandom(RoleInfo roleInfo, SnatchInfo snatchInfo, int size) {
		List<RivalListRe> rivalList = new ArrayList<RivalListRe>();
		if (size <= 0) {
			return rivalList;
		}
		List<Integer> mainHeros = new ArrayList<Integer>();
		HashMap<Integer, HeroXMLInfo> heroMap = HeroXMLInfoMap.getHeroMap();
		for (HeroXMLInfo heroXMLInfo : heroMap.values()) {
			if (heroXMLInfo.getInitial() == 1) {
				mainHeros.add(heroXMLInfo.getNo());
			}
		}
		int mainHeroLv = HeroInfoMap.getMainHeroLv(roleInfo.getId());
		RivalListRe re = null;
		HeroXMLInfo heroXMLInfo = null;
		for (int i = 0; i < size; i++) {
			re = new RivalListRe();
			int rd1 = RandomUtil.getRandom(0, mainHeros.size() - 1);
			int mainHeroNo = mainHeros.get(rd1);
			heroXMLInfo = heroMap.get(mainHeroNo);
			if (heroXMLInfo == null) {
				continue;
			}
			// 名字使用随机姓名
			if (heroXMLInfo.getSex() == HeroXMLInfo.SEX_MALE) {
				re.setRoleName(RandomNameXMLMap.randomMaleName());
			} else {
				re.setRoleName(RandomNameXMLMap.randomFemaleName());
			}
			re.setHeroNo(mainHeroNo);
			// 等级在（玩家等级至玩家等级-5）的区间内随机生成
			re.setRoleLevel(mainHeroLv - RandomUtil.getRandom(0, 5));
			if (re.getRoleLevel() <= 0) {
				re.setRoleLevel(1);
			}
			re.setIsNPC((byte) 1);
			re.setProbabilitybyRate(snatchInfo.getChance());
			rivalList.add(re);
		}
		return rivalList;
	}

	/**
	 * 判断战斗是否胜利
	 * @param myFightValue
	 * @param beChallengedFightValue
	 * @return
	 */
	public static boolean calculateFightResult(long myFightValue, long beChallengedFightValue) {
		return calculateResult(calcFightRate(myFightValue, beChallengedFightValue));
	}

	/**
	 * 获取战斗胜利概率
	 * @param myFightValue
	 * @param beChallengedFightValue
	 * @return
	 */
	private static double calcFightRate(long myFightValue, long beChallengedFightValue) {
		if (myFightValue == beChallengedFightValue) {
			return 1.0;
		}
		double min = Math.min(myFightValue, beChallengedFightValue);
		double max = Math.max(myFightValue, beChallengedFightValue);
		double rate = (max - min) / max;
		if (rate > 0 && rate < 0.1) {
			return 0.9;
		} else if (rate >= 0.1 && rate < 0.2) {
			return 0.8;
		} else if (rate >= 0.2 && rate < 0.3) {
			return 0.7;
		} else {
			return 0.5;
		}
	}

	/**
	 * 判断抢夺是否成功
	 * @param myFightValue
	 * @param beChallengedFightValue
	 * @return
	 */
	public static boolean calculateSnatchResult(long myFightValue, long beChallengedFightValue) {
		return calculateResult(calcSnatchRate(myFightValue, beChallengedFightValue));
	}

	/**
	 * 计算夺宝概率
	 * @param myFightValue
	 * @param beChallengedFightValue
	 * @return
	 */
	private static double calcSnatchRate(long myFightValue, long beChallengedFightValue) {
		if (myFightValue == beChallengedFightValue) {
			return 1.0;
		}
		double min = Math.min(myFightValue, beChallengedFightValue);
		double max = Math.max(myFightValue, beChallengedFightValue);
		double rate = (max - min) / max;
		if (rate > 0 && rate < 0.1) {
			return 0.9;
		} else if (rate >= 0.1 && rate < 0.2) {
			return 0.8;
		} else if (rate >= 0.2 && rate < 0.3) {
			return 0.7;
		} else {
			return 0.5;
		}
	}

	/**
	 * 判断抢夺是否成功
	 * @param successRate
	 * @return
	 */
	public static boolean calculateResult(double successRate) {
		int val = (int)(successRate * 100);
		int radom = RandomUtil.getRandom(0, 100);		
		if (radom <= val) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 夺宝石头合成
	 * @param roleInfo
	 * @param stoneNo
	 * @return
	 */
	public static int snatchMix(RoleInfo roleInfo, int stoneNo) {
		List<SnatchInfo> snatchInfos = SnatchMap.getByPropNo(stoneNo);
		if (snatchInfos == null || snatchInfos.size() == 0) {
			return ErrorCode.SNATCH_MIX_1;
		}
		// 扣除石头
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (SnatchInfo snatchInfo : snatchInfos) {
			map.put(snatchInfo.getPatchNo(), 1);
		}
		int status = ItemService.bagItemDel(ActionType.action363.getType(), roleInfo, map);
		if (1 != status) {
			return status;
		}
		// 生成新石头
		List<DropInfo> itemList = new ArrayList<DropInfo>();// 道具
		itemList.add(new DropInfo(String.valueOf(snatchInfos.get(0).getPropNo()), 1));
		status = ItemService.itemAdd(ActionType.action363.getType(), roleInfo, itemList, null, null, null, null,
				true);
		if (status != 1) {
			return status;
		}
		return 1;
	}
}
